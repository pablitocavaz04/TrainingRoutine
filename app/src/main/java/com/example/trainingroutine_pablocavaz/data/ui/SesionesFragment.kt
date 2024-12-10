package com.example.trainingroutine_pablocavaz.data.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.SesionAdapter
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SesionesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabCrearSesion: FloatingActionButton
    private var userRole: String? = null
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sesiones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewSesiones)
        fabCrearSesion = view.findViewById(R.id.fabCrearSesion)

        val sharedPreferences =
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        userRole = sharedPreferences.getString("role", null)
        userId = sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }

        println("TOKEN: $token")
        println("USER ROLE: $userRole")
        println("USER ID: $userId")

        if (token != null && userRole != null && userId != null) {
            setupFabVisibility(userRole!!)
            fetchSesiones(token, userId!!)
        } else {
            Toast.makeText(
                requireContext(),
                "Token, rol o ID no disponibles. Inicie sesión nuevamente.",
                Toast.LENGTH_SHORT
            ).show()
        }

        fabCrearSesion.setOnClickListener {
            findNavController().navigate(R.id.action_sesionesFragment_to_crearSesionFragment)
        }
    }

    private fun setupFabVisibility(role: String) {
        fabCrearSesion.visibility = if (role == "Entrenador") View.VISIBLE else View.GONE
    }

    private fun fetchSesiones(token: String, userId: Int) {
        lifecycleScope.launch {
            try {
                val sesionesResponse = if (userRole == "Entrenador") {
                    RetrofitInstance.api.getSesionesCreadas(
                        "Bearer $token",
                        userId
                    )
                } else {
                    RetrofitInstance.api.getSesionesAsignadas(
                        "Bearer $token",
                        userId
                    )
                }

                if (sesionesResponse.data.isNotEmpty()) {
                    val sesiones = sesionesResponse.data.map { it.attributes }
                    recyclerView.adapter = SesionAdapter(sesiones)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    Toast.makeText(requireContext(), "No hay sesiones disponibles.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: HttpException) {
                println("HTTP EXCEPTION: ${e.message()}")
                Toast.makeText(requireContext(), "Error al cargar las sesiones.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                println("GENERAL EXCEPTION: ${e.message}")
                Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
//LOGS Y EXPECIONES CON IA