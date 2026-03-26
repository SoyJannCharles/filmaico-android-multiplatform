package com.jycra.filmaico.feature.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.feature.auth.ActionLink
import com.jycra.filmaico.domain.user.model.AuthStatus
import com.jycra.filmaico.feature.signin.component.SignInForm
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    platform: Platform,
    onEvent: (SignInUiEvent) -> Unit
) {

    Screen(
        uiState = uiState,
        platform = platform,
        onEvent = onEvent
    )

}

@Composable
private fun Screen(
    uiState: SignInUiState,
    platform: Platform,
    onEvent: (SignInUiEvent) -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                FilmaicoLogo()

                if (platform == Platform.TV) {

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            SignInForm(
                                modifier = Modifier
                                    .weight(1f),
                                uiState = uiState,
                                onEvent = onEvent
                            )

                            ActionLink(
                                questionText = stringResource(R.string.signin_text_no_account),
                                actionText = stringResource(R.string.signin_link_go_to_signup),
                                onActionClick = { onEvent(SignInUiEvent.SignUpTriggered) }
                            )

                        }

                        VerticalDivider(
                            modifier = Modifier.padding(horizontal = 48.dp, vertical = 64.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "Vincula tu Dispositivo",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Text(
                                text = "Ingresa este código en la app de tu móvil para autenticarte",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                tonalElevation = 8.dp
                            ) {

                                if (uiState.authCode == null)
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .padding(24.dp)
                                            .size(24.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                else
                                    Text(
                                        text = uiState.authCode.chunked(3).joinToString(" - "),
                                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                            }

                            if (uiState.authCode != null) {
                                AuthTimer(
                                    expiresAt = uiState.authCodeExpiresAt,
                                    onExpire = { onEvent(SignInUiEvent.OnCodeExpired) }
                                )
                            }

                            val statusMessage = when (uiState.authSessionStatus) {
                                AuthStatus.PENDING -> "Esperando vinculación..."
                                AuthStatus.COMPLETED -> "¡Código detectado! Iniciando sesión..."
                                AuthStatus.ERROR -> "Ha sudedido un error"
                            }

                            Text(
                                text = statusMessage,
                                modifier = Modifier.padding(top = 24.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                        }

                    }

                } else {

                    SignInForm(
                        modifier = Modifier
                            .weight(1f),
                        uiState = uiState,
                        onEvent = onEvent
                    )

                    ActionLink(
                        questionText = stringResource(R.string.signin_text_no_account),
                        actionText = stringResource(R.string.signin_link_go_to_signup),
                        onActionClick = { onEvent(SignInUiEvent.SignUpTriggered) }
                    )

                }

            }

        }

    }

}

@Composable
fun AuthTimer(expiresAt: Date?, onExpire: () -> Unit) {

    var remainingSeconds by remember(expiresAt) {
        mutableLongStateOf(
            expiresAt?.let { (it.time - System.currentTimeMillis()) / 1000 } ?: 0L
        )
    }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else if (expiresAt != null) {
            onExpire()
        }
    }

    if (remainingSeconds > 0) {

        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60

        Text(
            text = "El código expira en %02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.bodyMedium,
            color = if (remainingSeconds < 60) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )

    }

}