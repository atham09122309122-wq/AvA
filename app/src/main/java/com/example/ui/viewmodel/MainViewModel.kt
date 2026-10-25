package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BetouAiMessageEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FollowEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ReportEntity
import com.example.data.model.UserEntity
import com.example.data.model.WardrobeItemEntity
import com.example.data.repository.AvaRepository
import com.example.util.AppLanguage
import com.example.util.DemoArtwork
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    IMAGE_EDITOR,
    PERSONAL_PHOTO,
    AD_STUDIO,
    STORY_DESIGNER,
    TEMPLATES,
    MY_PROJECTS,
    CREATE_CHOOSER,
    ABOUT_AVA,
    HELP_REPORT,
    SETTINGS,
    SPLASH,
    EXPLORE,
    CHARACTERS,
    PROFILE,
    CHARACTER_STUDIO,
    CHARACTER_WARDROBE,
    COINS_WALLET,
    NOTIFICATIONS,
    EDIT_PROFILE,
    FOLLOWERS_FOLLOWING,
    CHAT_LIST,
    CHAT_CONVERSATION,
    BETOU_AI,
    FOUNDER_DASHBOARD,
    ADMIN_USERS_MANAGEMENT,
    ACCOUNT_SETTINGS,
    LANGUAGE_SETTINGS,
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
    EMAIL_VERIFICATION
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AvaRepository(application)

    // Current User
    val currentUser: StateFlow<UserEntity?> = repository.currentUser

    // Language State (Defaults to Arabic as preferred in user prompt, can switch to English)
    private val _appLanguage = MutableStateFlow(AppLanguage.ARABIC)
    val appLanguage = _appLanguage.asStateFlow()

    // Screen Navigation Stack (Starts directly on HOME without login barriers)
    private val _screenStack = MutableStateFlow(listOf(AppScreen.HOME))
    val currentScreen: StateFlow<AppScreen> = MutableStateFlow(AppScreen.HOME)

    // Editing State & Payload Transfer
    val activeProject = MutableStateFlow<ProjectEntity?>(null)
    val activeCharacter = MutableStateFlow<CharacterEntity?>(null)
    val hasUnsavedChanges = MutableStateFlow(false)
    val showExitConfirmDialog = MutableStateFlow(false)

    // Chat target
    val activeChatUserId = MutableStateFlow<String>("user_sara_art")
    val activeChatUserName = MutableStateFlow<String>("Sara Visuals")
    val activeChatUserUsername = MutableStateFlow<String>("sara_art")

    // Followers / Following view target
    val targetProfileUserId = MutableStateFlow<String>("user_ayham_creator")
    val targetProfileUserName = MutableStateFlow<String>("Ayham")
    val followersFollowingTab = MutableStateFlow<Int>(0) // 0 = followers, 1 = following

    // User Message / Snackbar
    val statusMessage = MutableStateFlow<String?>(null)

    // Data streams
    val posts: StateFlow<List<PostEntity>> = repository.getAllPostsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val users: StateFlow<List<UserEntity>> = repository.getAllUsersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<ReportEntity>> = repository.getAllReportsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val betouMessages: StateFlow<List<BetouAiMessageEntity>> = repository.getBetouMessagesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectEntity>> = repository.getProjectsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentProjects: StateFlow<List<ProjectEntity>> = repository.getRecentProjectsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val characters: StateFlow<List<CharacterEntity>> = repository.getCharactersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wardrobeItems: StateFlow<List<WardrobeItemEntity>> = repository.getWardrobeItemsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coinTransactions: StateFlow<List<CoinTransactionEntity>> = repository.getCoinTransactionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.getNotificationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.init()
            currentUser.value?.let { user ->
                if (user.preferredLanguage == "en") {
                    _appLanguage.value = AppLanguage.ENGLISH
                } else {
                    _appLanguage.value = AppLanguage.ARABIC
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        val stack = _screenStack.value.toMutableList()
        stack.add(screen)
        _screenStack.value = stack
        (currentScreen as MutableStateFlow).value = screen
    }

    fun navigateBack() {
        if (hasUnsavedChanges.value && (currentScreen.value == AppScreen.IMAGE_EDITOR || currentScreen.value == AppScreen.CHARACTER_STUDIO)) {
            showExitConfirmDialog.value = true
            return
        }
        performBack()
    }

    fun confirmDiscardAndExit() {
        hasUnsavedChanges.value = false
        showExitConfirmDialog.value = false
        performBack()
    }

    private fun performBack() {
        val stack = _screenStack.value.toMutableList()
        if (stack.size > 1) {
            stack.removeAt(stack.size - 1)
            _screenStack.value = stack
            (currentScreen as MutableStateFlow).value = stack.last()
        } else {
            (currentScreen as MutableStateFlow).value = AppScreen.HOME
        }
    }

    fun navigateToBottomDestination(screen: AppScreen) {
        if (hasUnsavedChanges.value && (currentScreen.value == AppScreen.IMAGE_EDITOR || currentScreen.value == AppScreen.CHARACTER_STUDIO)) {
            showExitConfirmDialog.value = true
            return
        }
        _screenStack.value = listOf(screen)
        (currentScreen as MutableStateFlow).value = screen
    }

    fun loadDemoArtworkIntoEditor(artwork: DemoArtwork) {
        val proj = ProjectEntity(
            id = "proj_" + UUID.randomUUID().toString().take(8),
            userId = "local_creator",
            title = artwork.title,
            projectType = "IMAGE_EDITOR",
            aspectRatio = artwork.aspectRatio,
            thumbnailUri = "demo://${artwork.id}",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        activeProject.value = proj
        hasUnsavedChanges.value = false
        navigateTo(AppScreen.IMAGE_EDITOR)
    }

    fun createNewProject(title: String = "AVA Creation", aspectRatio: String = "1:1", imageUri: String? = null) {
        val proj = ProjectEntity(
            id = "proj_" + UUID.randomUUID().toString().take(8),
            userId = "local_creator",
            title = title,
            projectType = "IMAGE_EDITOR",
            aspectRatio = aspectRatio,
            thumbnailUri = imageUri,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        activeProject.value = proj
        hasUnsavedChanges.value = false
        navigateTo(AppScreen.IMAGE_EDITOR)
    }

    fun saveProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.saveProject(project)
            statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم حفظ المشروع محلياً بنجاح" else "Project saved locally"
        }
    }

    fun duplicateProject(project: ProjectEntity) {
        viewModelScope.launch {
            val duplicate = project.copy(
                id = "proj_" + UUID.randomUUID().toString().take(8),
                title = "${project.title} (Copy)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.saveProject(duplicate)
            statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم نسخ المشروع بنجاح" else "Project duplicated"
        }
    }

    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
        viewModelScope.launch {
            repository.setLanguage(language.code)
        }
    }

    // --- Social Actions ---
    fun createPost(content: String, imageType: String? = null, mediaTag: String? = null) {
        viewModelScope.launch {
            val result = repository.createPost(content, imageType, mediaTag)
            if (result.isSuccess) {
                statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم نشر المنشور بنجاح!" else "Post published!"
            } else {
                statusMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun bookmarkPost(postId: String, currentState: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(postId, currentState)
            statusMessage.value = if (!currentState) {
                if (_appLanguage.value == AppLanguage.ARABIC) "تم حفظ المنشور في المحفوظات" else "Saved to Bookmarks"
            } else {
                if (_appLanguage.value == AppLanguage.ARABIC) "تمت الإزالة من المحفوظات" else "Removed from Bookmarks"
            }
        }
    }

    fun addComment(postId: String, text: String) {
        viewModelScope.launch {
            repository.addComment(postId, text)
        }
    }

    fun toggleFollow(targetUserId: String) {
        viewModelScope.launch {
            val isNowFollowed = repository.toggleFollow(targetUserId)
            statusMessage.value = if (isNowFollowed) {
                if (_appLanguage.value == AppLanguage.ARABIC) "تمت المتابعة بنجاح" else "Followed"
            } else {
                if (_appLanguage.value == AppLanguage.ARABIC) "تم إلغاء المتابعة" else "Unfollowed"
            }
        }
    }

    fun sendChatMessage(text: String) {
        val receiver = activeChatUserId.value
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(receiver, text)
        }
    }

    fun sendBetouPrompt(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            repository.sendBetouMessage(prompt)
        }
    }

    fun submitReport(targetType: String, targetId: String, reason: String, details: String) {
        viewModelScope.launch {
            repository.submitReport(targetType, targetId, reason, details)
            statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم إرسال البلاغ بنجاح للإدارة" else "Report submitted to founder team"
        }
    }

    fun toggleVerification(userId: String, isVerified: Boolean) {
        viewModelScope.launch {
            repository.toggleUserVerification(userId, isVerified)
            statusMessage.value = if (isVerified) "تم توثيق الحساب بالشارة الرسمية" else "تم إلغاء توثيق الحساب"
        }
    }

    fun toggleBan(userId: String, isBanned: Boolean) {
        viewModelScope.launch {
            repository.toggleUserBan(userId, isBanned)
            statusMessage.value = if (isBanned) "تم حظر المستخدم ومنعه من النشر" else "تم فك الحظر عن المستخدم"
        }
    }

    fun resolveReport(reportId: String, status: String) {
        viewModelScope.launch {
            repository.resolveReport(reportId, status)
            statusMessage.value = "تم تحديث حالة البلاغ: $status"
        }
    }

    fun claimDailyCoins() {
        viewModelScope.launch {
            val success = repository.claimDailyBonus()
            if (success) {
                statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تمت إضافة 50 عملة بنجاح!" else "+50 AVA Coins claimed!"
            }
        }
    }

    fun purchaseWardrobe(item: WardrobeItemEntity) {
        viewModelScope.launch {
            val result = repository.purchaseWardrobeItem(item)
            if (result.isSuccess) {
                statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم فتح العنصر بنجاح!" else "Item unlocked!"
            } else {
                statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "رصيد العملات غير كافٍ!" else "Insufficient AVA Coins!"
            }
        }
    }

    fun clearStatusMessage() {
        statusMessage.value = null
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            repository.deleteProject(id)
        }
    }

    fun deleteCharacter(id: String) {
        viewModelScope.launch {
            repository.deleteCharacter(id)
        }
    }

    fun setAvatar(character: CharacterEntity) {
        viewModelScope.launch {
            repository.setCharacterAsAvatar(character)
            statusMessage.value = if (_appLanguage.value == AppLanguage.ARABIC) "تم تعيين الصورة الرمزية للملف الشخصي!" else "Profile avatar updated!"
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }
}
