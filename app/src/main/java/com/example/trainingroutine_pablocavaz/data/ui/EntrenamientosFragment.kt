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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.EntrenamientoAdapter
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.models.Entrenamiento
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EntrenamientosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrenamientos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewEntrenamientos)

        val sharedPreferences =
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            fetchEntrenamientos(token)
        } else {
            Toast.makeText(
                requireContext(),
                "Token no disponible. Inicie sesión nuevamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchEntrenamientos(token: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getEntrenamientos("Bearer $token")
                val entrenamientos: List<Entrenamiento> = response.data

                recyclerView.adapter = EntrenamientoAdapter(entrenamientos)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                Log.d("EntrenamientosFragment", "Entrenamientos cargados: ${entrenamientos.size}")
            } catch (e: HttpException) {
                Log.e("EntrenamientosFragment", "Error HTTP: ${e.response()?.errorBody()?.string()}")
                Toast.makeText(
                    requireContext(),
                    "Error al cargar entrenamientos",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("EntrenamientosFragment", "Error inesperado: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Error inesperado al cargar entrenamientos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
//LOG Y EXEPECIONES IA