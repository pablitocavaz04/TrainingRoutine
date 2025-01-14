package com.example.trainingroutine_pablocavaz.data.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.JugadorAdapter
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class JugadoresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jugadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewJugadores)

        val sharedPreferences =
            requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1 // Cada elemento ocupa 1 columna
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        if (token != null) {
            fetchJugadores(token)
        } else {
            Toast.makeText(
                requireContext(),
                "Token no disponible. Inicie sesión nuevamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchJugadores(token: String) {
        lifecycleScope.launch {
            try {
                val jugadoresResponse = RetrofitInstance.api.getJugadores("Bearer $token")
                val jugadores = jugadoresResponse.data
                recyclerView.adapter = JugadorAdapter(jugadores)
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Error al cargar los jugadores", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
//LOGS EXEPCIONES CON IA