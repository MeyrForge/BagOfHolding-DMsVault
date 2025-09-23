package com.meyrforge.bagofholdingdmsvault.feature_home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.feature_home.presentation.Category
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.GreyishBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Parchment

@Composable
fun CategoryCard(
    title: String,
    itemCount: Int,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Parchment// tono pergamino
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.textura), // Reemplaza con tu recurso de textura
                contentDescription = "texture", // Descripción para accesibilidad
                modifier = Modifier.fillMaxSize(), // La textura ocupa todo el espacio del Box
                contentScale = ContentScale.FillBounds // O ContentScale.FillBounds, según el efecto deseado
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, DeepDarkBrown, RoundedCornerShape(2.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .size(110.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.caudex_regular)),
                    color = DarkBrown // marrón oscuro
                )
                Text(
                    text = "$itemCount items",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.lato_regular)),
                    color = GreyishBrown
                )
            }
            Image(
                painter = painterResource(id = R.drawable.corner),
                contentDescription = "Esquinero superior izquierdo",
                colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
                modifier = Modifier
                    .align(Alignment.TopStart) // Alinea en la esquina superior izquierda
                    .size(40.dp)
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
                    .size(40.dp)
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
                    .size(40.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.corner),
                contentDescription = "Esquinero inferior derecho",
                colorFilter = ColorFilter.tint(Corner.copy(alpha = 0.9f)),
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Alinea en la esquina inferior derecha
                    .size(40.dp)
                    .graphicsLayer {
                        rotationZ = -90f
                    }
            )
        }
    }
}

@Composable
fun CategoryGrid(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                title = category.name,
                itemCount = category.count,
                imageRes = category.imageRes,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF362723)
@Composable
fun CategoryGridPreview() {
    val categories = listOf(
        Category("Minis", 67, R.drawable.ic_minis),
        Category("Dados", 24, R.drawable.ic_dices),
        Category("Mapas", 10, R.drawable.ic_maps),
        Category("Libros", 15, R.drawable.ic_books),
        Category("Props", 8, R.drawable.ic_props),
        Category("Otros", 3, R.drawable.ic_other)
    )
    CategoryGrid(categories) { /* Handle click */ }
}
