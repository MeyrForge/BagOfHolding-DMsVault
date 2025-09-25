package com.meyrforge.bagofholdingdmsvault.common

sealed class Screen(val route: String){
    data object Home: Screen("home_screen")
    data object Minis: Screen("minis_screen")
    data object Dices: Screen("dices_screen")
    data object Maps: Screen("maps_screen")
    data object Books: Screen("books_screen")
    data object Props: Screen("props_screen")
    data object Other: Screen("other_screen")
    data object AddItem: Screen("add_item")
}
