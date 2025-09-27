package com.meyrforge.bagofholdingdmsvault.ui.sharedComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkWood
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold
import com.meyrforge.bagofholdingdmsvault.ui.theme.GreyishBrown

@Composable
fun InputTextFieldComponent(
    label: String,
    isDesc: Boolean = false,
    text: String,
    type: KeyboardType = KeyboardType.Text,
    onTextChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
        label = { Text(label, fontFamily = FontFamily(Font(R.font.caudex_regular))) },
        shape = RoundedCornerShape(5.dp),
        keyboardOptions = KeyboardOptions(keyboardType = type),
        modifier =
            if (isDesc) Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(4.dp, GreyishBrown, RoundedCornerShape(5.dp))
                .height(100.dp) else
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(2.dp, GreyishBrown, RoundedCornerShape(5.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Copper,
            unfocusedLabelColor = DarkBrown,
            focusedContainerColor = DarkWood,
            focusedLabelColor = DeepDarkBrown,
            unfocusedIndicatorColor = GreyishBrown,
            focusedIndicatorColor = GreyishBrown,
            focusedTextColor = Gold,
            unfocusedTextColor = DarkBrown
        ),
        visualTransformation = if ((type == KeyboardType.Password) && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if ((type == KeyboardType.Password)) {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description, tint = DarkBrown)
                }
            }
        }
    )
}