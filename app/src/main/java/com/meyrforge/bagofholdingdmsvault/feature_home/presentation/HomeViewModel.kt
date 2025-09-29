package com.meyrforge.bagofholdingdmsvault.feature_home.presentation

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.meyrforge.bagofholdingdmsvault.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeEvent {
    data class Navigate(val route: String) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
}