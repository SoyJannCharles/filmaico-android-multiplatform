package com.jycra.filmaico.feature.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.app.AppHealth

@Composable
fun UpdateRoute(
    viewModel: UpdateViewModel = hiltViewModel()
) {

    val health by viewModel.appHealth.collectAsStateWithLifecycle()

    when (val state = health) {
        is AppHealth.UpdateRequired -> {
            UpdateScreen(
                currentVersion = state.current,
                serverVersion = state.server
            )
        }
        else -> {

        }
    }

}