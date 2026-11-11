package io.github.mirukurusan.wificountrycode.ui.route.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mirukurusan.wificountrycode.R
import io.github.mirukurusan.wificountrycode.viewmodel.ConfigViewModel

@Immutable
enum class TopLevelDestination(
    val label: @Composable () -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    CONFIG(
        label = { stringResource(R.string.navi_btn_config) },
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build,
    ),
    ABOUT(
        label = { stringResource(R.string.navi_btn_about) },
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
    )
}

private val currentPage = mutableStateOf(TopLevelDestination.CONFIG)

/**
 * Main launcher screen with bottom navigation
 */
@Composable
@Preview
fun LauncherScreen() {
    val bottomNavBackground = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05F)
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val configViewModel: ConfigViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner
    )

    @Composable
    fun RowScope.NavItem(
        destination: TopLevelDestination,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector,
        label: String,
    ) {
        val selectedColor = MaterialTheme.colorScheme.primary
        val unselectedColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6F)
        val selected = currentPage.value == destination

        val (icon, color) =
            (if (selected) arrayOf(selectedIcon, selectedColor)
            else arrayOf(unselectedIcon, unselectedColor))

        val scale = if (selected) 1.1F else 1F
        NavigationBarItem(
            selected = selected,
            onClick = {
                currentPage.value = destination
            },
            icon = {
                Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = null,
                    tint = color as Color,
                    modifier = Modifier.scale(scale)
                )
            },
            label = { Text(text = label, modifier = Modifier.scale(scale)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
    }

    Scaffold(
        bottomBar = {
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            NavigationBar(
                containerColor = bottomNavBackground,
                tonalElevation = 0.dp
            ) {
                TopLevelDestination.entries.forEach { screenType ->
                    NavItem(
                        destination = screenType,
                        selectedIcon = screenType.selectedIcon,
                        unselectedIcon = screenType.unselectedIcon,
                        label = screenType.label()
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when (currentPage.value) {
                TopLevelDestination.CONFIG -> ConfigurationScreen(configViewModel)
                TopLevelDestination.ABOUT -> AboutScreen()
            }
        }
    }
}
