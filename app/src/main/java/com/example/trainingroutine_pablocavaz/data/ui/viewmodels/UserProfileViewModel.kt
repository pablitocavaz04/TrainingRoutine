package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import android.net.Uri
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

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _personaId = MutableStateFlow<Int?>(null)
    val personaId: StateFlow<Int?> = _personaId.asStateFlow()

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun setPersonaId(id: Int) {
        _personaId.value = id
    }

    fun uploadImageToStrapi(token: String, imageFile: File, personaId: Int) {
        viewModelScope.launch {
            try {
                val body = MultipartBody.Part.createFormData(
                    "files", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )

                RetrofitInstance.api.uploadImage("Bearer $token", body)
                    .firstOrNull()?.let { uploadedImage ->
                        RetrofitInstance.api.updatePersonaProfileImage(
                            "Bearer $token", personaId,
                            UpdatePersonaRequest(UpdatePersonaData(uploadedImage.id))
                        )
                    }
            } catch (_: Exception) { }
        }
    }
}
