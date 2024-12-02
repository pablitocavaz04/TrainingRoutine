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

        val userRole = getUserRole()
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
        return "Jugador"
    }
}

/*
package com.example.trainingroutine_pablocavaz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import com.example.trainingroutine_pablocavaz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.visibility = View.GONE // Inicialmente oculto

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Ocultar el menú en ciertas pantallas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        // Obtener el rol del usuario
        val jwt = intent.getStringExtra("JWT") ?: ""
        val userId = intent.getIntExtra("USER_ID", -1)
        if (jwt.isNotEmpty() && userId != -1) {
            fetchAndSetUserRole(jwt, userId)
        } else {
            Log.e("MainActivity", "JWT o USER_ID faltantes")
        }
    }

    private suspend fun configureMenu(role: String) {
        withContext(Dispatchers.Main) {
            Log.d("MainActivity", "Configurando menú para el rol: $role")
            bottomNavigationView.menu.clear()
            if (role == "Entrenador") {
                bottomNavigationView.inflateMenu(R.menu.navigation_coach_menu)
                Log.d("MainActivity", "Menú de entrenador configurado")
            } else {
                bottomNavigationView.inflateMenu(R.menu.navigation_player_menu)
                Log.d("MainActivity", "Menú de jugador configurado")
            }

            // Vincular el menú actualizado al NavController
            bottomNavigationView.setupWithNavController(navController)

            // Forzar un redibujado
            bottomNavigationView.requestLayout()
        }
    }


    private fun fetchAndSetUserRole(jwt: String, userId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getPersonasByUserId("Bearer $jwt", userId.toString())
                val role = response.data.firstOrNull()?.attributes?.Rol ?: "Jugador"

                Log.d("MainActivity", "Rol obtenido: $role")
                configureMenu(role)
                withContext(Dispatchers.Main) {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener el rol del usuario: ${e.message}")
            }
        }
    }
}
*/