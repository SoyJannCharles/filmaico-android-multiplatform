package com.jycra.filmaico.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.util.UiText
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.usecase.SyncMetadataRealtimeUseCase
import com.jycra.filmaico.domain.media.usecase.SyncMetadataSnapshotUseCase
import com.jycra.filmaico.domain.user.usecase.GetCurrentUserUseCase
import com.jycra.filmaico.feature.movie.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncMetadataSnapshotUseCase: SyncMetadataSnapshotUseCase,
    private val syncMetadataRealtimeUseCase: SyncMetadataRealtimeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        startGlobalSync()
    }

    private fun startGlobalSync() {

        viewModelScope.launch {

            try {

                syncMetadataSnapshotUseCase(MediaType.CHANNEL)

                _uiState.value = MainUiState.Success()

                launch { syncMetadataRealtimeUseCase(MediaType.CHANNEL) }
                launch { syncMetadataRealtimeUseCase(MediaType.MOVIE) }
                launch { syncMetadataRealtimeUseCase(MediaType.SERIE) }
                launch { syncMetadataRealtimeUseCase(MediaType.ANIME) }

                loadSubscriptionData()

            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e.message ?: "Error desconocido")
            }

        }

    }

    private fun loadSubscriptionData() {

        viewModelScope.launch {

            val user = getCurrentUserUseCase() ?: return@launch
            val expiry = user.subscription.expirationDate.time

            while (isActive) {

                val now = System.currentTimeMillis()
                val diff = expiry - now
                val text = formatTimeRemaining(diff)
                val urgent = diff < TimeUnit.DAYS.toMillis(5)

                _uiState.update { currentState ->

                    if (currentState is MainUiState.Success) {

                        currentState.copy(
                            expirationTimestamp = expiry,
                            expirationText = text,
                            isSubscriptionUrgent = urgent
                        )

                    } else {

                        MainUiState.Success(
                            expirationTimestamp = expiry,
                            expirationText = text,
                            isSubscriptionUrgent = urgent
                        )

                    }

                }


                val delayTime = if (diff < TimeUnit.DAYS.toMillis(1)) {
                    TimeUnit.MINUTES.toMillis(1)
                } else {
                    TimeUnit.HOURS.toMillis(1)
                }

                delay(delayTime)

            }

        }

    }

    private fun formatTimeRemaining(diffMillis: Long): UiText {

        val diff = diffMillis.coerceAtLeast(0)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {

            diff <= 0 -> UiText.StringResource(R.string.subscription_expired)

            days < 1 -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours < 1) {
                    val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
                    UiText.StringResource(R.string.subscription_mins, mins)
                } else {
                    UiText.StringResource(R.string.subscription_hours, hours)
                }
            }

            days < 30 -> UiText.StringResource(R.string.subscription_days, days)

            days < 365 -> {
                val months = days / 30
                if (months == 1L)
                    UiText.StringResource(R.string.subscription_month, months)
                else
                    UiText.StringResource(R.string.subscription_months, months)
            }

            else -> {
                val years = days / 365
                if (years == 1L)
                    UiText.StringResource(R.string.subscription_year, years)
                else
                    UiText.StringResource(R.string.subscription_years, years)
            }

        }

    }

}