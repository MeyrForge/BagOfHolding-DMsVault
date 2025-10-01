package com.meyrforge.bagofholdingdmsvault.feature_home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import com.meyrforge.bagofholdingdmsvault.feature_home.domain.usecases.GetAllItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.meyrforge.bagofholdingdmsvault.common.Category
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

sealed class HomeEvent {
    data class Navigate(val route: String) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getAllItemsUseCase: GetAllItemsUseCase
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _groupedItems = mutableStateOf<Map<Category, List<Item>>>(emptyMap())
    val groupedItems: State<Map<Category, List<Item>>> = _groupedItems


    init {
        getAllItems()
    }

    fun signOutUser() {
        viewModelScope.launch {
            auth.signOut()
            // Después de cerrar sesión, navega a la pantalla de Login
            _eventFlow.emit(HomeEvent.Navigate(Screen.Login.route))
        }
    }

    // Opcional: Obtener el nombre del usuario para mostrarlo en HomeScreen
    fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName
    }

    private fun getAllItems(){
        viewModelScope.launch {
            val allItems = getAllItemsUseCase()
            _groupedItems.value = allItems.groupBy { it.category }
        }
    }
}