package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.components.AvaBottomBar
import com.example.ui.components.AvaTopBar
import com.example.ui.screens.adstudio.AdStudioWizardScreen
import com.example.ui.screens.ai.BetouAiScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.character.CharacterStudioScreen
import com.example.ui.screens.character.CharacterWardrobeScreen
import com.example.ui.screens.character.CharactersScreen
import com.example.ui.screens.character.CoinsWalletScreen
import com.example.ui.screens.chat.ChatConversationScreen
import com.example.ui.screens.chat.ChatListScreen
import com.example.ui.screens.editor.ImageEditorScreen
import com.example.ui.screens.explore.ExploreScreen
import com.example.ui.screens.explore.ExploreUsersPostsScreen
import com.example.ui.screens.feed.FeedScreen
import com.example.ui.screens.founder.AdminUserManagementScreen
import com.example.ui.screens.founder.FounderDashboardScreen
import com.example.ui.screens.home.CreateChooserScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.photostudio.PersonalPhotoStudioScreen
import com.example.ui.screens.profile.EditProfileScreen
import com.example.ui.screens.profile.FollowersFollowingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.projects.MyProjectsScreen
import com.example.ui.screens.settings.AboutAvaScreen
import com.example.ui.screens.settings.AccountSettingsScreen
import com.example.ui.screens.settings.HelpAndReportScreen
import com.example.ui.screens.settings.LanguageSettingsScreen
import com.example.ui.screens.settings.NotificationsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.templates.StoryDesignerScreen
import com.example.ui.screens.templates.TemplatesScreen
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage
import com.example.util.ProvideAppLanguage

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appLanguage by viewModel.appLanguage.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val user by viewModel.currentUser.collectAsState()
            val notifications by viewModel.notifications.collectAsState()
            val statusMessage by viewModel.statusMessage.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(statusMessage) {
                statusMessage?.let {
                    snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                    viewModel.clearStatusMessage()
                }
            }

            ProvideAppLanguage(language = appLanguage) {
                MyApplicationTheme {
                    val primaryScreens = listOf(
                        AppScreen.HOME,
                        AppScreen.IMAGE_EDITOR,
                        AppScreen.CREATE_CHOOSER,
                        AppScreen.TEMPLATES,
                        AppScreen.MY_PROJECTS
                    )
                    val showBottomNav = currentScreen in primaryScreens && currentScreen != AppScreen.IMAGE_EDITOR
                    val showTopBar = currentScreen in listOf(
                        AppScreen.HOME,
                        AppScreen.CREATE_CHOOSER,
                        AppScreen.TEMPLATES,
                        AppScreen.MY_PROJECTS
                    )

                    // Handle back press
                    BackHandler(enabled = currentScreen != AppScreen.HOME) {
                        viewModel.navigateBack()
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AvaBlack),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            if (showTopBar) {
                                Box(modifier = Modifier.statusBarsPadding()) {
                                    AvaTopBar(
                                        onLanguageClick = {
                                            val nextLang = if (appLanguage == AppLanguage.ENGLISH) AppLanguage.ARABIC else AppLanguage.ENGLISH
                                            viewModel.setLanguage(nextLang)
                                        },
                                        onHelpClick = { viewModel.navigateTo(AppScreen.HELP_REPORT) },
                                        onSettingsClick = { viewModel.navigateTo(AppScreen.ACCOUNT_SETTINGS) }
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            if (showBottomNav) {
                                AvaBottomBar(
                                    currentScreen = currentScreen,
                                    onNavigate = { screen ->
                                        viewModel.navigateToBottomDestination(screen)
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(AvaBlack)
                        ) {
                            AnimatedContent(
                                targetState = currentScreen,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "ScreenTransition"
                            ) { target ->
                                when (target) {
                                    AppScreen.HOME -> HomeScreen(viewModel)
                                    AppScreen.IMAGE_EDITOR -> ImageEditorScreen(viewModel)
                                    AppScreen.CREATE_CHOOSER -> CreateChooserScreen(viewModel)
                                    AppScreen.TEMPLATES -> TemplatesScreen(viewModel)
                                    AppScreen.MY_PROJECTS -> MyProjectsScreen(viewModel)

                                    // Creative Studios
                                    AppScreen.AD_STUDIO -> AdStudioWizardScreen(viewModel)
                                    AppScreen.PERSONAL_PHOTO -> PersonalPhotoStudioScreen(viewModel)
                                    AppScreen.STORY_DESIGNER -> StoryDesignerScreen(viewModel)

                                    // Support, Settings & Info
                                    AppScreen.HELP_REPORT -> HelpAndReportScreen(viewModel)
                                    AppScreen.ACCOUNT_SETTINGS -> AccountSettingsScreen(viewModel)
                                    AppScreen.LANGUAGE_SETTINGS -> LanguageSettingsScreen(viewModel)
                                    AppScreen.ABOUT_AVA -> AboutAvaScreen(viewModel)

                                    // Preserved / fallback routes for modular compatibility
                                    AppScreen.SPLASH -> HomeScreen(viewModel)
                                    AppScreen.EXPLORE -> TemplatesScreen(viewModel)
                                    AppScreen.CHARACTERS -> HomeScreen(viewModel)
                                    AppScreen.PROFILE -> AccountSettingsScreen(viewModel)
                                    AppScreen.CHAT_LIST -> HomeScreen(viewModel)
                                    AppScreen.CHAT_CONVERSATION -> HomeScreen(viewModel)
                                    AppScreen.BETOU_AI -> HomeScreen(viewModel)
                                    AppScreen.FOLLOWERS_FOLLOWING -> HomeScreen(viewModel)
                                    AppScreen.FOUNDER_DASHBOARD -> HomeScreen(viewModel)
                                    AppScreen.ADMIN_USERS_MANAGEMENT -> HomeScreen(viewModel)
                                    AppScreen.CHARACTER_STUDIO -> HomeScreen(viewModel)
                                    AppScreen.CHARACTER_WARDROBE -> HomeScreen(viewModel)
                                    AppScreen.COINS_WALLET -> HomeScreen(viewModel)
                                    AppScreen.NOTIFICATIONS -> NotificationsScreen(viewModel)
                                    AppScreen.EDIT_PROFILE -> EditProfileScreen(viewModel)
                                    AppScreen.LOGIN -> HomeScreen(viewModel)
                                    AppScreen.REGISTER -> HomeScreen(viewModel)
                                    AppScreen.FORGOT_PASSWORD -> HomeScreen(viewModel)
                                    else -> HomeScreen(viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
