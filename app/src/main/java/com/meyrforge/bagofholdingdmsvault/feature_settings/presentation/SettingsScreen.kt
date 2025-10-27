package com.meyrforge.bagofholdingdmsvault.feature_settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.TopBarComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown

@Preview
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        TopBarComponent("Ajustes")
    },
        containerColor = DeepDarkBrown
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.bag_of_holding),
                "Banner",
                modifier = Modifier
                    .size(900.dp)
                    .alpha(0.3f)
            )
            ButtonItemComponent(
                text = "Cerrar Sesión",
                onClick = {
                    viewModel.signOutUser()
                })
        }
    }
}