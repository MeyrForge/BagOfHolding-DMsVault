package com.meyrforge.bagofholdingdmsvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddModerator
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.twotone.AddModerator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.feature_create_item.presentation.CreateItemScreen
import com.meyrforge.bagofholdingdmsvault.feature_home.presentation.HomeScreen
import com.meyrforge.bagofholdingdmsvault.feature_login.presentation.LoginScreen
import com.meyrforge.bagofholdingdmsvault.feature_login.presentation.RegisterScreen
import com.meyrforge.bagofholdingdmsvault.feature_login.presentation.SentEmailScreen
import com.meyrforge.bagofholdingdmsvault.feature_settings.presentation.SettingsScreen
import com.meyrforge.bagofholdingdmsvault.ui.theme.BagOfHoldingDMsVaultTheme
import com.meyrforge.bagofholdingdmsvault.ui.theme.BannerBackground
import com.meyrforge.bagofholdingdmsvault.ui.theme.BottomNavBar
import com.meyrforge.bagofholdingdmsvault.ui.theme.BurgundyRed
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold
import com.meyrforge.bagofholdingdmsvault.ui.theme.Parchment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        val startDestination = determineStartDestination()
        setContent {
            BagOfHoldingDMsVaultTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val screensWithBottomBar = listOf(
                    Screen.Home.route,
                    Screen.AddItem.route,
                    Screen.Settings.route
                )
                Scaffold(
                    bottomBar = {
                        if (currentRoute in screensWithBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepDarkBrown
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(route = Screen.Login.route) {
                                LoginScreen(navController = navController)
                            }
                            composable(route = Screen.Register.route) {
                                RegisterScreen(navController = navController)
                            }
                            composable(route = Screen.SentEmail.route) {
                                SentEmailScreen(navController = navController)
                            }
                            composable(route = Screen.Home.route) {
                                HomeScreen(navController = navController)
                            }
                            composable(route = Screen.AddItem.route) {
                                CreateItemScreen()
                            }
                            composable(route = Screen.Settings.route) {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun determineStartDestination(): String {
        val currentUser = auth.currentUser
        return if (currentUser != null && currentUser.isEmailVerified) {
            Screen.Home.route
        } else {
            if (currentUser != null && !currentUser.isEmailVerified) {
                auth.signOut() // Opcional: Desloguear si no está verificado para forzar el login
            }
            Screen.Login.route
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        containerColor = BottomNavBar
    ) {
        val createItem = Screen.AddItem
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AddModerator, "Crear Item") },
            selected = currentDestination?.hierarchy?.any { it.route == createItem.route } == true,
            label = { Text("Crear Item",
                fontFamily = FontFamily(Font(R.font.caudex_regular))) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Parchment,
                unselectedTextColor = Parchment,
                selectedIconColor = Gold,
                indicatorColor = Corner,
                selectedTextColor = Gold
            ),
            onClick = {
                navController.navigate(createItem.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })

        val home = Screen.Home
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, "Home") },
            selected = currentDestination?.hierarchy?.any { it.route == home.route } == true,
            label = { Text("Home",
                fontFamily = FontFamily(Font(R.font.caudex_regular))) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Parchment,
                unselectedTextColor = Parchment,
                selectedIconColor = Gold,
                indicatorColor = Corner,
                selectedTextColor = Gold
            ),
            onClick = {
                navController.navigate(home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })

        val settings = Screen.Settings
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Settings, "Ajustes") },
            selected = currentDestination?.hierarchy?.any { it.route == settings.route } == true,
            label = { Text("Ajustes",
                fontFamily = FontFamily(Font(R.font.caudex_regular))) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Parchment,
                unselectedTextColor = Parchment,
                selectedIconColor = Gold,
                indicatorColor = Corner,
                selectedTextColor = Gold
            ),
            onClick = {
                navController.navigate(settings.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
    }
}