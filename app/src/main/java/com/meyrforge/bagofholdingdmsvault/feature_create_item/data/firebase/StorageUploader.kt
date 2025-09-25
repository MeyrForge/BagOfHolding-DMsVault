package com.meyrforge.bagofholdingdmsvault.feature_create_item.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class StorageUploader @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) {

    private val baseStorageRef: StorageReference by lazy {
        firebaseStorage.reference.child("item_images")
    }

    suspend fun uploadImageToFirebase(imageUri: Uri, itemId: String? = null): String? {
        val fileName = itemId?.let { "${it}_${UUID.randomUUID()}.jpg" } ?: "${UUID.randomUUID()}.jpg"
        val imageFileRef = baseStorageRef.child(fileName)

        return try {
            Log.d("StorageUploader", "Iniciando subida para: ${imageUri.path} como $fileName")
            val uploadTask = imageFileRef.putFile(imageUri)

            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.d("FirebaseStorage", "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d("FirebaseStorage", "Upload is paused")
            }

            val result = uploadTask.await()

            if (result.metadata != null) {
                val downloadUrl = imageFileRef.downloadUrl.await()
                Log.i("FirebaseStorage", "Imagen subida exitosamente: $downloadUrl")
                downloadUrl.toString()
            } else {
                Log.e("FirebaseStorage", "Error al subir imagen: la metadata es nula después de la subida.")
                null
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error al subir imagen a Firebase: ${e.message}", e)
            null
        }
    }
}
