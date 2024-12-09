package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingroutine_pablocavaz.data.local.dao.SesionDao
import com.example.trainingroutine_pablocavaz.data.local.entities.SesionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SesionViewModel @Inject constructor(
    private val sesionDao: SesionDao
) : ViewModel() {

    fun insertSesion(sesion: SesionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            sesionDao.insertSesion(sesion)
        }
    }

    fun getSesiones(callback: (List<SesionEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val sesiones = sesionDao.getAllSesiones()
            callback(sesiones)
        }
    }
}
