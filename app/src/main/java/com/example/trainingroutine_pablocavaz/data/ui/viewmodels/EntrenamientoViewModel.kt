package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingroutine_pablocavaz.data.local.dao.EntrenamientoDao
import com.example.trainingroutine_pablocavaz.data.local.entities.EntrenamientoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenamientoViewModel @Inject constructor(
    private val entrenamientoDao: EntrenamientoDao
) : ViewModel() {

    fun insertEntrenamiento(entrenamiento: EntrenamientoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            entrenamientoDao.insertEntrenamiento(entrenamiento)
        }
    }

    fun getEntrenamientos(callback: (List<EntrenamientoEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val entrenamientos = entrenamientoDao.getAllEntrenamientos()
            callback(entrenamientos)
        }
    }
}
