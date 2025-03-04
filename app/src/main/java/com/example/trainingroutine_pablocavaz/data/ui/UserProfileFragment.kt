package com.example.trainingroutine_pablocavaz.data.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.UserProfileViewModel
import com.example.trainingroutine_pablocavaz.databinding.FragmentUserProfileBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserProfileViewModel by viewModels()

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = requireContext()
            .getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null)

        if (token != null) {
            loadUserProfile(token)
        } else {
            redirectToLogin()
        }

        binding.logoutButton.setOnClickListener { logout() }
        binding.floatingActionButtonCamera.setOnClickListener { showImagePickerDialog() }

        lifecycleScope.launch {
            viewModel.imageUri.collect { uri ->
                uri?.let { binding.imagenJugadorImageView2.setImageURI(it) }
            }
        }
    }

    private fun loadUserProfile(token: String) {
        lifecycleScope.launch {
            try {
                val userDetailsResponse = RetrofitInstance.api.getUserDetails("Bearer $token")
                val personaResponse = RetrofitInstance.api.getPersonasByUserId(
                    "Bearer $token", userDetailsResponse.id.toString()
                )

                val personaId = personaResponse.data.firstOrNull()?.id ?: -1
                viewModel.setPersonaId(personaId)

                val imageUrl = personaResponse.data.firstOrNull()?.attributes?.perfil?.data?.attributes?.url

                if (!imageUrl.isNullOrEmpty()) {
                    binding.imagenJugadorImageView2.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.bmba)
                    }
                } else {
                    binding.imagenJugadorImageView2.setImageResource(R.drawable.bmba)
                }


                binding.userName.text = userDetailsResponse.username
                binding.userEmail.text = userDetailsResponse.email
                binding.userRole.text = personaResponse.data.firstOrNull()?.attributes?.Rol ?: "Desconocido"

            } catch (_: Exception) { }
        }
    }

    private fun logout() {
        requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .edit().clear().apply()
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
        requireActivity().finishAffinity()
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Elige una opción")
            .setItems(arrayOf("📷 Cámara", "🖼 Galería")) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAPTURE_IMAGE_REQUEST)
        }
    }

    private fun openGallery() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> data?.data?.let { subirImagen(it) }
                CAPTURE_IMAGE_REQUEST -> (data?.extras?.get("data") as? Bitmap)?.let {
                    subirImagen(Uri.fromFile(saveBitmapToFile(requireContext(), it)))
                }
            }
        }
    }

    private fun subirImagen(uri: Uri) {
        requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null)?.let { token ->
                getFileFromUri(requireContext(), uri)?.let { file ->
                    lifecycleScope.launch {
                        viewModel.personaId.collect { personaId ->
                            if (personaId != null && personaId != -1) {
                                viewModel.uploadImageToStrapi(token, file, personaId)
                            }
                        }
                    }
                }
            }
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
        return File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg").apply {
            FileOutputStream(this).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return context.contentResolver.openInputStream(uri)?.let { inputStream ->
            File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg").apply {
                FileOutputStream(this).use { inputStream.copyTo(it) }
            }
        }
    }

    private fun redirectToLogin() {
        Toast.makeText(requireContext(), "Sesión expirada, inicia sesión nuevamente", Toast.LENGTH_SHORT).show()
    }
}
