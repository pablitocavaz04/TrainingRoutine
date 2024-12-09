package com.example.trainingroutine_pablocavaz.data.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingroutine_pablocavaz.data.local.dao.PersonaDao
import com.example.trainingroutine_pablocavaz.data.local.entities.PersonaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonaViewModel @Inject constructor(
    private val personaDao: PersonaDao
) : ViewModel() {

    fun insertPersona(persona: PersonaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            personaDao.insertPersona(persona)
        }
    }

    fun getPersonas(callback: (List<PersonaEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val personas = personaDao.getAllPersonas()
            callback(personas)
        }
    }
}
