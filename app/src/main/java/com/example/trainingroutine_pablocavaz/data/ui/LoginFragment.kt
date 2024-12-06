package com.example.trainingroutine_pablocavaz.data.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.SharedViewModel
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.data.remote.models.LoginRequest
import com.example.trainingroutine_pablocavaz.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Ahora incluye ambos parámetros
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }


    private fun loginUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val loginResponse = RetrofitInstance.api.login(LoginRequest(username, password))
                val token = loginResponse.jwt
                val userId = loginResponse.user.id.toInt()

                // Guardar token e ID en SharedPreferences
                val sharedPreferences =
                    requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("token", token)
                    putInt("user_id", userId)
                    apply()
                }

                // Obtener y guardar el rol del usuario
                getPersonaRol(userId, token)

                Toast.makeText(
                    requireContext(),
                    "Inicio de sesión exitoso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: HttpException) {
                Log.e(
                    "LoginFragment",
                    "HTTP error: ${e.response()?.errorBody()?.string()}"
                )
                Toast.makeText(
                    requireContext(),
                    "Error en el inicio de sesión",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("LoginFragment", "Error en el inicio de sesión: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Error en el inicio de sesión",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPersonaRol(userId: Int, token: String) {
        lifecycleScope.launch {
            try {
                val personaResponse =
                    RetrofitInstance.api.getPersonasByUserId("Bearer $token", userId.toString())
                if (personaResponse.data.isNotEmpty()) {
                    val personaAttributes = personaResponse.data.first().attributes
                    val role = personaAttributes.Rol

                    // Guardar el rol en SharedPreferences
                    val sharedPreferences =
                        requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("role", role)
                        apply()
                    }

                    sharedViewModel.setUserRole(role) // Actualizar el rol en el ViewModel
                    findNavController().navigate(R.id.sesionesFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se encontró información del rol del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: HttpException) {
                Log.e(
                    "LoginFragment",
                    "HTTP error al obtener persona: ${e.response()?.errorBody()?.string()}"
                )
                Toast.makeText(
                    requireContext(),
                    "Error al obtener la persona asociada",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("LoginFragment", "Error al obtener persona: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Error al obtener la persona asociada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
