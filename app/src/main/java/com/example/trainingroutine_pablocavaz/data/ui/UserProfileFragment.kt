package com.example.trainingroutine_pablocavaz.data.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            loadUserProfile(token)
        } else {
            redirectToLogin()
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }

        binding.floatingActionButtonCamera.setOnClickListener {
            showImagePickerDialog()
        }

        // Observar cambios en la imagen seleccionada
        lifecycleScope.launch {
            viewModel.imageUri.collect { uri ->
                uri?.let {
                    binding.imagenJugadorImageView2.setImageURI(it)
                    val file = File(it.path!!)
                    token?.let { t -> viewModel.uploadImageToStrapi(t, file) }
                }
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

                val userName = userDetailsResponse.username
                val userEmail = userDetailsResponse.email
                val userRole = personaResponse.data.firstOrNull()?.attributes?.Rol ?: "Desconocido"
                val imageUrl = personaResponse.data.firstOrNull()
                    ?.attributes?.perfil?.data?.attributes?.formats?.small?.url

                if (imageUrl != null) {
                    binding.imagenJugadorImageView2.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.bmba)
                    }
                }

                binding.userName.text = userName
                binding.userEmail.text = userEmail
                binding.userRole.text = userRole

            } catch (e: Exception) {
                Log.e("UserProfileFragment", "Error al cargar perfil: ${e.message}")
            }
        }
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
        requireActivity().finishAffinity()
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("📷 Cámara", "🖼 Galería")

        AlertDialog.Builder(requireContext())
            .setTitle("Elige una opción")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permiso
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Si el permiso ya fue otorgado, abrir la cámara
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let { viewModel.setImageUri(it) }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abrir la cámara
                openCamera()
            } else {
                // Permiso denegado, mostrar mensaje al usuario
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun redirectToLogin() {
        Toast.makeText(requireContext(), "Sesión expirada, inicia sesión nuevamente", Toast.LENGTH_SHORT).show()
    }
}
