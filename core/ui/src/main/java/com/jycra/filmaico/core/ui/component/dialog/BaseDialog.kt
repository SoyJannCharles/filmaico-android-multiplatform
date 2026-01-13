package com.jycra.filmaico.core.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    hasActions: Boolean = true,
    actions: @Composable () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {

        Card(
            onClick = {},
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.widthIn(min = 280.dp, max = 420.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 48.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                        ProvideTextStyle(value = MaterialTheme.typography.headlineLarge) {
                            icon()
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                        title()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                        text()
                    }
                }

                if (hasActions) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            actions()
                        }
                    }
                }

            }

        }

    }

}