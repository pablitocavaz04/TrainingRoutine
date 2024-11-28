package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.databinding.FragmentSesionesBinding
import com.example.trainingroutine_pablocavaz.data.adapters.SesionAdapter
import com.example.trainingroutine_pablocavaz.data.adapters.Sesion

class SesionesFragment : Fragment() {

    private var _binding: FragmentSesionesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sesionAdapter: SesionAdapter
    private val mockSesiones = listOf(
        Sesion("Sesión 1", "Entrenamiento A", "Entrenador X"),
        Sesion("Sesión 2", "Entrenamiento B", "Entrenador Y")
    )

    private val userRole = "Jugador" ///////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSesionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        if (userRole == "Entrenador") {
            binding.fabCrearSesion.visibility = View.VISIBLE
            binding.fabCrearSesion.setOnClickListener {
                findNavController().navigate(R.id.action_sesionesFragment_to_crearSesionFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        sesionAdapter = SesionAdapter(mockSesiones)
        binding.recyclerViewSesiones.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sesionAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
