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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.twotone.AddBox
import androidx.compose.material.icons.twotone.AddModerator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.feature_create_item.presentation.CreateItemScreen
import com.meyrforge.bagofholdingdmsvault.feature_home.presentation.HomeScreen
import com.meyrforge.bagofholdingdmsvault.ui.theme.BagOfHoldingDMsVaultTheme
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Parchment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BagOfHoldingDMsVaultTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepDarkBrown,
                    floatingActionButton = {FABAddItem(navController)}) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route
                        ) {
                            composable(route = Screen.Home.route) {
                                HomeScreen()
                            }
                            composable(route = Screen.AddItem.route){
                                CreateItemScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FABAddItem(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = {navController.navigate(Screen.AddItem.route)},
        shape = RoundedCornerShape(0.dp, 20.dp, 0.dp, 20.dp),
        icon = { Icon(Icons.TwoTone.AddModerator, "Agregar") },
        text = { Text("Agregar objeto", fontFamily = FontFamily(Font(R.font.caudex_regular)), fontWeight = FontWeight.Bold) },
        containerColor = Copper,
        contentColor = DarkBrown
    )
}