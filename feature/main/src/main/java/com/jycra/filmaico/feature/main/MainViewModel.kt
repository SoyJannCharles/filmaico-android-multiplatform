package com.jycra.filmaico.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.user.usecase.SaveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveSessionUseCase: SaveSessionUseCase
) : ViewModel() {

    init {
        registerDeviceSession()
    }

    private fun registerDeviceSession() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveSessionUseCase()
            } catch (e: Exception) {

            }
        }
    }

}