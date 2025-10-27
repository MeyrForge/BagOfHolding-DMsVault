package com.meyrforge.bagofholdingdmsvault.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.meyrforge.bagofholdingdmsvault.common.Screen
import com.meyrforge.bagofholdingdmsvault.feature_home.presentation.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun signOutUser() {
        viewModelScope.launch {
            auth.signOut()
            _eventFlow.emit(HomeEvent.Navigate(Screen.Login.route))
        }
    }
}