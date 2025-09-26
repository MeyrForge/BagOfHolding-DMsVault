package com.meyrforge.bagofholdingdmsvault.ui.sharedComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold

@Composable
fun ButtonItemComponent(text:String, onClick:()->Unit){
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Corner,
            contentColor = Gold
        )
    ) {
        Text(text, fontFamily = FontFamily(Font(R.font.caudex_regular)))
    }
}