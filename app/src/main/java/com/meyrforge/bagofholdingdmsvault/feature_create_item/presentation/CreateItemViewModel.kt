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
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.firebase.StorageUploader

@HiltViewModel
class CreateItemViewModel @Inject constructor(private val storageUploader: StorageUploader): ViewModel(){

    private val _itemName = mutableStateOf("")
    val itemName: State<String> = _itemName

    private val _itemDescription = mutableStateOf("")
    val itemDescription: State<String> = _itemDescription

    private val _itemCategory = mutableStateOf(Category.MINI)
    val itemCategory: State<Category> = _itemCategory

    private val _uploadedImageUrl = mutableStateOf<String?>(null)
    val uploadedImageUrl: State<String?> = _uploadedImageUrl

    private val _isUploading = mutableStateOf(false)
    val isUploading: State<Boolean> = _isUploading

    private val _uploadError = mutableStateOf<String?>(null)
    val uploadError: State<String?> = _uploadError


    var selectedImageUri by mutableStateOf<Uri?>(null)
        private set

    // Función para que la UI actualice la URI seleccionada
    fun updateSelectedImageUri(uri: Uri?) {
        selectedImageUri = uri
        if (uri != null) {
            clearUploadedImageInfo()
        }
    }

    fun uploadItemImage() {
        selectedImageUri?.let { uri ->
            viewModelScope.launch {
                _isUploading.value = true
                _uploadError.value = null
                _uploadedImageUrl.value = null

                val name = _itemName.value
                val description = _itemDescription.value

                val url = storageUploader.uploadImageToFirebase(uri, null, _itemCategory.value)

                if (url != null) {
                    _uploadedImageUrl.value = url
                } else {
                    _uploadError.value = "Error al subir la imagen. Inténtalo de nuevo."
                }
                _isUploading.value = false
            }
        } ?: run {
            _uploadError.value = "Por favor, selecciona una imagen primero."
        }
    }

    fun clearUploadedImageInfo() {
        _uploadedImageUrl.value = null
        _uploadError.value = null
    }

    fun onItemNameChange(name: String){
        _itemName.value = name
    }

    fun onItemDescriptionChange(description: String){
        _itemDescription.value = description
    }

    fun onItemCategoryChange(category: Category){
        _itemCategory.value = category
    }
}