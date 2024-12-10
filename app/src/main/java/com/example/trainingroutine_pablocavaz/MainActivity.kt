package com.example.trainingroutine_pablocavaz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trainingroutine_pablocavaz.data.repository.EntrenamientoRepository
import com.example.trainingroutine_pablocavaz.data.repository.PersonaRepository
import com.example.trainingroutine_pablocavaz.data.repository.SesionRepository
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.EntrenamientoViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.PersonaViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.SesionViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.SharedViewModel
import com.example.trainingroutine_pablocavaz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel

    private val entrenamientoViewModel: EntrenamientoViewModel by viewModels()
    private val sesionViewModel: SesionViewModel by viewModels()
    private val personaViewModel: PersonaViewModel by viewModels()

    @Inject
    lateinit var personaRepository: PersonaRepository

    @Inject
    lateinit var sesionRepository: SesionRepository

    @Inject
    lateinit var entrenamientoRepository: EntrenamientoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registerFragment -> {
                    bottomNavigationView.visibility = android.view.View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = android.view.View.VISIBLE
                }
            }
        }

        sharedViewModel.userRole.observe(this) { role ->
            configureMenuBasedOnRole(role, bottomNavigationView)
        }

        bottomNavigationView.setupWithNavController(navController)

        // Intentar sincronizar datos al iniciar la app
        sincronizarDatos()
    }

    private fun configureMenuBasedOnRole(role: String, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.clear()
        if (role == "Entrenador") {
            bottomNavigationView.inflateMenu(R.menu.navigation_coach_menu)
        } else {
            bottomNavigationView.inflateMenu(R.menu.navigation_player_menu)
        }
        bottomNavigationView.requestLayout() // Forzar redibujado del menú
    }

    private fun sincronizarDatos() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        if (token.isNullOrEmpty()) {
            println("Sincronización cancelada: No hay token almacenado.")
            return
        }

        lifecycleScope.launch {
            try {
                personaRepository.syncPersonas("Bearer $token")
                val personas = personaRepository.getAllPersonas()
                personas.forEach { println("Persona sincronizada: ${it.rol}") }
            } catch (e: Exception) {
                println("Error sincronizando personas: ${e.message}")
            }
        }

        lifecycleScope.launch {
            try {
                sesionRepository.syncSesiones("Bearer $token")
                val sesiones = sesionRepository.getAllSesiones()
                sesiones.forEach { println("Sesion sincronizada: ${it.nombre}") }
            } catch (e: Exception) {
                println("Error sincronizando sesiones: ${e.message}")
            }
        }

        lifecycleScope.launch {
            try {
                entrenamientoRepository.syncEntrenamientos("Bearer $token")
                val entrenamientos = entrenamientoRepository.getAllEntrenamientos()
                entrenamientos.forEach { println("Entrenamiento sincronizado: ${it.nombre}") }
            } catch (e: Exception) {
                println("Error sincronizando entrenamientos: ${e.message}")
            }
        }
    }

    fun guardarToken(jwt: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("jwt_token", jwt)
            apply()
        }
    }
}
//Logs y Excepciones con IA