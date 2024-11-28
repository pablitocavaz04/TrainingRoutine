package com.example.trainingroutine_pablocavaz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trainingroutine_pablocavaz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Mostrar ocultar el BottomNavigationView dependiendo del rol del usuario
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

        val userRole = getUserRole() // Obtener el rol del usuario
        if (userRole == "Entrenador") {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.navigation_coach_menu)
        } else {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.navigation_player_menu)
        }

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun getUserRole(): String {
        return "Jugador" // Cambiar a "Entrenador" para mostrar la lista de jugadores
    }
}
