package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SentEmailScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {

    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginEvent.Navigate -> {
                    navController.navigate(event.route) {
                        // Opcional: Limpiar el backstack para que el usuario no vuelva a la pantalla de login
                        popUpTo(Screen.SentEmail.route) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepDarkBrown)
            .padding(16.dp, 0.dp)
    ) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            Image(
                painterResource(R.drawable.boh_login_banner),
                "banner"
            )
        }
        Text(
            "Hemos enviado un correo de activación a su email para que pueda acceder a Bag of Holding | the DM's Vault",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Gold,
            fontFamily = FontFamily(Font(R.font.caudex_regular))
        )

        ButtonItemComponent("Volver a iniciar sesión") { navController.navigate(route = Screen.Login.route) }

        TextButton(
            onClick = {viewModel.resendVerificationEmail()},
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                "Reenviar el email",
                fontFamily = FontFamily(Font(R.font.caudex_regular)),
                color = Gold,
                fontSize = 16.sp
            )
        }
    }
}