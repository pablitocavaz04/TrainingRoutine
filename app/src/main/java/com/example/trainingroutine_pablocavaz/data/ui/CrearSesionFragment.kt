package com.example.trainingroutine_pablocavaz.data.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.BuildConfig
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.GoogleMapsRetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.models.CrearSesionRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.CrearSesionData
import com.example.trainingroutine_pablocavaz.databinding.FragmentCrearSesionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CrearSesionFragment : Fragment(R.layout.fragment_crear_sesion) {

    private var _binding: FragmentCrearSesionBinding? = null
    private val binding get() = _binding!!

    private val entrenamientoMap = mutableMapOf<String, Int>()
    private val jugadorMap = mutableMapOf<String, Int>()
    private val selectedJugadorIds = mutableListOf<Int>()
    private var entrenadorPersonaId: Int? = null

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private var imageUri: Uri? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCrearSesionBinding.bind(view)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE

        setupToolbar()
        loadEntrenadorId()
        loadEntrenamientos()
        loadJugadores()

        binding.btnGuardarSesion.setOnClickListener { guardarSesion() }
        binding.spinnerJugadores.setOnTouchListener { _, _ ->
            showJugadorSelectionDialog()
            true
        }

        binding.btnCamara.setOnClickListener { abrirCamara() }
        binding.btnGaleria.setOnClickListener { abrirGaleria() }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val tempUri = getImageUri(requireContext(), imageBitmap)
                    imageUri = tempUri
                    binding.imageViewSesion.setImageURI(tempUri)
                }
                GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    binding.imageViewSesion.setImageURI(imageUri)
                }
            }
        }
    }

    // Convierte el Bitmap en un Uri temporal
    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "TempImage", null)
        return Uri.parse(path)
    }


    private suspend fun subirImagenAStrapi(): Int? {
        if (imageUri == null) return null

        val token = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null) ?: return null

        val file = File(getRealPathFromUri(requireContext(), imageUri!!))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

        return try {
            val response = RetrofitInstance.api.uploadImage("Bearer $token", body)
            val imageId = response.firstOrNull()?.id // ✅ Devuelve el ID en lugar de la URL
            println("🔹 ID de la imagen subida: $imageId") // ✅ Verifica en Logcat
            imageId
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            null
        }
    }




    // Obtiene la ruta real del archivo desde un Uri
    private fun getRealPathFromUri(context: Context, uri: Uri): String {
        var result = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (idx >= 0) result = it.getString(idx)
            }
        }
        return result
    }


    private fun loadEntrenadorId() {
        val token = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null)
        val userId = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getInt("user_id", -1).toString()

        lifecycleScope.launch {
            RetrofitInstance.api.getPersonasByUserId("Bearer $token", userId).data
                .firstOrNull()?.let { entrenadorPersonaId = it.id }
        }
    }

    private fun loadEntrenamientos() {
        val token = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null)

        lifecycleScope.launch {
            RetrofitInstance.api.getEntrenamientos("Bearer $token").data.forEach {
                entrenamientoMap[it.attributes.nombre] = it.id
            }

            binding.spinnerEntrenamientos.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                entrenamientoMap.keys.toList()
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        }
    }

    private fun loadJugadores() {
        val token = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("token", null)

        lifecycleScope.launch {
            RetrofitInstance.api.getJugadores("Bearer $token").data.forEach {
                jugadorMap[it.attributes.user?.data?.attributes?.username ?: "Sin nombre"] = it.id
            }
        }
    }

    private fun showJugadorSelectionDialog() {
        val jugadorNames = jugadorMap.keys.toTypedArray()
        val selectedItems = BooleanArray(jugadorNames.size) { index ->
            selectedJugadorIds.contains(jugadorMap[jugadorNames[index]])
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona jugadores")
            .setMultiChoiceItems(jugadorNames, selectedItems) { _, which, isChecked ->
                jugadorMap[jugadorNames[which]]?.let {
                    if (isChecked) selectedJugadorIds.add(it) else selectedJugadorIds.remove(it)
                }
            }
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun guardarSesion() {
        val nombreSesion = binding.inputNombreSesion.text.toString()
        val estadoSesion = binding.switchEstadoSesion.isChecked
        val direccion = binding.editTextDireccion.text.toString()
        val entrenamientoId = entrenamientoMap[binding.spinnerEntrenamientos.selectedItem?.toString()]

        if (nombreSesion.isEmpty() || direccion.isEmpty() || entrenamientoId == null || selectedJugadorIds.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        obtenerCoordenadas(direccion) { latitud, longitud ->
            if (latitud != null && longitud != null) {
                lifecycleScope.launch {
                    val imagenId = subirImagenAStrapi() // ✅ Primero subimos la imagen y obtenemos su ID
                    enviarSesion(nombreSesion, estadoSesion, direccion, latitud, longitud, entrenamientoId, imagenId)
                }
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener las coordenadas.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerCoordenadas(direccion: String, onResult: (Double?, Double?) -> Unit) {
        lifecycleScope.launch {
            val apiKey = BuildConfig.MAPS_API_KEY
            val response = GoogleMapsRetrofitInstance.api.getGeocodingData(direccion, apiKey)

            response.results.firstOrNull()?.geometry?.location?.let {
                onResult(it.lat, it.lng)
            } ?: onResult(null, null)
        }
    }

    private fun enviarSesion(
        nombreSesion: String,
        estadoSesion: Boolean,
        direccion: String,
        latitud: Double,
        longitud: Double,
        entrenamientoId: Int,
        imagenId: Int? // ✅ Ahora pasamos el ID de la imagen
    ) {
        lifecycleScope.launch {
            val token = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getString("token", null)

            val request = CrearSesionRequest(
                data = CrearSesionData(
                    nombre = nombreSesion,
                    estado = estadoSesion,
                    direccion = direccion,
                    latitud = latitud,
                    longitud = longitud,
                    entrenamiento = entrenamientoId,
                    jugadores = selectedJugadorIds,
                    entrenador = entrenadorPersonaId ?: return@launch,
                    sesionpicture = imagenId // ✅ Enviamos directamente el ID
                )
            )

            println("📌 Enviando sesión con imagen ID: $request") // ✅ Log para verificar

            val response = RetrofitInstance.api.createSesion("Bearer $token", request)
            println("✅ Sesión creada: ${response}") // ✅ Log para verificar que Strapi lo recibe bien

            Toast.makeText(requireContext(), "Sesión creada exitosamente.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.sesionesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE
    }
}