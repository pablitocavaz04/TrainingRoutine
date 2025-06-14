package com.example.trainingroutine_pablocavaz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
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
import com.google.android.material.bottomsheet.BottomSheetDialog
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
            Log.d("Navigation", "Fragment actual: ${destination.label} (${destination.id})")
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        sharedViewModel.userRole.observe(this) { role ->
            Log.d("MainActivity", "Rol detectado: $role")
            configureMenuBasedOnRole(role, bottomNavigationView)
            bottomNavigationView.setupWithNavController(navController)

            // Escuchar clics del botón "Más"
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.moreOptions -> {
                        showMoreOptions()
                        false
                    }
                    else -> {
                        NavigationUI.onNavDestinationSelected(item, navController)
                        true
                    }
                }
            }

            // Forzar navegación inicial
            when (role) {
                "Entrenador", "Jugador" -> {
                    navController.navigate(R.id.sesionesFragment)
                }
            }
        }

        sincronizarDatos()
    }

    private fun configureMenuBasedOnRole(role: String, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.menu.clear()
        if (role == "Entrenador") {
            bottomNavigationView.inflateMenu(R.menu.navigation_coach_menu)
            Log.d("MainActivity", "Menú de entrenador inflado")
        } else {
            bottomNavigationView.inflateMenu(R.menu.navigation_player_menu)
            Log.d("MainActivity", "Menú de jugador inflado")
        }
        bottomNavigationView.requestLayout()
    }

    private fun showMoreOptions() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_more_options, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)

        dialogView.findViewById<View>(R.id.btn_profile).setOnClickListener {
            bottomSheetDialog.dismiss()
            navController.navigate(R.id.userProfileFragment)
        }

        dialogView.findViewById<View>(R.id.btn_about).setOnClickListener {
            bottomSheetDialog.dismiss()
            navController.navigate(R.id.aboutMeFragment)
        }

        bottomSheetDialog.show()
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
