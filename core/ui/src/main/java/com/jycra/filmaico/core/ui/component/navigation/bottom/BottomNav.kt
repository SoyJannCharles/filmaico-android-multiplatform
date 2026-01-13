package com.jycra.filmaico.core.ui.component.navigation.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jycra.filmaico.core.ui.theme.color.Gradient

@Composable
fun BottomNav(
    currentRoute: String?,
    items: List<BottomNavItem>,
    onNavigate: (String) -> Unit
) {

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Gradient.verticalBottomBarGradient()
            ),
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = { Icon(painterResource(id = item.iconResId), contentDescription = null) },
                label = { Text(stringResource(id = item.titleResId)) },
                onClick = { onNavigate(item.route) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}