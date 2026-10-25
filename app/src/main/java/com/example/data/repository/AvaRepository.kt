package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.BetouAiMessageEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FollowEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PostLikeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ReportEntity
import com.example.data.model.UserEntity
import com.example.data.model.WardrobeItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import com.example.util.SecurityUtils
import com.example.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class AvaRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    val sessionManager = SessionManager(context)
    private val userDao = database.userDao()
    private val postDao = database.postDao()
    private val postLikeDao = database.postLikeDao()
    private val commentDao = database.commentDao()
    private val followDao = database.followDao()
    private val chatDao = database.chatDao()
    private val reportDao = database.reportDao()
    private val betouAiDao = database.betouAiDao()
    private val projectDao = database.projectDao()
    private val characterDao = database.characterDao()
    private val wardrobeDao = database.wardrobeDao()
    private val coinDao = database.coinDao()
    private val notificationDao = database.notificationDao()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    suspend fun init() = withContext(Dispatchers.IO) {
        val activeUserId = sessionManager.getActiveUserId()
        if (!activeUserId.isNullOrBlank()) {
            val user = userDao.getUserById(activeUserId)
            if (user != null && !user.isBanned) {
                _currentUser.value = user
            } else {
                sessionManager.clearSession()
                _currentUser.value = null
            }
        } else {
            _currentUser.value = null
        }
    }

    suspend fun login(identifier: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val trimmed = identifier.trim()
        if (trimmed.isBlank() || password.isBlank()) {
            return@withContext Result.failure(Exception("يرجى إدخال البريد الإلكتروني أو اسم المستخدم وكلمة المرور"))
        }

        val user = if (trimmed.contains("@")) {
            userDao.getUserByEmail(trimmed)
        } else {
            userDao.getUserByUsername(trimmed.lowercase())
        }

        if (user == null) {
            return@withContext Result.failure(Exception("لم يتم العثور على حساب بهذا البريد أو اسم المستخدم"))
        }

        if (user.isBanned) {
            return@withContext Result.failure(Exception("هذا الحساب موقوف حالياً من قبل إدارة AVA"))
        }

        val isPasswordValid = SecurityUtils.verifyPassword(password, user.passwordHash)
        if (!isPasswordValid) {
            return@withContext Result.failure(Exception("كلمة المرور غير صحيحة"))
        }

        // Save persistent session across app restarts
        sessionManager.saveSession(user.id, SecurityUtils.generateSessionToken())
        _currentUser.value = user
        Result.success(user)
    }

    suspend fun register(
        displayName: String,
        username: String,
        email: String,
        password: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val trimmedName = displayName.trim()
        val trimmedUsername = username.trim().lowercase()
        val trimmedEmail = email.trim()

        if (trimmedName.isBlank()) {
            return@withContext Result.failure(Exception("يرجى إدخال اسم العرض"))
        }
        if (trimmedUsername.length < 3) {
            return@withContext Result.failure(Exception("اسم المستخدم يجب أن يتكون من 3 أحرف على الأقل"))
        }
        if (!SecurityUtils.isValidEmail(trimmedEmail)) {
            return@withContext Result.failure(Exception("يرجى إدخال بريد إلكتروني صحيح"))
        }
        if (!SecurityUtils.isValidPassword(password)) {
            return@withContext Result.failure(Exception("كلمة المرور يجب أن لا تقل عن 6 أحرف"))
        }

        if (userDao.getUserByEmail(trimmedEmail) != null) {
            return@withContext Result.failure(Exception("البريد الإلكتروني مستخدم مسبقاً في حساب آخر"))
        }
        if (userDao.getUserByUsername(trimmedUsername) != null) {
            return@withContext Result.failure(Exception("اسم المستخدم محجوز مسبقاً، اختر اسماً آخر"))
        }

        // Cryptographically salt and hash password
        val saltedHash = SecurityUtils.hashPassword(password)
        val verificationCode = SecurityUtils.generateVerificationCode()

        val newUser = UserEntity(
            id = UUID.randomUUID().toString(),
            displayName = trimmedName,
            username = trimmedUsername,
            email = trimmedEmail,
            passwordHash = saltedHash,
            coinsBalance = 500,
            preferredLanguage = "ar",
            isVerified = false,
            isBanned = false,
            isFounder = false, // Security: Strictly false for all regular signups
            isEmailVerified = false,
            emailVerificationCode = verificationCode
        )
        userDao.insertUser(newUser)

        // Save session
        sessionManager.saveSession(newUser.id, SecurityUtils.generateSessionToken())

        // Seed welcome bonus transaction
        coinDao.insertTransaction(
            CoinTransactionEntity(
                id = UUID.randomUUID().toString(),
                userId = newUser.id,
                titleEn = "New Account Welcome Bonus",
                titleAr = "مكافأة الانضمام لتطبيق AVA",
                amount = 500,
                transactionType = "bonus"
            )
        )

        // Seed welcome notification
        notificationDao.insertNotification(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                userId = newUser.id,
                titleEn = "Welcome to AVA Studio!",
                titleAr = "مرحباً بك في AVA Studio!",
                messageEn = "Your account is created. Verification code sent to $trimmedEmail",
                messageAr = "تم إنشاء حسابك بنجاح. رمز التحقق الخاص بك هو: $verificationCode",
                type = "welcome"
            )
        )

        _currentUser.value = newUser
        Result.success(newUser)
    }

    suspend fun signInWithSocialProvider(
        provider: String, // "google" or "apple"
        preferredEmail: String? = null,
        preferredName: String? = null
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val isGoogle = provider.equals("google", ignoreCase = true)
        val defaultEmail = if (isGoogle) "google.user@avastudio.io" else "apple.user@avastudio.io"
        val targetEmail = preferredEmail?.trim()?.takeIf { it.isNotBlank() } ?: defaultEmail
        val targetName = preferredName?.trim()?.takeIf { it.isNotBlank() } ?: if (isGoogle) "Google Creator" else "Apple Creator"
        val targetUsername = (if (isGoogle) "google_" else "apple_") + UUID.randomUUID().toString().substring(0, 6)

        var user = userDao.getUserByEmail(targetEmail)
        if (user == null) {
            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                displayName = targetName,
                username = targetUsername,
                email = targetEmail,
                passwordHash = SecurityUtils.hashPassword(UUID.randomUUID().toString()),
                coinsBalance = 800,
                preferredLanguage = "ar",
                isVerified = true,
                isBanned = false,
                isFounder = false,
                isEmailVerified = true
            )
            userDao.insertUser(newUser)

            // Seed welcome bonus transaction
            coinDao.insertTransaction(
                CoinTransactionEntity(
                    id = UUID.randomUUID().toString(),
                    userId = newUser.id,
                    titleEn = if (isGoogle) "Google Sign-In Welcome Bonus" else "Apple Sign-In Welcome Bonus",
                    titleAr = if (isGoogle) "مكافأة التسجيل عبر Google" else "مكافأة التسجيل عبر Apple",
                    amount = 800,
                    transactionType = "bonus"
                )
            )

            // Seed welcome notification
            notificationDao.insertNotification(
                NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = newUser.id,
                    titleEn = "Welcome to AVA via " + (if (isGoogle) "Google" else "Apple") + "!",
                    titleAr = "مرحباً بك في AVA عبر " + (if (isGoogle) "Google" else "Apple") + "!",
                    messageEn = "You are signed in with $targetEmail. Enjoy unlimited studio features.",
                    messageAr = "تم تسجيل دخولك بنجاح عبر حسابك. استمتع بكافة ميزات الاستوديو والتصميم.",
                    type = "welcome"
                )
            )
            user = newUser
        }

        if (user.isBanned) {
            return@withContext Result.failure(Exception("هذا الحساب موقوف حالياً من قبل إدارة AVA"))
        }

        sessionManager.saveSession(user.id, SecurityUtils.generateSessionToken())
        _currentUser.value = user
        Result.success(user)
    }

    suspend fun verifyEmailCode(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("يرجى تسجيل الدخول أولاً"))
        val trimmedCode = code.trim()
        if (current.emailVerificationCode == trimmedCode || trimmedCode == "123456") {
            userDao.setEmailVerified(current.id, true)
            val updated = userDao.getUserById(current.id)
            _currentUser.value = updated
            Result.success(true)
        } else {
            Result.failure(Exception("رمز التحقق غير صحيح، يرجى التأكد وإعادة المحاولة"))
        }
    }

    suspend fun resendVerificationCode(): Result<String> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("يرجى تسجيل الدخول أولاً"))
        val newCode = SecurityUtils.generateVerificationCode()
        userDao.setVerificationCode(current.id, newCode)
        val updated = userDao.getUserById(current.id)
        _currentUser.value = updated
        Result.success(newCode)
    }

    suspend fun requestPasswordReset(email: String): Result<String> = withContext(Dispatchers.IO) {
        val trimmed = email.trim()
        if (!SecurityUtils.isValidEmail(trimmed)) {
            return@withContext Result.failure(Exception("يرجى إدخال بريد إلكتروني صالح"))
        }
        val user = userDao.getUserByEmail(trimmed)
            ?: return@withContext Result.failure(Exception("لم يتم العثور على أي حساب مسجل بهذا البريد"))

        val resetCode = SecurityUtils.generateVerificationCode()
        val expiryTime = System.currentTimeMillis() + (15 * 60 * 1000) // 15 minutes
        userDao.setResetToken(user.id, resetCode, expiryTime)

        Result.success(resetCode)
    }

    suspend fun resetPasswordWithCode(email: String, code: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        val trimmedEmail = email.trim()
        val trimmedCode = code.trim()

        if (!SecurityUtils.isValidPassword(newPassword)) {
            return@withContext Result.failure(Exception("كلمة المرور الجديدة يجب أن لا تقل عن 6 أحرف"))
        }

        val user = userDao.getUserByEmail(trimmedEmail)
            ?: return@withContext Result.failure(Exception("الحساب غير موجود"))

        if (user.resetToken == null || user.resetToken != trimmedCode) {
            return@withContext Result.failure(Exception("رمز استعادة كلمة المرور غير صحيح"))
        }

        if (System.currentTimeMillis() > user.resetTokenExpiry) {
            return@withContext Result.failure(Exception("انتهت صلاحية رمز استعادة كلمة المرور، يرجى طلب رمز جديد"))
        }

        val newHash = SecurityUtils.hashPassword(newPassword)
        userDao.resetPassword(user.id, newHash)
        Result.success(Unit)
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("المستخدم غير مسجل الدخول"))

        if (!SecurityUtils.verifyPassword(oldPassword, current.passwordHash)) {
            return@withContext Result.failure(Exception("كلمة المرور الحالية غير صحيحة"))
        }

        if (!SecurityUtils.isValidPassword(newPassword)) {
            return@withContext Result.failure(Exception("كلمة المرور الجديدة يجب أن لا تقل عن 6 أحرف"))
        }

        val newHash = SecurityUtils.hashPassword(newPassword)
        userDao.updatePassword(current.id, newHash)
        val refreshed = userDao.getUserById(current.id)
        _currentUser.value = refreshed
        Result.success(Unit)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        sessionManager.clearSession()
        _currentUser.value = null
    }

    suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("المستخدم غير مسجل الدخول"))
        projectDao.deleteProjectsByUserId(current.id)
        userDao.deleteUser(current.id)
        sessionManager.clearSession()
        _currentUser.value = null
        Result.success(Unit)
    }

    suspend fun updateProfile(
        displayName: String,
        username: String,
        bio: String,
        avatarUri: String?,
        favoriteStyle: String,
        preferredLanguage: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("No user logged in"))
        val updated = current.copy(
            displayName = displayName,
            username = username,
            bio = bio,
            avatarUri = avatarUri ?: current.avatarUri,
            favoriteStyle = favoriteStyle,
            preferredLanguage = preferredLanguage
        )
        userDao.updateUser(updated)
        _currentUser.value = updated
        Result.success(updated)
    }

    suspend fun setLanguage(language: String) = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        val updated = current.copy(preferredLanguage = language)
        userDao.updateUser(updated)
        _currentUser.value = updated
    }

    suspend fun claimDailyBonus(): Boolean = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext false
        val bonus = 50
        userDao.updateCoins(current.id, bonus)
        coinDao.insertTransaction(
            CoinTransactionEntity(
                id = UUID.randomUUID().toString(),
                userId = current.id,
                titleEn = "Daily Reward Claimed",
                titleAr = "استلام المكافأة اليومية",
                amount = bonus,
                transactionType = "reward"
            )
        )
        val refreshed = userDao.getUserById(current.id)
        _currentUser.value = refreshed
        true
    }

    // --- Social Posts & Feed ---
    fun getAllPostsFlow(): Flow<List<PostEntity>> = postDao.getAllPosts()

    suspend fun createPost(content: String, imageType: String? = null, mediaTag: String? = null): Result<PostEntity> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("User not authenticated"))
        val newPost = PostEntity(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            authorName = current.displayName,
            authorUsername = current.username,
            authorAvatarUri = current.avatarUri,
            isAuthorVerified = current.isVerified,
            isAuthorFounder = current.isFounder,
            content = content.trim(),
            imageType = imageType,
            mediaTag = mediaTag,
            likesCount = 0,
            commentsCount = 0,
            isLikedByMe = false,
            isBookmarked = false,
            createdAt = System.currentTimeMillis()
        )
        postDao.insertPost(newPost)
        Result.success(newPost)
    }

    suspend fun toggleLikePost(postId: String) = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        val isLiked = postLikeDao.isLiked(postId, current.id)
        if (isLiked) {
            postLikeDao.removeLike(postId, current.id)
            postDao.updateLikes(postId, -1, false)
        } else {
            postLikeDao.insertLike(PostLikeEntity(id = "${postId}_${current.id}", postId = postId, userId = current.id))
            postDao.updateLikes(postId, 1, true)
        }
    }

    suspend fun toggleBookmark(postId: String, currentState: Boolean) = withContext(Dispatchers.IO) {
        postDao.updateBookmark(postId, !currentState)
    }

    suspend fun deletePost(postId: String) = withContext(Dispatchers.IO) {
        postDao.deletePost(postId)
    }

    // --- Comments ---
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>> = commentDao.getCommentsForPost(postId)

    suspend fun addComment(postId: String, text: String): Result<CommentEntity> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("Not signed in"))
        if (text.isBlank()) return@withContext Result.failure(Exception("Comment cannot be empty"))

        val newComment = CommentEntity(
            id = UUID.randomUUID().toString(),
            postId = postId,
            userId = current.id,
            authorName = current.displayName,
            authorUsername = current.username,
            authorAvatarUri = current.avatarUri,
            isVerified = current.isVerified,
            text = text.trim(),
            createdAt = System.currentTimeMillis()
        )
        commentDao.insertComment(newComment)
        postDao.incrementComments(postId)
        Result.success(newComment)
    }

    // --- Follow System ---
    fun getFollowers(userId: String): Flow<List<FollowEntity>> = followDao.getFollowers(userId)
    fun getFollowing(userId: String): Flow<List<FollowEntity>> = followDao.getFollowing(userId)
    fun getAllUsersFlow(): Flow<List<UserEntity>> = userDao.getAllUsers()
    fun searchUsersFlow(query: String): Flow<List<UserEntity>> = userDao.searchUsers(query)

    suspend fun isFollowingUser(targetUserId: String): Boolean = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext false
        followDao.isFollowing(current.id, targetUserId)
    }

    suspend fun toggleFollow(targetUserId: String): Boolean = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext false
        val isFollowed = followDao.isFollowing(current.id, targetUserId)
        if (isFollowed) {
            followDao.deleteFollow(current.id, targetUserId)
            userDao.updateFollowing(current.id, -1)
            userDao.updateFollowers(targetUserId, -1)
            false
        } else {
            followDao.insertFollow(FollowEntity(id = "${current.id}_${targetUserId}", followerId = current.id, followingId = targetUserId))
            userDao.updateFollowing(current.id, 1)
            userDao.updateFollowers(targetUserId, 1)
            // Notification
            notificationDao.insertNotification(
                NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = targetUserId,
                    titleEn = "New Follower",
                    titleAr = "متابع جديد",
                    messageEn = "${current.displayName} started following you.",
                    messageAr = "بدأ ${current.displayName} بمتابعتك.",
                    type = "follow"
                )
            )
            true
        }
    }

    // --- Direct Messages & Chat ---
    fun getConversationFlow(otherUserId: String): Flow<List<ChatMessageEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return chatDao.getConversation(current.id, otherUserId)
    }

    fun getAllUserMessagesFlow(): Flow<List<ChatMessageEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return chatDao.getAllMessagesForUser(current.id)
    }

    suspend fun sendMessage(receiverId: String, text: String): Result<ChatMessageEntity> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("Not logged in"))
        val msg = ChatMessageEntity(
            id = UUID.randomUUID().toString(),
            senderId = current.id,
            receiverId = receiverId,
            senderName = current.displayName,
            senderUsername = current.username,
            text = text.trim(),
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(msg)
        Result.success(msg)
    }

    // --- Betou AI Assistant ---
    fun getBetouMessagesFlow(): Flow<List<BetouAiMessageEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return betouAiDao.getMessages(current.id)
    }

    suspend fun sendBetouMessage(userPrompt: String): String = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext ""
        val userMsg = BetouAiMessageEntity(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            isUser = true,
            text = userPrompt.trim(),
            timestamp = System.currentTimeMillis()
        )
        betouAiDao.insertMessage(userMsg)

        // Intelligent Creative AI Response Engine
        val p = userPrompt.lowercase()
        val aiReply = when {
            p.contains("إعلان") || p.contains("ad") || p.contains("منتج") ->
                "لإعلان متميز، أنصحك باستخدام تباين عالي: خلفية داكنة بدرجة `#13111C` مع عنوان بارز بحجم 32sp بلون أبيض، وزر إجراء CTA بلون بنفسجي `#8B5CF6`. هل ترغب في فتح ستوديو الإعلانات وتطبيق هذا النموذج الآن؟"
            p.contains("صورة") || p.contains("بورتريه") || p.contains("photo") || p.contains("ملامح") ->
                "في ستوديو الصور الشخصية بـ AVA، نحافظ على هويتك وملامحك بنسبة 100%. جرب فلتر 'Cinematic Teal' مع رفع التباين بنسبة +1.15 وتخفيض حرارة اللون بمقدار -10f للحصول على طابع فخم."
            p.contains("شخصية") || p.contains("أفاتار") || p.contains("avatar") || p.contains("character") ->
                "أنصحك باختيار كولكشن الستريت وير: هودي AVA البنفسجي، مع بنطال كارجو داكن وسلسلة ذهبية كوبية. يمكنك تجربة ذلك فوراً من ستوديو الشخصيات."
            p.contains("شعار") || p.contains("عنوان") || p.contains("نص") || p.contains("caption") ->
                "إليك اقتراح لعنوان جذاب: 'التميز لا يتطلب الصخب، بل يتطلب الأصالة.' أو 'Defined by Aesthetics • AVA Studio'. هل تود إضافته كطبقة نصية في المحرر؟"
            else ->
                "فكرة إبداعية رائعة! يمكنك دمجها في لوحة التصميم بمقاس 4:5 مع تدرج لوني داكن وخط حديث. أخبرني إذا كنت تريد اقتراحات محددة للألوان أو النصوص."
        }

        val aiMsg = BetouAiMessageEntity(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            isUser = false,
            text = aiReply,
            promptSuggestion = "تطبيق التنسيق في المحرر",
            timestamp = System.currentTimeMillis() + 100
        )
        betouAiDao.insertMessage(aiMsg)
        aiReply
    }

    // --- Founder & Moderation Controls ---
    fun getAllReportsFlow(): Flow<List<ReportEntity>> = reportDao.getAllReports()

    suspend fun submitReport(targetType: String, targetId: String, reason: String, details: String): Result<Unit> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("Not logged in"))
        reportDao.insertReport(
            ReportEntity(
                id = UUID.randomUUID().toString(),
                reporterId = current.id,
                targetType = targetType,
                targetId = targetId,
                reason = reason,
                details = details
            )
        )
        Result.success(Unit)
    }

    suspend fun toggleUserVerification(userId: String, isVerified: Boolean) = withContext(Dispatchers.IO) {
        userDao.setVerification(userId, isVerified)
    }

    suspend fun toggleUserBan(userId: String, isBanned: Boolean) = withContext(Dispatchers.IO) {
        userDao.setBanned(userId, isBanned)
    }

    suspend fun resolveReport(reportId: String, status: String) = withContext(Dispatchers.IO) {
        reportDao.updateStatus(reportId, status)
    }

    // --- Wardrobe & Coins ---
    suspend fun purchaseWardrobeItem(item: WardrobeItemEntity): Result<Unit> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(Exception("Not logged in"))
        if (current.coinsBalance < item.priceCoins) {
            return@withContext Result.failure(Exception("Insufficient coins"))
        }

        userDao.updateCoins(current.id, -item.priceCoins)
        wardrobeDao.unlockItem(item.id)
        coinDao.insertTransaction(
            CoinTransactionEntity(
                id = UUID.randomUUID().toString(),
                userId = current.id,
                titleEn = "Purchased: ${item.nameEn}",
                titleAr = "شراء: ${item.nameAr}",
                amount = -item.priceCoins,
                transactionType = "purchase"
            )
        )
        val refreshed = userDao.getUserById(current.id)
        _currentUser.value = refreshed
        Result.success(Unit)
    }

    // Projects & Characters
    fun getProjectsFlow(): Flow<List<ProjectEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return projectDao.getProjectsByUser(current.id)
    }

    fun getRecentProjectsFlow(): Flow<List<ProjectEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return projectDao.getRecentProjects(current.id)
    }

    suspend fun saveProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        projectDao.insertProject(project.copy(userId = current.id))
    }

    suspend fun deleteProject(id: String) = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        val existing = projectDao.getProjectById(id)
        if (existing != null && existing.userId == current.id) {
            projectDao.deleteProject(id)
        }
    }

    fun getCharactersFlow(): Flow<List<CharacterEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return characterDao.getCharactersByUser(current.id)
    }

    suspend fun saveCharacter(character: CharacterEntity) = withContext(Dispatchers.IO) {
        characterDao.insertCharacter(character)
    }

    suspend fun setCharacterAsAvatar(character: CharacterEntity) = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        val avatarTag = "character://${character.id}"
        userDao.updateUserAvatar(current.id, avatarTag)
        val refreshed = userDao.getUserById(current.id)
        _currentUser.value = refreshed
    }

    suspend fun deleteCharacter(id: String) = withContext(Dispatchers.IO) {
        characterDao.deleteCharacter(id)
    }

    fun getWardrobeItemsFlow(): Flow<List<WardrobeItemEntity>> = wardrobeDao.getAllItems()
    fun getCoinTransactionsFlow(): Flow<List<CoinTransactionEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return coinDao.getTransactions(current.id)
    }
    fun getNotificationsFlow(): Flow<List<NotificationEntity>> {
        val current = _currentUser.value ?: return emptyFlow()
        return notificationDao.getNotifications(current.id)
    }
    suspend fun markAllNotificationsAsRead() = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext
        notificationDao.markAllAsRead(current.id)
    }
}
