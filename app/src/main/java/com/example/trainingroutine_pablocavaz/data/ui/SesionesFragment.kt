package com.example.trainingroutine_pablocavaz.data.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
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
                val sesionesResponse = when (userRole) {
                    "Entrenador" -> RetrofitInstance.api.getSesionesCreadas("Bearer $token", userId)
                    else -> RetrofitInstance.api.getSesionesAsignadas("Bearer $token", userId)
                }

                val sesiones = sesionesResponse.data.map { it.attributes }

                val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val sesionesVistasSet = sharedPreferences.getStringSet("sesiones_vistas", emptySet()) ?: emptySet()

                val sesionesActuales = sesiones.map { it.nombre }.toSet()
                val nuevasSesiones = sesionesActuales - sesionesVistasSet

                if (userRole == "Jugador" && nuevasSesiones.isNotEmpty() && sesionesVistasSet.isNotEmpty()) {
                    mostrarNotificacionLocal(nuevasSesiones)
                }

                sharedPreferences.edit()
                    .putStringSet("sesiones_vistas", sesionesActuales)
                    .apply()

                // 🔹 Configurar RecyclerView
                recyclerView.apply {
                    adapter = SesionAdapter(sesiones)
                    layoutManager = LinearLayoutManager(requireContext())
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar las sesiones.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun mostrarNotificacionLocal(nuevasSesiones: Set<String>) {
        val channelId = "new_sessions"
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sesiones Asignadas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val mensaje = if (nuevasSesiones.size == 1) {
            "Tienes una nueva sesión: ${nuevasSesiones.first()}"
        } else {
            "Tienes ${nuevasSesiones.size} nuevas sesiones."
        }

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.mipmap.ic_launcher) //Icono cambiado aquí al logo de la app
            .setContentTitle("Nuevas Sesiones Asignadas")
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(2, notification)
    }


}
//LOGS Y EXPECIONES CON IA