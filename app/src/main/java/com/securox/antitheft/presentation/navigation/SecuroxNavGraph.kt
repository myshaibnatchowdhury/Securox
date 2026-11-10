package com.securox.antitheft.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.securox.antitheft.core.theme.SecuroxBg
import com.securox.antitheft.core.theme.SecuroxCardBorder
import com.securox.antitheft.core.theme.SecuroxNeonPrimary
import com.securox.antitheft.core.theme.SecuroxSurface
import com.securox.antitheft.core.theme.SecuroxTextSecondary
import com.securox.antitheft.presentation.screens.AboutScreen
import com.securox.antitheft.presentation.screens.AccountSettingsScreen
import com.securox.antitheft.presentation.screens.AntiShutdownScreen
import com.securox.antitheft.presentation.screens.CommandHistoryScreen
import com.securox.antitheft.presentation.screens.DeviceAdminSetupScreen
import com.securox.antitheft.presentation.screens.DeviceDetailsScreen
import com.securox.antitheft.presentation.screens.DeviceOwnerSetupScreen
import com.securox.antitheft.presentation.screens.EmergencyLockScreen
import com.securox.antitheft.presentation.screens.FactoryResetProtectionScreen
import com.securox.antitheft.presentation.screens.ForgotPasswordScreen
import com.securox.antitheft.presentation.screens.AdminControlDashboardScreen
import com.securox.antitheft.presentation.screens.HomeScreen
import com.securox.antitheft.presentation.screens.LocationTrackingScreen
import com.securox.antitheft.presentation.screens.LoginScreen
import com.securox.antitheft.presentation.screens.MyDevicesScreen
import com.securox.antitheft.presentation.screens.OnboardingScreen
import com.securox.antitheft.presentation.screens.PrivacyPolicyScreen
import com.securox.antitheft.presentation.screens.RegisterDeviceScreen
import com.securox.antitheft.presentation.screens.RegisterScreen
import com.securox.antitheft.presentation.screens.SecurityAlertsScreen
import com.securox.antitheft.presentation.screens.SecurityCenterScreen
import com.securox.antitheft.presentation.screens.SimSecurityScreen
import com.securox.antitheft.presentation.screens.SplashScreen
import com.securox.antitheft.presentation.screens.TrustedDevicesScreen
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun SecuroxNavGraph(
    viewModel: SecuroxViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Screen.AdminControlDashboard.route, "Admin", Icons.Default.AdminPanelSettings),
        BottomNavItem(Screen.MyDevices.route, "Devices", Icons.Default.Smartphone),
        BottomNavItem(Screen.SecurityCenter.route, "Security", Icons.Default.Security),
        BottomNavItem(Screen.SecurityAlerts.route, "Alerts", Icons.Default.Notifications),
        BottomNavItem(Screen.AccountSettings.route, "Settings", Icons.Default.Person)
    )

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SecuroxSurface,
                    contentColor = SecuroxTextSecondary
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SecuroxNeonPrimary,
                                selectedTextColor = SecuroxNeonPrimary,
                                unselectedIconColor = SecuroxTextSecondary,
                                unselectedTextColor = SecuroxTextSecondary,
                                indicatorColor = Color(0x2239FF14)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SecuroxBg)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                // 1. Splash Screen
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onSplashFinished = {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 2. Onboarding Screen
                composable(Screen.Onboarding.route) {
                    OnboardingScreen(
                        onFinished = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        },
                        onLoginClick = {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }

                // 3. Login Screen
                composable(Screen.Login.route) {
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        },
                        onNavigateToForgotPassword = {
                            navController.navigate(Screen.ForgotPassword.route)
                        }
                    )
                }

                // 4. Register Screen
                composable(Screen.Register.route) {
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }

                // 5. Forgot Password Screen
                composable(Screen.ForgotPassword.route) {
                    ForgotPasswordScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 6. Home Dashboard Screen
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToEmergencyLock = { navController.navigate(Screen.EmergencyLock.route) },
                        onNavigateToLocation = { navController.navigate(Screen.LocationTracking.route) },
                        onNavigateToSecurityCenter = { navController.navigate(Screen.SecurityCenter.route) },
                        onNavigateToDevices = { navController.navigate(Screen.MyDevices.route) },
                        onNavigateToAntiShutdown = { navController.navigate(Screen.AntiShutdown.route) },
                        onNavigateToDeviceOwner = { navController.navigate(Screen.DeviceOwnerSetup.route) },
                        onNavigateToSimSecurity = { navController.navigate(Screen.SimSecurity.route) },
                        onNavigateToAlerts = { navController.navigate(Screen.SecurityAlerts.route) },
                        onNavigateToSettings = { navController.navigate(Screen.AccountSettings.route) },
                        onNavigateToDeviceDetails = { navController.navigate(Screen.DeviceDetails.route) },
                        onNavigateToAdminDashboard = { navController.navigate(Screen.AdminControlDashboard.route) }
                    )
                }

                // Admin Control Dashboard Screen
                composable(Screen.AdminControlDashboard.route) {
                    AdminControlDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToEmergencyLock = { navController.navigate(Screen.EmergencyLock.route) },
                        onNavigateToLocationTracking = { navController.navigate(Screen.LocationTracking.route) },
                        onBackClick = { navController.navigate(Screen.Home.route) }
                    )
                }

                // 7. Register Device Screen
                composable(Screen.RegisterDevice.route) {
                    RegisterDeviceScreen(
                        viewModel = viewModel,
                        onSuccess = { navController.popBackStack() },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 8. My Devices Screen
                composable(Screen.MyDevices.route) {
                    MyDevicesScreen(
                        viewModel = viewModel,
                        onNavigateToRegisterDevice = { navController.navigate(Screen.RegisterDevice.route) },
                        onNavigateToDetails = { navController.navigate(Screen.DeviceDetails.route) },
                        onBackClick = { navController.navigate(Screen.Home.route) }
                    )
                }

                // 9. Device Details Screen
                composable(Screen.DeviceDetails.route) {
                    DeviceDetailsScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToLock = { navController.navigate(Screen.EmergencyLock.route) },
                        onNavigateToLocation = { navController.navigate(Screen.LocationTracking.route) }
                    )
                }

                // 10. Emergency Lock Screen
                composable(Screen.EmergencyLock.route) {
                    EmergencyLockScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 11. Location Tracking Screen
                composable(Screen.LocationTracking.route) {
                    LocationTrackingScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 12. Security Center Screen
                composable(Screen.SecurityCenter.route) {
                    SecurityCenterScreen(
                        viewModel = viewModel,
                        onNavigateToDeviceAdmin = { navController.navigate(Screen.DeviceAdminSetup.route) },
                        onNavigateToDeviceOwner = { navController.navigate(Screen.DeviceOwnerSetup.route) },
                        onNavigateToAntiShutdown = { navController.navigate(Screen.AntiShutdown.route) },
                        onNavigateToFactoryReset = { navController.navigate(Screen.FactoryResetProtection.route) },
                        onNavigateToSimSecurity = { navController.navigate(Screen.SimSecurity.route) },
                        onBackClick = { navController.navigate(Screen.Home.route) }
                    )
                }

                // 13. Device Administrator Setup Screen
                composable(Screen.DeviceAdminSetup.route) {
                    DeviceAdminSetupScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 14. Device Owner Setup Screen
                composable(Screen.DeviceOwnerSetup.route) {
                    DeviceOwnerSetupScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 15. Anti-Shutdown Protection Screen
                composable(Screen.AntiShutdown.route) {
                    AntiShutdownScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 16. Factory Reset Protection Screen
                composable(Screen.FactoryResetProtection.route) {
                    FactoryResetProtectionScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 17. SIM Security Screen
                composable(Screen.SimSecurity.route) {
                    SimSecurityScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 18. Security Alerts Screen
                composable(Screen.SecurityAlerts.route) {
                    SecurityAlertsScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.navigate(Screen.Home.route) }
                    )
                }

                // 19. Command History Screen
                composable(Screen.CommandHistory.route) {
                    CommandHistoryScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 20. Trusted Devices Screen
                composable(Screen.TrustedDevices.route) {
                    TrustedDevicesScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 21. Account Settings Screen
                composable(Screen.AccountSettings.route) {
                    AccountSettingsScreen(
                        viewModel = viewModel,
                        onNavigateToPrivacyPolicy = { navController.navigate(Screen.PrivacyPolicy.route) },
                        onNavigateToAbout = { navController.navigate(Screen.About.route) },
                        onSignOut = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        },
                        onBackClick = { navController.navigate(Screen.Home.route) }
                    )
                }

                // 22. Privacy Policy Screen
                composable(Screen.PrivacyPolicy.route) {
                    PrivacyPolicyScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 23. About SECUROX Screen
                composable(Screen.About.route) {
                    AboutScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
