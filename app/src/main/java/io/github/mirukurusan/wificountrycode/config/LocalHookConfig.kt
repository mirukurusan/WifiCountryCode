package io.github.mirukurusan.wificountrycode.config

import android.content.SharedPreferences
import android.util.Log
import io.github.mirukurusan.wificountrycode.data.Configuration

internal object LocalHookConfig {
    const val TAG = "WifiCountryCodeConfLocal"

    lateinit var config: Configuration
        private set

    fun loadConfig(prefs: SharedPreferences) {
        val jsonString = prefs.getString(HookConfig.KEY_CONFIG, "") ?: ""
        val newConfig = try {
            Configuration.fromJSON(jsonString)
        } catch (_: Exception) {
            Configuration.default
        }
        
        // 只有当配置发生改变，或者尚未初始化时，才打印日志，避免海量日志刷屏
        if (!this::config.isInitialized || config != newConfig) {
            Log.d(TAG, "Config loaded/updated from RemotePreferences: $newConfig (raw string: '$jsonString')")
        }
        config = newConfig
    }
}
