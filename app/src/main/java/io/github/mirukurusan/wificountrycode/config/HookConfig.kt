package io.github.mirukurusan.wificountrycode.config

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper
import io.github.mirukurusan.wificountrycode.ContextAmbient
import io.github.mirukurusan.wificountrycode.data.Configuration

/**
 * Hook configuration storage manager using LibXposed RemotePreferences
 */
object HookConfig {
    const val TAG = "WifiCountryCodeConf"
    const val PREF_NAME = "wifi_country_code_config"
    const val KEY_CONFIG = "config"

    @Volatile
    private var sharedService: XposedService? = null

    @Volatile
    var isModuleActive: Boolean = false
        private set

    private val sharedPref by lazy {
        ContextAmbient.current.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun initService(context: Context, onStateChanged: ((Boolean) -> Unit)? = null) {
        XposedServiceHelper.registerListener(object : XposedServiceHelper.OnServiceListener {
            override fun onServiceBind(service: XposedService) {
                sharedService = service
                isModuleActive = true
                Log.d(TAG, "Xposed service bound")
                syncToRemote()
                onStateChanged?.invoke(true)
            }

            override fun onServiceDied(service: XposedService) {
                sharedService = null
                isModuleActive = false
                Log.d(TAG, "Xposed service died")
                onStateChanged?.invoke(false)
            }
        })
    }

    fun syncToRemote(jsonStr: String? = null) {
        val service = sharedService ?: return
        val configStr = jsonStr ?: sharedPref.getString(KEY_CONFIG, "") ?: ""
        runCatching {
            val remotePrefs = service.getRemotePreferences(PREF_NAME)
            remotePrefs.edit(commit = true) { putString(KEY_CONFIG, configStr) }
            Log.d(TAG, "Synced config to remote preferences: $configStr")
        }.onFailure { e ->
            Log.e(TAG, "Failed to sync config to remote preferences", e)
        }
    }

    fun get(): Configuration {
        val configStr = sharedPref.getString(KEY_CONFIG, "") ?: ""
        return try {
            Configuration.fromJSON(configStr)
        } catch (e: Exception) {
            Configuration.default
        }
    }

    fun save(config: Configuration) {
        val json = config.toJSON()
        sharedPref.edit(commit = true) { putString(KEY_CONFIG, json) }
        Log.d(TAG, "Config saved locally: $json")
        syncToRemote(json)
    }
}
