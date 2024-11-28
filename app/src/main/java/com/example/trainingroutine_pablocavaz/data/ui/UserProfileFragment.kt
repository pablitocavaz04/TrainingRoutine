package com.example.trainingroutine_pablocavaz.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // Cargar datos ficticios de usuario
        binding.userName.text = "Juan Pérez"
        binding.userEmail.text = "juan.perez@email.com"
        binding.userPhone.text = "+34 123 456 789"

        // Botón de cerrar sesión
        binding.logoutButton.setOnClickListener {
            // Aquí no eliminamos el token todavía
            // Solo navegamos al LoginFragment
            it.findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
        }

        return binding.root
    }
}
