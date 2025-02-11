package com.example.trainingroutine_pablocavaz.data.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

class CrearSesionFragment : Fragment(R.layout.fragment_crear_sesion) {

    private var _binding: FragmentCrearSesionBinding? = null
    private val binding get() = _binding!!

    private val entrenamientoMap = mutableMapOf<String, Int>()
    private val jugadorMap = mutableMapOf<String, Int>()
    private val selectedJugadorIds = mutableListOf<Int>()
    private var entrenadorPersonaId: Int? = null

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
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
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
                enviarSesion(nombreSesion, estadoSesion, direccion, latitud, longitud, entrenamientoId)
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
        entrenamientoId: Int
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
                    entrenador = entrenadorPersonaId ?: return@launch
                )
            )

            RetrofitInstance.api.createSesion("Bearer $token", request)
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
