package com.meyrforge.bagofholdingdmsvault.feature_home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.feature_home.presentation.components.CategoryCard
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.ButtonItemComponent
import kotlinx.coroutines.flow.collectLatest

data class CategoryHome(
    val category: Category,
    val name: String,
    val count: Int,
    val imageRes: Int
)

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val categories = mutableListOf<CategoryHome>()

    val groupedItems by viewModel.groupedItems

    groupedItems.forEach { (category, itemsInCategory) ->
        val categoryImage = getIconByCategory(category)
        val categoryName = getNameByCategory(category)
        val newCategoryHome = CategoryHome(category, categoryName,itemsInCategory.size, categoryImage)
        categories.add(newCategoryHome)
    }

    for(category in Category.entries){
        if(!groupedItems.containsKey(category)){
            val categoryImage = getIconByCategory(category)
            val categoryName = getNameByCategory(category)
            val newCategoryHome = CategoryHome(category, categoryName, 0, categoryImage)
            categories.add(newCategoryHome)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeEvent.Navigate -> {
                    navController.navigate(event.route) {
                        // Limpiar el backstack para que el usuario no pueda volver a Home
                        // presionando el botón "atrás" después de cerrar sesión.
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        // Asegúrate de que solo haya una instancia de LoginScreen en la pila
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            )
            {
                Image(
                    painterResource(R.drawable.boh_login_banner),
                    "banner",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.BottomCenter

                )
            }
        }
        items(categories) { category ->
            CategoryCard(
                title = category.name,
                itemCount = category.count,
                imageRes = category.imageRes,
                onClick = { }
            )
        }
        item {
            ButtonItemComponent(
                text = "Cerrar Sesión",
                onClick = {
                    viewModel.signOutUser()
                })
        }
        item {
            ButtonItemComponent("Agregar objeto") { navController.navigate(Screen.AddItem.route) }
        }

    }
}
fun getIconByCategory(category: Category): Int {
    return when (category) {
        Category.MINI -> R.drawable.ic_minis
        Category.DADO -> R.drawable.ic_dices
        Category.MAPA -> R.drawable.ic_maps
        Category.LIBRO -> R.drawable.ic_books
        Category.PROP -> R.drawable.ic_props
        Category.OTRO -> R.drawable.ic_other
    }
}

fun getNameByCategory(category: Category): String {
    return when (category) {
        Category.MINI -> "Minis"
        Category.DADO -> "Dados"
        Category.MAPA -> "Mapas"
        Category.LIBRO -> "Libros"
        Category.PROP -> "Props"
        Category.OTRO -> "Otros"
    }
}