package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.InputTextFieldComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val email by viewModel.email
    val password by viewModel.password

    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginEvent.Navigate -> {
                    navController.navigate(event.route) {
                        // Opcional: Limpiar el backstack para que el usuario no vuelva a la pantalla de login
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        } // Asumiendo que tienes Screen.Login
                        launchSingleTop = true
                    }
                }

                is LoginEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                LoginEvent.LoginSuccess -> {
                    // Navegar a la pantalla principal después de un inicio de sesión exitoso
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepDarkBrown),
        contentAlignment = Alignment.Center // Para centrar el CircularProgressIndicator
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepDarkBrown)
                .padding(16.dp, 0.dp)
        ) {
            item {
                Box(modifier = Modifier.padding(bottom = 16.dp)) {
                    Image(
                        painterResource(R.drawable.boh_login_banner),
                        "banner"
                    )
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = 10.dp))
                {
                    InputTextFieldComponent(
                        "Email",
                        text = email,
                        type = KeyboardType.Email
                    ) { viewModel.onEmailChange(it) }
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = 10.dp))
                {
                    InputTextFieldComponent(
                        "Contraseña",
                        text = password,
                        type = KeyboardType.Password
                    ) { viewModel.onPasswordChange(it) }
                }
            }
            item {
                ButtonItemComponent("Iniciar sesión") { navController.navigate(route = Screen.Home.route) }
            }
            item {
                TextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading // Deshabilitar botón mientras carga
                ) {
                    Text(
                        "No tengo cuenta, quiero registrarme",
                        fontFamily = FontFamily(Font(R.font.caudex_regular)),
                        color = Gold,
                        fontSize = 16.sp
                    )
                }
            }
            // Podrías añadir un item para el logo de "Cargando" si isLoading es true
            if (isLoading) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = Gold)
                }
            }
        }
    }
}