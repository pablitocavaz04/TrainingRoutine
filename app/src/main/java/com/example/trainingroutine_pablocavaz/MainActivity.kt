package com.example.trainingroutine_pablocavaz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trainingroutine_pablocavaz.data.local.entities.EntrenamientoEntity
import com.example.trainingroutine_pablocavaz.data.local.entities.PersonaEntity
import com.example.trainingroutine_pablocavaz.data.local.entities.SesionEntity
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.EntrenamientoViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.PersonaViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.SesionViewModel
import com.example.trainingroutine_pablocavaz.data.ui.viewmodels.SharedViewModel
import com.example.trainingroutine_pablocavaz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel
    private val entrenamientoViewModel: EntrenamientoViewModel by viewModels()
    private val sesionViewModel: SesionViewModel by viewModels()
    private val personaViewModel: PersonaViewModel by viewModels()

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

        // Prueba: Insertar y recuperar entrenamientos, sesiones y personas
        pruebaDatos()
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

    private fun pruebaDatos() {
        // Insertar un ejemplo de entrenamiento
        val entrenamiento = EntrenamientoEntity(
            id = 1,
            nombre = "Entrenamiento de Prueba",
            descripcion = "Prueba para validar Room",
            fecha = "2024-12-10",
            imagen = null
        )
        entrenamientoViewModel.insertEntrenamiento(entrenamiento)

        // Insertar un ejemplo de sesión
        val sesion = SesionEntity(
            id = 1,
            nombre = "Sesion de Prueba",
            estado = true,
            entrenamientoId = 1,
            entrenadorId = 1,
            jugadoresId = listOf(1, 2, 3)
        )
        sesionViewModel.insertSesion(sesion)

        // Insertar un ejemplo de persona
        val persona = PersonaEntity(
            id = 1,
            rol = "Jugador",
            usuarioId = 1
        )
        personaViewModel.insertPersona(persona)

        // Recuperar y mostrar datos
        entrenamientoViewModel.getEntrenamientos { entrenamientos ->
            entrenamientos.forEach { println("Entrenamiento: ${it.nombre}") }
        }

        sesionViewModel.getSesiones { sesiones ->
            sesiones.forEach { println("Sesion: ${it.nombre}") }
        }

        personaViewModel.getPersonas { personas ->
            personas.forEach { println("Persona: ${it.rol}") }
        }
    }
}
