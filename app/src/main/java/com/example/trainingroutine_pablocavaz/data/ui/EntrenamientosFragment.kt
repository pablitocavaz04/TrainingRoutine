package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.Entrenamiento
import com.example.trainingroutine_pablocavaz.data.adapters.EntrenamientoAdapter

class EntrenamientosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrenamientos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEntrenamientos)
        val entrenamientos = listOf(
            Entrenamiento("Entrenamiento Físico", "2024-11-30", "Heavy"),
            Entrenamiento("Táctica Ofensiva", "2024-12-01", "Strong")
        )
        recyclerView.adapter = EntrenamientoAdapter(entrenamientos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}
