package com.example.trainingroutine_pablocavaz.data.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.models.SesionData
import com.example.trainingroutine_pablocavaz.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var googleMap: GoogleMap
    private var userRole: String? = null
    private var userId: Int? = null

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        fetchSesionesAndAddMarkers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener el rol y el ID del usuario
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        userRole = sharedPreferences.getString("role", null)
        userId = sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun fetchSesionesAndAddMarkers() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token == null || userRole == null || userId == null) {
            Toast.makeText(requireContext(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val sesionesResponse = if (userRole == "Entrenador") {
                    RetrofitInstance.api.getSesionesCreadas("Bearer $token", userId!!)
                } else {
                    RetrofitInstance.api.getSesionesAsignadas("Bearer $token", userId!!)
                }

                if (sesionesResponse.data.isNotEmpty()) {
                    for (sesion in sesionesResponse.data) {
                        addMarkerForSesion(sesion)
                    }
                } else {
                    Toast.makeText(requireContext(), "No hay sesiones con ubicaciones registradas.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Log.e("MapsFragment", "Error HTTP: ${e.message}")
                Toast.makeText(requireContext(), "Error al cargar sesiones", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("MapsFragment", "Error inesperado: ${e.message}")
                Toast.makeText(requireContext(), "Error inesperado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMarkerForSesion(sesion: SesionData) {
        val latitud = sesion.attributes.latitud
        val longitud = sesion.attributes.longitud
        val nombreSesion = sesion.attributes.nombre

        if (latitud != null && longitud != null) {
            val location = LatLng(latitud, longitud)
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(nombreSesion)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }
}
