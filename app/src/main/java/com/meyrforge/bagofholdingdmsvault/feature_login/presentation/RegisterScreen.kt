package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import com.meyrforge.bagofholdingdmsvault.FABAddItem
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.InputTextFieldComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown

@Preview
@Composable
fun RegisterScreen() {
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
                            text = ""
                        ) { }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Email",
                            text = "",
                            type = KeyboardType.Email
                        ) { }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Contraseña",
                            text = "",
                            type = KeyboardType.Password
                        ) { }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp))
                    {
                        InputTextFieldComponent(
                            "Repetir contraseña",
                            text = "",
                            type = KeyboardType.Password
                        ) { }
                    }
                }
                item {
                    ButtonItemComponent("Registrarme") { }
                }
            }
        }
    }
}

@Preview
@Composable
fun TopBarComponent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
            .height(140.dp)
            .background(Copper)
    ) {
        Text(
            "Registro",
            fontFamily = FontFamily(Font(R.font.caudex_regular)),
            fontSize = 50.sp,
            color = DarkBrown,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(R.drawable.textura), // Reemplaza con tu recurso de textura
            contentDescription = "texture", // Descripción para accesibilidad
            modifier = Modifier.fillMaxSize(), // La textura ocupa todo el espacio del Box
            contentScale = ContentScale.FillBounds // O ContentScale.FillBounds, según el efecto deseado
        )
        Image(
            painter = painterResource(id = R.drawable.corner),
            contentDescription = "Esquinero superior izquierdo",
            colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
            modifier = Modifier
                .align(Alignment.TopStart) // Alinea en la esquina superior izquierda
                .size(50.dp)
                .offset((-3).dp, (-3).dp)
                .graphicsLayer {
                    rotationZ = 90f // o 270f
                }// Ajusta el tamaño según sea necesario

        )
        Image(
            painter = painterResource(id = R.drawable.corner),
            contentDescription = "Esquinero superior derecho",
            colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
            modifier = Modifier
                .align(Alignment.TopEnd) // Alinea en la esquina superior derecha
                .size(50.dp)
                .offset(3.dp, (-3).dp)
                .graphicsLayer {
                    rotationZ = 180f
                }
        )
        Image(
            painter = painterResource(id = R.drawable.corner),
            contentDescription = "Esquinero inferior izquierdo",
            colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
            modifier = Modifier
                .align(Alignment.BottomStart) // Alinea en la esquina inferior izquierda
                .size(50.dp)
                .offset((-3).dp, 3.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.corner),
            contentDescription = "Esquinero inferior derecho",
            colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
            modifier = Modifier
                .align(Alignment.BottomEnd) // Alinea en la esquina inferior derecha
                .size(50.dp)
                .offset(3.dp, 3.dp)
                .graphicsLayer {
                    rotationZ = -90f
                }
        )
    }
}