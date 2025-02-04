package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserProfileViewModel : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _uploadSuccess = MutableStateFlow<Boolean?>(null)
    val uploadSuccess: StateFlow<Boolean?> = _uploadSuccess.asStateFlow()

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun uploadImageToStrapi(token: String, imageFile: File) {
        viewModelScope.launch {
            try {
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

                val response = RetrofitInstance.api.uploadProfileImage("Bearer $token", body)

                if (response.isNotEmpty()) {
                    _uploadSuccess.value = true
                    Log.d("UserProfileViewModel", "Imagen subida correctamente: ${response[0].url}")
                } else {
                    _uploadSuccess.value = false
                    Log.e("UserProfileViewModel", "Error en la subida: Respuesta vacía")
                }


            } catch (e: Exception) {
                _uploadSuccess.value = false
            }
        }
    }
}
