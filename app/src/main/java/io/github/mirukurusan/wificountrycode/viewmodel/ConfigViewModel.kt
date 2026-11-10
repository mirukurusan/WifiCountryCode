package io.github.mirukurusan.wificountrycode.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.mirukurusan.wificountrycode.config.HookConfig
import io.github.mirukurusan.wificountrycode.data.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigViewModel(private val context: Context) : ViewModel() {

    val configState = State.default
    val saveSuccess = mutableStateOf(false)
    val moduleActive = mutableStateOf(false)

    init {
        HookConfig.initService(context) { active ->
            moduleActive.value = active
        }
    }

    // Load configuration
    fun loadConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            moduleActive.value = HookConfig.isModuleActive
            val config = HookConfig.get()
            configState.toState(config)
        }
    }

    // Save configuration
    fun saveConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val configuration = Configuration(configState)
            HookConfig.save(configuration)
            saveSuccess.value = true
        }
    }

    companion object {
        private lateinit var applicationContext: Context

        fun initialize(context: Context) {
            applicationContext = context.applicationContext
        }

        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConfigViewModel(applicationContext) as T
            }
        }
    }

    class State {
        val enabled = mutableStateOf(false)
        val countryCode = mutableStateOf(Configuration.DEFAULT_COUNTRY_CODE)

        fun toState(config: Configuration) {
            enabled.value = config.enabled
            countryCode.value = config.countryCode
        }

        companion object {
            val default = State()
        }
    }
}
