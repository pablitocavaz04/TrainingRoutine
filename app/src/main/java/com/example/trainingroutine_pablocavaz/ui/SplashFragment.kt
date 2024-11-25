package com.example.trainingroutine_pablocavaz.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trainingroutine_pablocavaz.R
import androidx.fragment.app.commit



class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Opcional: Configurar un retraso para pasar al siguiente fragmento
        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager.commit {
                replace(R.id.container,LoginFragment())
            }
        }, 3000) // Retraso de 3 segundos

        return view
    }
}