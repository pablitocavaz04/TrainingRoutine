package com.example.trainingroutine_pablocavaz.data.ui

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
import com.example.trainingroutine_pablocavaz.data.remote.models.RegisterRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.PersonaRequest
import com.example.trainingroutine_pablocavaz.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbIsTrainer.setOnCheckedChangeListener { _, isChecked ->
            binding.etTrainerKey.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val isTrainer = binding.cbIsTrainer.isChecked
            val trainerKey = binding.etTrainerKey.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (isTrainer && trainerKey != "ENTRENADOR2024") {
                    Toast.makeText(requireContext(), "Clave incorrecta", Toast.LENGTH_SHORT).show()
                } else {
                    val role = if (isTrainer) "entrenador" else "jugador"
                    registerUser(name, email, password, role)
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun registerUser(name: String, email: String, password: String, role: String) {
        lifecycleScope.launch {
            try {
                val registerResponse = RetrofitInstance.api.register(
                    RegisterRequest(name, email, password)
                )

                val userId = registerResponse.user.id
                val token = registerResponse.jwt

                createPersona(userId, role, token)

            } catch (e: Exception) {
                Log.e("RegisterFragment", "Error en el registro: ${e.message}")
                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPersona(userId: Int, role: String, token: String) {
        lifecycleScope.launch {
            try {
                RetrofitInstance.api.createPersona(
                    "Bearer $token",
                    PersonaRequest(role, userId)
                )
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            } catch (e: Exception) {
                Log.e("RegisterFragment", "Error al crear la persona: ${e.message}")
                Toast.makeText(requireContext(), "Error al crear la persona", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
