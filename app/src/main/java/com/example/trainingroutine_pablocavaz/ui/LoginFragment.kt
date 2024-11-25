package com.example.trainingroutine_pablocavaz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.trainingroutine_pablocavaz.R

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Listener para ir al RegisterFragment
        view.findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container, RegisterFragment())
                addToBackStack(null) // Añade a la pila para volver atrás
            }
        }

        return view
    }
}
