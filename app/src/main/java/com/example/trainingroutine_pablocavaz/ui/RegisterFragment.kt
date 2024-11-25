package com.example.trainingroutine_pablocavaz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.trainingroutine_pablocavaz.R

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Listener para volver al LoginFragment
        view.findViewById<TextView>(R.id.tvGoToLogin).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container, LoginFragment())
                addToBackStack(null)
            }
        }

        return view
    }
}
