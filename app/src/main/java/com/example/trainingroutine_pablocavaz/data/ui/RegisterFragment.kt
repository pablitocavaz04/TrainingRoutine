package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.R

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Navegar al LoginFragment al hacer clic en "¿Ya tienes cuenta? Inicia sesión aquí"
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)
        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }
}
