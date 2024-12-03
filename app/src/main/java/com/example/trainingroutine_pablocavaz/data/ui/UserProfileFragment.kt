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
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.databinding.FragmentUserProfileBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            loadUserProfile(token)
        } else {
            redirectToLogin()
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun loadUserProfile(token: String) {
        lifecycleScope.launch {
            try {
                val userDetailsResponse = RetrofitInstance.api.getUserDetails("Bearer $token")
                val personaResponse = RetrofitInstance.api.getPersonasByUserId("Bearer $token", userDetailsResponse.id.toString())

                val userName = userDetailsResponse.username
                val userEmail = userDetailsResponse.email
                val userRole = personaResponse.data.firstOrNull()?.attributes?.Rol ?: "Desconocido"

                binding.userName.text = userName
                binding.userEmail.text = userEmail
                binding.userRole.text = userRole
            } catch (e: HttpException) {
                Log.e("UserProfileFragment", "Error al cargar perfil: ${e.response()?.errorBody()?.string()}")
                Toast.makeText(requireContext(), "Error al cargar perfil del usuario", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("UserProfileFragment", "Error inesperado: ${e.message}")
                Toast.makeText(requireContext(), "Error inesperado al cargar perfil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

        // Redirigir al LoginFragment
        redirectToLogin()
    }

    private fun redirectToLogin() {
        findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
    }
}
