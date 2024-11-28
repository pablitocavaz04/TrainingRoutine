package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trainingroutine_pablocavaz.R

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Se infla el layout y se hace referencia a las vistas a través del 'view'
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Cargar datos ficticios de usuario
        view.findViewById<TextView>(R.id.user_name).text = "Juan Pérez"
        view.findViewById<TextView>(R.id.user_email).text = "juan.perez@email.com"
        view.findViewById<TextView>(R.id.user_phone).text = "+34 123 456 789"

        // Botón de cerrar sesión (lógica ficticia)
        view.findViewById<Button>(R.id.logout_button).setOnClickListener {
            // Aquí pondrías la lógica para cerrar sesión, si es necesario.
            // Por ejemplo, limpiar los SharedPreferences o JWT token.
        }

        return view
    }
}
