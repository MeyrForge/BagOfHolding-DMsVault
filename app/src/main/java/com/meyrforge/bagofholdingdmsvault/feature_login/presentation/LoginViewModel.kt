package com.meyrforge.bagofholdingdmsvault.feature_login.presentation

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
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
    object LoginSuccess : LoginEvent()
    object RegistrationSuccessAndVerificationSent : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _registerName = mutableStateOf("")
    val registerName: State<String> = _registerName

    private val _registerEmail = mutableStateOf("")
    val registerEmail: State<String> = _registerEmail

    private val _registerPassword = mutableStateOf("")
    val registerPassword: State<String> = _registerPassword

    private val _registerPasswordRepeat = mutableStateOf("")
    val registerPasswordRepeat: State<String> = _registerPasswordRepeat

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onRegisterNameChange(value: String) {
        _registerName.value = value
    }

    fun onRegisterEmailChange(value: String) {
        _registerEmail.value = value
    }

    fun onRegisterPasswordChange(value: String) {
        _registerPassword.value = value
    }

    fun onRegisterPasswordRepeatChange(value: String) {
        _registerPasswordRepeat.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
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
                val authResult = auth.signInWithEmailAndPassword(currentEmail, currentPassword).await()
                val user = authResult.user

                if (user != null) {
                    if (user.isEmailVerified) {
                        _eventFlow.emit(LoginEvent.LoginSuccess)
                    } else {
                        user.sendEmailVerification().await()
                        _eventFlow.emit(LoginEvent.ShowError("Por favor, verifica tu correo. Se ha reenviado un enlace."))
                        auth.signOut()
                    }
                } else {
                    _eventFlow.emit(LoginEvent.ShowError("Error al iniciar sesión."))
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.i("LoginViewModel", "Error de autenticación: ${e.localizedMessage}")
                _eventFlow.emit(LoginEvent.ShowError("Error de autenticación"))
            }
        }
    }

    fun registerUser() {
        val currentName = registerName.value.trim()
        val currentEmail = registerEmail.value.trim()
        val currentPassword = registerPassword.value.trim()
        val currentPasswordRepeat = registerPasswordRepeat.value.trim()

        if (currentName.isBlank()) {
            viewModelScope.launch { _eventFlow.emit(LoginEvent.ShowError("El nombre no puede estar vacío.")) }
            return
        }
        if (currentEmail.isBlank()) {
            viewModelScope.launch { _eventFlow.emit(LoginEvent.ShowError("El email no puede estar vacío.")) }
            return
        }
        if (currentPassword != currentPasswordRepeat) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowError("Las contraseñas no coinciden."))
            }
            return
        } else
            if (currentEmail.isBlank() || currentPassword.isBlank()) {
                viewModelScope.launch {
                    _eventFlow.emit(LoginEvent.ShowError("Email y contraseña no pueden estar vacíos para registrarse."))
                }
                return
            } else
                if (currentPassword.length < 6 || !currentPassword.any { it.isDigit() } || !currentPassword.any { it.isLetter() }) {
                    viewModelScope.launch {
                        _eventFlow.emit(LoginEvent.ShowError("La contraseña debe tener al menos 6 caracteres, incluyendo letras y números."))
                    }
                    return
                }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(currentEmail, currentPassword).await()
                val user = authResult.user

                if (user != null) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = currentName
                    }
                    user.updateProfile(profileUpdates).await()

                    user.sendEmailVerification().await()
                    _eventFlow.emit(LoginEvent.RegistrationSuccessAndVerificationSent)
                } else {
                    _eventFlow.emit(LoginEvent.ShowError("Error al registrar usuario."))
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.i("LoginViewModel", "Error de registro: ${e.localizedMessage}")
                _eventFlow.emit(LoginEvent.ShowError("Error en el registro"))
            }
        }
    }

    fun resendVerificationEmail() {
        val user = auth.currentUser
        if (user != null && !user.isEmailVerified) {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    user.sendEmailVerification().await()
                    _eventFlow.emit(LoginEvent.ShowError("Se ha reenviado el correo de verificación."))
                    _isLoading.value = false
                } catch (e: Exception) {
                    _isLoading.value = false
                    Log.i("LoginViewModel", "Error al reenviar el correo: ${e.localizedMessage}")
                    _eventFlow.emit(LoginEvent.ShowError("Error al reenviar el correo."))
                }
            }
        } else if (user?.isEmailVerified == true) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowError("Tu correo ya está verificado."))
            }
        } else {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowError("No hay usuario logueado o el correo ya fue verificado."))
            }
        }
    }


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