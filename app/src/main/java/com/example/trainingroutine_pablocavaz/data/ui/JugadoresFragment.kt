package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.Jugador
import com.example.trainingroutine_pablocavaz.data.adapters.JugadorAdapter

class JugadoresFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jugadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewJugadores)
        val jugadores = listOf(
            Jugador("Carlos Pérez", "Delantero"),
            Jugador("Luis García", "Defensa"),
            Jugador("Ana López", "Mediocampista")
        )
        recyclerView.adapter = JugadorAdapter(jugadores)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
