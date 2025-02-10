package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.models.UpdatePersonaData
import com.example.trainingroutine_pablocavaz.data.remote.models.UpdatePersonaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserProfileViewModel : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null) // Mantener Uri
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _uploadSuccess = MutableStateFlow<Boolean?>(null)
    val uploadSuccess: StateFlow<Boolean?> = _uploadSuccess.asStateFlow()

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    private val _personaId = MutableStateFlow<Int?>(null)
    val personaId: StateFlow<Int?> = _personaId.asStateFlow()

    fun setPersonaId(id: Int) {
        _personaId.value = id
    }

    fun uploadImageToStrapi(token: String, imageFile: File, personaId: Int) {
        viewModelScope.launch {
            try {
                Log.d("UserProfileViewModel", "Iniciando subida de imagen: ${imageFile.path}")

                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

                val response = RetrofitInstance.api.uploadProfileImage("Bearer $token", body)

                if (response.isNotEmpty()) {
                    val uploadedImageId = response[0].id
                    Log.d("UserProfileViewModel", "Imagen subida correctamente: ${response[0].url}")

                    // ✅ Usamos la nueva data class para la actualización
                    val updateRequest = UpdatePersonaRequest(UpdatePersonaData(uploadedImageId))

                    val updateResponse = RetrofitInstance.api.updatePersonaProfileImage(
                        "Bearer $token", personaId, updateRequest
                    )

                    if (updateResponse) {
                        _uploadSuccess.value = true
                        Log.d("UserProfileViewModel", "Imagen asociada al perfil con éxito.")
                    } else {
                        _uploadSuccess.value = false
                        Log.e("UserProfileViewModel", "Error al asociar la imagen al perfil.")
                    }

                } else {
                    _uploadSuccess.value = false
                    Log.e("UserProfileViewModel", "Error en la subida: Respuesta vacía")
                }

            } catch (e: Exception) {
                _uploadSuccess.value = false
                Log.e("UserProfileViewModel", "Error al subir imagen: ${e.message}")
            }
        }
    }

}
