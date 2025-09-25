package com.meyrforge.bagofholdingdmsvault.feature_create_item.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.firebase.StorageUploader

@HiltViewModel
class CreateItemViewModel @Inject constructor(private val storageUploader: StorageUploader): ViewModel(){
    private val _uploadedImageUrl = mutableStateOf<String?>(null)
    val uploadedImageUrl: State<String?> = _uploadedImageUrl

    private val _isUploading = mutableStateOf(false)
    val isUploading: State<Boolean> = _isUploading

    var selectedImageUri by mutableStateOf<Uri?>(null)


    fun uploadItemImage() {
        selectedImageUri?.let { uri ->
            viewModelScope.launch {
                _isUploading.value = true
                val url = storageUploader.uploadImageToFirebase(uri)
                _isUploading.value = false
            }
        }
    }
}