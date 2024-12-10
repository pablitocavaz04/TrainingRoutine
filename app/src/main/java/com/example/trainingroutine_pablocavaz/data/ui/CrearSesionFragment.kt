package com.example.trainingroutine_pablocavaz.data.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.R
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

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        setupToolbar()
        loadEntrenadorId()
        loadEntrenamientos()
        loadJugadores()

        binding.btnGuardarSesion.setOnClickListener {
            guardarSesion()
        }

        binding.spinnerJugadores.setOnTouchListener { _, _ ->
            showJugadorSelectionDialog()
            true
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadEntrenadorId() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val userId = sharedPreferences.getInt("user_id", -1).toString()

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getPersonasByUserId("Bearer $token", userId)
                if (response.data.isNotEmpty()) {
                    entrenadorPersonaId = response.data[0].id
                    Log.d("CrearSesion", "Entrenador ID desde personas: $entrenadorPersonaId")
                } else {
                    Log.e("CrearSesion", "No se encontró el entrenador en la tabla personas.")
                }
            } catch (e: Exception) {
                Log.e("CrearSesion", "Error al cargar el ID del entrenador: ${e.message}")
            }
        }
    }



    private fun loadEntrenamientos() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getEntrenamientos("Bearer $token")
                entrenamientoMap.clear()
                response.data.forEach {
                    entrenamientoMap[it.attributes.nombre] = it.id
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    entrenamientoMap.keys.toList()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerEntrenamientos.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar entrenamientos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadJugadores() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getJugadores("Bearer $token")
                jugadorMap.clear()
                response.data.forEach {
                    val jugadorNombre = it.attributes.user?.data?.attributes?.username ?: "Sin nombre"
                    jugadorMap[jugadorNombre] = it.id
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar jugadores: ${e.message}", Toast.LENGTH_SHORT).show()
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
                val jugadorName = jugadorNames[which]
                val jugadorId = jugadorMap[jugadorName]
                if (isChecked) {
                    jugadorId?.let { selectedJugadorIds.add(it) }
                } else {
                    jugadorId?.let { selectedJugadorIds.remove(it) }
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(requireContext(), "Jugadores seleccionados: ${selectedJugadorIds.size}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun guardarSesion() {
        val nombreSesion = binding.inputNombreSesion.text.toString()
        val estadoSesion = binding.switchEstadoSesion.isChecked

        val entrenamientoSeleccionado = binding.spinnerEntrenamientos.selectedItem?.toString()
        val entrenamientoId = entrenamientoMap[entrenamientoSeleccionado]

        if (nombreSesion.isEmpty() || entrenamientoId == null || selectedJugadorIds.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", null)
                val entrenadorId = sharedPreferences.getInt("user_id", -1)

                if (entrenadorId == -1) {
                    Toast.makeText(requireContext(), "No se pudo obtener el ID del entrenador", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val request = CrearSesionRequest(
                    data = CrearSesionData(
                        nombre = nombreSesion,
                        estado = estadoSesion,
                        entrenamiento = entrenamientoId,
                        jugadores = selectedJugadorIds,
                        entrenador = entrenadorPersonaId!!
                    )
                )

                RetrofitInstance.api.createSesion("Bearer $token", request)
                Toast.makeText(requireContext(), "Sesión creada exitosamente", Toast.LENGTH_SHORT).show()


                findNavController().navigate(R.id.sesionesFragment)
            } catch (e: Exception) {
                Log.e("CrearSesion", "Error al crear la sesión: ${e.message}")
                Toast.makeText(requireContext(), "Error al crear la sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }
}

//LOGS Y EXEPCIONES CON IA