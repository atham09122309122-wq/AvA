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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class AvaRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
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
        val user = userDao.getFirstUser()
        if (user != null) {
            _currentUser.value = user
        }
    }

    suspend fun login(identifier: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val user = if (identifier.contains("@")) {
            userDao.getUserByEmail(identifier.trim())
        } else {
            userDao.getUserByUsername(identifier.trim())
        }

        if (user == null) {
            return@withContext Result.failure(Exception("Account not found"))
        }

        if (user.isBanned) {
            return@withContext Result.failure(Exception("This account is currently suspended."))
        }

        if (user.passwordHash != password) {
            return@withContext Result.failure(Exception("Incorrect password"))
        }

        _currentUser.value = user
        Result.success(user)
    }

    suspend fun register(
        displayName: String,
        username: String,
        email: String,
        password: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        if (userDao.getUserByEmail(email.trim()) != null) {
            return@withContext Result.failure(Exception("Email already in use"))
        }
        if (userDao.getUserByUsername(username.trim()) != null) {
            return@withContext Result.failure(Exception("Username already taken"))
        }

        val newUser = UserEntity(
            id = UUID.randomUUID().toString(),
            displayName = displayName.trim(),
            username = username.trim().lowercase(),
            email = email.trim(),
            passwordHash = password,
            coinsBalance = 500,
            preferredLanguage = "ar"
        )
        userDao.insertUser(newUser)

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
                titleAr = "مرحباً بك في مجتمع AVA!",
                messageEn = "You have received 500 AVA Coins. Made by Ayham.",
                messageAr = "حصلت على 500 عملة AVA لبدء رحلتك ومشاركة منشوراتك وتصاميمك.",
                type = "welcome"
            )
        )

        _currentUser.value = newUser
        Result.success(newUser)
    }

    suspend fun logout() {
        _currentUser.value = null
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
        projectDao.insertProject(project)
    }

    suspend fun deleteProject(id: String) = withContext(Dispatchers.IO) {
        projectDao.deleteProject(id)
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
