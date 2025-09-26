package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

import androidx.activity.result.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class LoginEvent {
    data class Navigate(val route: String) : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
    object LoginSuccess : LoginEvent() // Puedes usar esto para navegar o mostrar un mensaje
}

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Para comunicar eventos a la UI (como navegación o errores)
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEmailChange(value: String){
        _email.value = value
    }

    fun onPasswordChange(value: String){
        _password.value = value
    }

    fun loginUser() {
        val currentEmail = email.value.trim()
        val currentPassword = password.value.trim()

        if (currentEmail.isBlank() || currentPassword.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowError("Email y contraseña no pueden estar vacíos."))
            }
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(currentEmail, currentPassword).await()
                // Inicio de sesión exitoso
                _eventFlow.emit(LoginEvent.LoginSuccess) // O directamente LoginEvent.Navigate(Screen.Home.route)
                _isLoading.value = false
            } catch (e: Exception) {
                // Error en el inicio de sesión
                _isLoading.value = false
                _eventFlow.emit(LoginEvent.ShowError(e.localizedMessage ?: "Error de autenticación"))
            }
        }
    }

//    fun registerUser() {
//        val currentEmail = email.value.trim()
//        val currentPassword = password.value.trim()
//
//        if (currentEmail.isBlank() || currentPassword.isBlank()) {
//            viewModelScope.launch {
//                _eventFlow.emit(LoginEvent.ShowError("Email y contraseña no pueden estar vacíos para registrarse."))
//            }
//            return
//        }
//        // Podrías añadir más validaciones aquí (ej. longitud de la contraseña)
//
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                auth.createUserWithEmailAndPassword(currentEmail, currentPassword).await()
//                // Registro exitoso, podrías iniciar sesión automáticamente o pedir confirmación
//                _eventFlow.emit(LoginEvent.ShowError("¡Registro exitoso! Por favor, inicia sesión.")) // O navega a otra pantalla
//                _isLoading.value = false
//            } catch (e: Exception) {
//                _isLoading.value = false
//                _eventFlow.emit(LoginEvent.ShowError(e.localizedMessage ?: "Error en el registro"))
//            }
//        }
//    }

    // Opcional: Verificar si el usuario ya está logueado al iniciar el ViewModel
    init {
        if (auth.currentUser != null) {
             //Usuario ya logueado, podrías navegar directamente a Home
             viewModelScope.launch {
                 _eventFlow.emit(LoginEvent.Navigate(Screen.Home.route))
             }
        }
    }
}