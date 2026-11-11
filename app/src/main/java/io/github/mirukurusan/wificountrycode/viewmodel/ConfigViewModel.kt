package io.github.mirukurusan.wificountrycode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.mirukurusan.wificountrycode.config.HookConfig
import io.github.mirukurusan.wificountrycode.data.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConfigUiState(
    val config: Configuration = Configuration.default,
    val isModuleActive: Boolean = false,
    val saveSuccess: Boolean = false
)

class ConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    init {
        HookConfig.initService(getApplication()) { active ->
            _uiState.update { it.copy(isModuleActive = active) }
        }
    }

    // Load configuration
    fun loadConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val active = HookConfig.isModuleActive
            val config = HookConfig.get()
            _uiState.update {
                it.copy(
                    config = config,
                    isModuleActive = active
                )
            }
        }
    }

    // State update functions (Intents)
    fun updateEnabled(enabled: Boolean) {
        _uiState.update {
            it.copy(config = it.config.copy(enabled = enabled))
        }
    }

    fun updateCountryCode(code: String) {
        _uiState.update {
            it.copy(config = it.config.copy(countryCode = code.uppercase()))
        }
    }

    // Save configuration
    fun saveConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            HookConfig.save(_uiState.value.config)
            _uiState.update { it.copy(saveSuccess = true) }
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
