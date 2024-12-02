package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.databinding.FragmentCrearSesionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CrearSesionFragment : Fragment(R.layout.fragment_crear_sesion) {

    private var _binding: FragmentCrearSesionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCrearSesionBinding.bind(view)

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarSesion.setOnClickListener {
            val nombreEntrenamiento = binding.inputNombreEntrenamiento.text.toString()
            val jugadores = binding.inputJugadores.text.toString()

            if (nombreEntrenamiento.isNotEmpty() && jugadores.isNotEmpty()) {
                Toast.makeText(requireContext(), "Sesión guardada con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp() // Volver a SesionesFragment
            } else {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
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
