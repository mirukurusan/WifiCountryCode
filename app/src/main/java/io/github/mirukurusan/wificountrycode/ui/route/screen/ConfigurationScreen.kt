package io.github.mirukurusan.wificountrycode.ui.route.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mirukurusan.wificountrycode.R
import io.github.mirukurusan.wificountrycode.ui.components.ScrollColumn
import io.github.mirukurusan.wificountrycode.ui.components.SmallTitle
import io.github.mirukurusan.wificountrycode.viewmodel.ConfigViewModel

/**
 * Configuration screen for WiFi country code settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(viewModel: ConfigViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadConfig()
    }

    // Listen for save success and show toast
    LaunchedEffect(viewModel.saveSuccess.value) {
        if (viewModel.saveSuccess.value) {
            Toast.makeText(context, "Configuration saved. Restart system to take effect.", Toast.LENGTH_LONG).show()
            viewModel.saveSuccess.value = false // Reset the state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.config_title)) }
            )
        }
    ) { paddingValues ->
        ScrollColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.Top
        ) {
            // Module Status Section
            SmallTitle { stringResource(R.string.module_status) }
            ModuleActivationStatus(
                active = viewModel.moduleActive.value
            )

            Spacer(modifier = Modifier.size(16.dp))

            ModuleStatusCard(
                enabled = viewModel.configState.enabled.value,
                onEnabledChange = { viewModel.configState.enabled.value = it },
                moduleActive = viewModel.moduleActive.value
            )

            Spacer(modifier = Modifier.size(24.dp))

            // Country Code Section
            SmallTitle { stringResource(R.string.config_country_code) }
            CountryCodeInput(
                value = viewModel.configState.countryCode.value,
                onValueChange = { viewModel.configState.countryCode.value = it.uppercase() },
                enabled = viewModel.moduleActive.value && viewModel.configState.enabled.value
            )

            Spacer(modifier = Modifier.size(24.dp))

            // Quick Select Buttons
            QuickCountryCodeSelect(
                onCodeSelected = { viewModel.configState.countryCode.value = it },
                enabled = viewModel.moduleActive.value && viewModel.configState.enabled.value
            )

            Spacer(modifier = Modifier.size(32.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveConfig() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.moduleActive.value
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.config_save))
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ModuleActivationStatus(
    active: Boolean
) {
    val containerColor = if (active)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    else
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
    val contentColor = if (active)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error
    val icon = if (active) Icons.Filled.CheckCircle else Icons.Filled.Error
    val text = if (active) stringResource(R.string.module_active) else stringResource(R.string.module_inactive)
    val hint = if (!active) stringResource(R.string.module_inactive_hint) else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )
        }
        if (hint != null) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 36.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun ModuleStatusCard(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    moduleActive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.config_enable),
                style = MaterialTheme.typography.titleMedium,
                color = if (moduleActive) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Text(
                text = stringResource(R.string.config_enable_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (moduleActive) 0.7f else 0.38f)
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = onEnabledChange,
            enabled = moduleActive
        )
    }
}

@Composable
private fun CountryCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Column {
        Text(
            text = stringResource(R.string.config_country_code_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= 2) {
                    onValueChange(newValue.uppercase())
                }
            },
            placeholder = { Text(stringResource(R.string.config_country_code_hint)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Public,
                    contentDescription = null
                )
            },
            singleLine = true
        )
    }
}

@Composable
private fun QuickCountryCodeSelect(
    onCodeSelected: (String) -> Unit,
    enabled: Boolean
) {
    Column {
        SmallTitle { "Quick Select" }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickCodeButton("US", onCodeSelected, enabled)
            QuickCodeButton("CN", onCodeSelected, enabled)
            QuickCodeButton("JP", onCodeSelected, enabled)
            QuickCodeButton("GB", onCodeSelected, enabled)
            QuickCodeButton("DE", onCodeSelected, enabled)
        }
    }
}

@Composable
private fun RowScope.QuickCodeButton(
    code: String,
    onCodeSelected: (String) -> Unit,
    enabled: Boolean
) {
    TextButton(
        onClick = { onCodeSelected(code) },
        modifier = Modifier.weight(1f),
        enabled = enabled
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
