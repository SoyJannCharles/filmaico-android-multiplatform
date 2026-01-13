package com.jycra.filmaico.feature.signup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.feature.signup.SignUpUiEvent

@Composable
fun SignInActionLink(
    onEvent: (SignUpUiEvent) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(R.string.signup_text_i_have_account)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onEvent(SignUpUiEvent.OnSignInClick) }
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(R.string.signup_link_go_to_signin),
        )

    }

}