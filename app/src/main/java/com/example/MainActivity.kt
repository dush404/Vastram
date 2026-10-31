package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppScreen
import com.example.data.LocalAppLanguage
import com.example.data.LocalUserRole
import com.example.data.UserRole
import com.example.ui.components.VastramBottomNavBar
import com.example.ui.components.VastramFloatingToast
import com.example.ui.components.VastramTopHeader
import com.example.ui.screens.ClusterManagerScreen
import com.example.ui.screens.DiscoveryFeedScreen
import com.example.ui.screens.LoomDeskScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.QrVerificationScreen
import com.example.ui.screens.RoleLoginScreen
import com.example.ui.screens.SocialProofReviewsScreen
import com.example.ui.screens.VoiceStudioScreen
import com.example.ui.screens.WeaverProfileScreen
import com.example.ui.theme.VastramTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VastramTheme {
                VastramMarketplaceApp()
            }
        }
    }
}

@Composable
fun VastramMarketplaceApp() {
    var loggedInRole by remember { mutableStateOf<UserRole?>(null) }
    var currentScreen by remember { mutableStateOf(AppScreen.DISCOVERY_FEED) }
    val navigationBackStack = remember { mutableStateListOf<AppScreen>() }
    var selectedLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val primaryTabs = remember(loggedInRole) {
        if (loggedInRole == UserRole.SELLER) {
            setOf(
                AppScreen.DISCOVERY_FEED,
                AppScreen.VOICE_STUDIO,
                AppScreen.LOOM_DESK,
                AppScreen.CLUSTER_MANAGER,
                AppScreen.WEAVER_PROFILE
            )
        } else {
            setOf(
                AppScreen.DISCOVERY_FEED,
                AppScreen.PRODUCT_DETAIL,
                AppScreen.QR_VERIFICATION,
                AppScreen.ORDER_TRACKING,
                AppScreen.WEAVER_PROFILE
            )
        }
    }

    // Auto-dismiss floating heritage toast after 3.0 seconds
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3000)
            toastMessage = null
        }
    }

    val navigateTo: (AppScreen) -> Unit = { targetScreen ->
        if (targetScreen != currentScreen) {
            navigationBackStack.add(currentScreen)
            currentScreen = targetScreen
        }
    }

    val navigateBack: () -> Unit = {
        if (navigationBackStack.isNotEmpty()) {
            currentScreen = navigationBackStack.removeAt(navigationBackStack.lastIndex)
        } else if (currentScreen != AppScreen.DISCOVERY_FEED) {
            currentScreen = AppScreen.DISCOVERY_FEED
        } else if (loggedInRole != null) {
            loggedInRole = null
        }
    }

    // BackHandler for secondary screens, non-home tabs, and returning to role login
    BackHandler(enabled = loggedInRole != null) {
        navigateBack()
    }

    val showToast: (String) -> Unit = { msg ->
        toastMessage = msg
    }

    val activeRole = loggedInRole
    if (activeRole == null) {
        CompositionLocalProvider(LocalAppLanguage provides selectedLanguage) {
            Box(modifier = Modifier.fillMaxSize()) {
                RoleLoginScreen(
                    selectedLanguage = selectedLanguage,
                    onSelectLanguage = { lang -> selectedLanguage = lang },
                    onLoginWithRole = { chosenRole ->
                        loggedInRole = chosenRole
                        currentScreen = AppScreen.DISCOVERY_FEED
                        navigationBackStack.clear()
                    }
                )
                VastramFloatingToast(
                    message = toastMessage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    } else {
        CompositionLocalProvider(
            LocalAppLanguage provides selectedLanguage,
            LocalUserRole provides activeRole
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    VastramTopHeader(
                        currentScreen = currentScreen,
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { lang ->
                            selectedLanguage = lang
                        },
                        onNavigate = navigateTo,
                        onBack = if (currentScreen !in primaryTabs) navigateBack else null,
                        onToast = showToast,
                        onSwitchRoleClick = {
                            loggedInRole = null
                            navigationBackStack.clear()
                        }
                    )
                },
                bottomBar = {
                    VastramBottomNavBar(
                        currentScreen = currentScreen,
                        onSelectScreen = navigateTo
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                        },
                        label = "vastram_screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.DISCOVERY_FEED -> {
                                DiscoveryFeedScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.PRODUCT_DETAIL -> {
                                ProductDetailScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.WEAVER_PROFILE -> {
                                WeaverProfileScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.QR_VERIFICATION -> {
                                QrVerificationScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.VOICE_STUDIO -> {
                                VoiceStudioScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.LOOM_DESK -> {
                                LoomDeskScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.ORDER_TRACKING -> {
                                OrderTrackingScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.CLUSTER_MANAGER -> {
                                ClusterManagerScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }

                            AppScreen.SOCIAL_REVIEWS -> {
                                SocialProofReviewsScreen(
                                    onNavigate = navigateTo,
                                    onToast = showToast
                                )
                            }
                        }
                    }

                    // Floating Heritage Toast Notification
                    VastramFloatingToast(
                        message = toastMessage,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}
