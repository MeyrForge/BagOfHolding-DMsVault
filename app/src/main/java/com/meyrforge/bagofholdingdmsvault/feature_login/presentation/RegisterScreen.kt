package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.InputTextFieldComponent
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.TopBarComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {

    val registerName by viewModel.registerName
    val registerEmail by viewModel.registerEmail
    val registerPassword by viewModel.registerPassword
    val registerPasswordRepeat by viewModel.registerPasswordRepeat
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginEvent.Navigate -> {
                    navController.navigate(event.route) {
                        // Opcional: Limpiar el backstack para que el usuario no vuelva a la pantalla de login
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        } // Asumiendo que tienes Screen.Login
                        launchSingleTop = true
                    }
                }

                is LoginEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                LoginEvent.LoginSuccess -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                LoginEvent.RegistrationSuccessAndVerificationSent -> {
                    navController.navigate(Screen.SentEmail.route)
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DeepDarkBrown,
        topBar = {TopBarComponent()}
        ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DeepDarkBrown)
                    .padding(16.dp)
            ) {
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Nombre",
                            text = registerName
                        ) { viewModel.onRegisterNameChange(it) }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Email",
                            text = registerEmail,
                            type = KeyboardType.Email
                        ) { viewModel.onRegisterEmailChange(it) }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Contraseña",
                            text = registerPassword,
                            type = KeyboardType.Password
                        ) { viewModel.onRegisterPasswordChange(it) }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Repetir contraseña",
                            text = registerPasswordRepeat,
                            type = KeyboardType.Password
                        ) { viewModel.onRegisterPasswordRepeatChange(it) }
                    }
                }
                item {
                    ButtonItemComponent("Registrarme") { viewModel.registerUser() }
                }
            }
        }
    }
}

