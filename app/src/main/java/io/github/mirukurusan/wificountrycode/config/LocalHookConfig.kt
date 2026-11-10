package io.github.mirukurusan.wificountrycode.config

import android.content.SharedPreferences
import android.util.Log
import io.github.mirukurusan.wificountrycode.data.Configuration

internal object LocalHookConfig {
    const val TAG = "LocalHookConfig"

    lateinit var config: Configuration
        private set

    fun loadConfig(prefs: SharedPreferences) {
        Log.d(TAG, "Loading config from RemotePreferences")
        val jsonString = prefs.getString(HookConfig.KEY_CONFIG, "") ?: ""
        config = try {
            Configuration.fromJSON(jsonString)
        } catch (e: Exception) {
            Log.d(TAG, "Failed to read config: $jsonString, error: $e")
            Configuration.default
        }
        Log.d(TAG, "Config loaded: $config")
    }
}
