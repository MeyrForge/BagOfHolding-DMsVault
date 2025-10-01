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
import com.google.firebase.auth.FirebaseAuth
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.firebase.StorageUploader
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.usecases.CreateItemUseCase

@HiltViewModel
class CreateItemViewModel @Inject constructor(
    private val storageUploader: StorageUploader,
    private val createItemUseCase: CreateItemUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

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

    private val _isSaving = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _message = mutableStateOf("")
    val message: State<String> = _message

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
        val currentUserId = firebaseAuth.currentUser?.uid // Obtén el UID aquí
        if (currentUserId == null) {
            _uploadError.value = "Error: Usuario no autenticado."
            // Podrías también emitir un evento para redirigir al login
            return
        }

        selectedImageUri?.let { uri ->
            viewModelScope.launch {
                _isUploading.value = true
                _uploadError.value = null
                _uploadedImageUrl.value = null

                val url = storageUploader.uploadImageToFirebase(uri, currentUserId, _itemCategory.value)

                if (url != null) {
                    _uploadedImageUrl.value = url
                    createItem(currentUserId)
                } else {
                    _uploadError.value = "Error al guardar la imagen. Inténtalo de nuevo."
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

    fun onItemNameChange(name: String) {
        _itemName.value = name
    }

    fun onItemDescriptionChange(description: String) {
        _itemDescription.value = description
    }

    fun onItemCategoryChange(category: Category) {
        _itemCategory.value = category
    }

    fun clearMessage() {
        _message.value = ""
    }

    private fun clearInputs() {
        _itemName.value = ""
        _itemDescription.value = ""
        clearUploadedImageInfo()
        selectedImageUri = null
    }

    private fun createItem(currentUserId: String) {
        if (_itemName.value.isNotBlank() && _itemDescription.value.isNotBlank()) {
            _isSaving.value = true
            val name = _itemName.value
            val description = _itemDescription.value
            val category = _itemCategory.value
            val imgUrl = _uploadedImageUrl.value
            if (imgUrl != null) {
                val item = Item(null, currentUserId,name, description, category, imgUrl)
                viewModelScope.launch {
                    val success = createItemUseCase(item)
                    if (success) {
                        _message.value = "Item creado exitosamente"
                        clearInputs()
                    } else {
                        _message.value = "Error al crear el item"
                    }
                    _isSaving.value = false
                }
            }
        }
    }
}