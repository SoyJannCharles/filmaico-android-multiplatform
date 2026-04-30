package com.jycra.filmaico.feature.update

import androidx.lifecycle.ViewModel
import com.jycra.filmaico.core.app.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val appHealth = sessionManager.appHealth

    fun retryCheck() {
        sessionManager.checkHealth()
    }

}