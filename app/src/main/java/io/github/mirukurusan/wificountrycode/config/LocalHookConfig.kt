package io.github.mirukurusan.wificountrycode.config

import android.content.SharedPreferences
import android.util.Log
import io.github.mirukurusan.wificountrycode.data.Configuration

internal object LocalHookConfig {
    const val TAG = "WifiCountryCodeConfLocal"

    @Volatile
    var config: Configuration = Configuration.default
        private set

    @Volatile
    private var lastJsonString: String? = null

    fun loadConfig(prefs: SharedPreferences) {
        val jsonString = prefs.getString(HookConfig.KEY_CONFIG, "") ?: ""
        if (jsonString == lastJsonString) {
            return
        }

        val newConfig = try {
            Configuration.fromJSON(jsonString)
        } catch (_: Exception) {
            Configuration.default
        }

        if (config != newConfig) {
            Log.d(TAG, "Config loaded/updated from RemotePreferences: $newConfig (raw string: '$jsonString')")
        }
        config = newConfig
        lastJsonString = jsonString
    }
}
