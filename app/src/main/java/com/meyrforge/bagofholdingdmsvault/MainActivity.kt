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
import androidx.compose.material.icons.twotone.AddModerator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
import com.meyrforge.bagofholdingdmsvault.ui.theme.BagOfHoldingDMsVaultTheme
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepDarkBrown,
                    floatingActionButton = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                        if (currentRoute == Screen.Home.route && auth.currentUser != null && auth.currentUser!!.isEmailVerified) {
                            FABAddItem(navController)
                        }
                    }) { innerPadding ->
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
                                HomeScreen()
                            }
                            composable(route = Screen.AddItem.route) {
                                CreateItemScreen()
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
fun FABAddItem(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate(Screen.AddItem.route) },
        shape = RoundedCornerShape(0.dp, 20.dp, 0.dp, 20.dp),
        icon = { Icon(Icons.TwoTone.AddModerator, "Agregar") },
        text = {
            Text(
                "Agregar objeto",
                fontFamily = FontFamily(Font(R.font.caudex_regular)),
                fontWeight = FontWeight.Bold
            )
        },
        containerColor = Copper,
        contentColor = DarkBrown
    )
}