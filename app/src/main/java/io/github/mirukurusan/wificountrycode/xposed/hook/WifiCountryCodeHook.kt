package io.github.mirukurusan.wificountrycode.xposed.hook

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import io.github.libxposed.api.XposedModule
import io.github.mirukurusan.wificountrycode.config.LocalHookConfig
import io.github.mirukurusan.wificountrycode.xposed.BaseHook
import java.lang.reflect.Method
import java.util.Collections

object WifiCountryCodeHook : BaseHook() {

    override val name: String = "WifiCountryCodeHook"
    private const val TAG = "WifiCountryCodeHook"

    private val hookedMethods: MutableSet<Method> = Collections.synchronizedSet(HashSet())

    @SuppressLint("PrivateApi")
    override fun init(module: XposedModule, classLoader: ClassLoader) {
        val remotePrefs = module.getRemotePreferences("wifi_country_code_config")
        Log.i(TAG, "Initializing WifiCountryCodeHook in system server")
        module.log(Log.INFO, TAG, "Initializing WifiCountryCodeHook in system server")

        // Attempt direct hook using available ClassLoaders
        hookWifiCountryCodeInClassLoader(module, classLoader, remotePrefs)
        runCatching {
            hookWifiCountryCodeInClassLoader(module, ClassLoader.getSystemClassLoader(), remotePrefs)
        }

        // Also hook SystemServiceManager.loadClassFromLoader as fallback for late-loaded WifiService
        runCatching {
            val systemServiceManagerClass = classLoader.loadClass("com.android.server.SystemServiceManager")
            val loadClassMethod = systemServiceManagerClass.getDeclaredMethod(
                "loadClassFromLoader",
                String::class.java,
                ClassLoader::class.java
            )

            module.hook(loadClassMethod).intercept { chain ->
                val clzName = chain.getArg(0) as String
                val cl = chain.getArg(1) as ClassLoader
                val result = chain.proceed()

                if (clzName.contains("WifiService")) {
                    Log.i(TAG, "SystemServiceManager loaded class $clzName")
                    module.log(Log.INFO, TAG, "SystemServiceManager loaded class $clzName")
                    hookWifiCountryCodeInClassLoader(module, cl, remotePrefs)
                }
                result
            }
        }.onFailure { e ->
            Log.e(TAG, "Failed to hook SystemServiceManager: ${e.message}", e)
            module.log(Log.ERROR, TAG, "Failed to hook SystemServiceManager: ${e.message}", e)
        }
    }

    private fun hookWifiCountryCodeInClassLoader(
        module: XposedModule,
        cl: ClassLoader,
        remotePrefs: SharedPreferences
    ) {
        runCatching {
            val wifiCountryCodeClass = cl.loadClass("com.android.server.wifi.WifiCountryCode")
            Log.i(TAG, "Found WifiCountryCode class in classloader: $cl")
            module.log(Log.INFO, TAG, "Found WifiCountryCode class in classloader: $cl")

            wifiCountryCodeClass.declaredMethods.forEach { method ->
                val methodName = method.name
                when (methodName) {
                    "setTelephonyCountryCode", "setOverrideCountryCode", "setDriverCountryCode", "updateCountryCode" -> {
                        if (hookedMethods.add(method)) {
                            module.hook(method).intercept { chain ->
                                LocalHookConfig.loadConfig(remotePrefs)
                                val config = LocalHookConfig.config
                                if (!config.enabled) {
                                    return@intercept chain.proceed()
                                }
                                val targetCode = config.countryCode
                                val originalArg = if (chain.args.isNotEmpty()) chain.args[0] else null
                                Log.i(TAG, "Intercepted $methodName(original=$originalArg) -> forcing $targetCode")
                                module.log(Log.INFO, TAG, "Intercepted $methodName(original=$originalArg) -> forcing $targetCode")

                                val newArgs = chain.args.toTypedArray()
                                if (newArgs.isNotEmpty() && method.parameterTypes[0] == String::class.java) {
                                    newArgs[0] = targetCode
                                }
                                chain.proceed(newArgs)
                            }
                            Log.i(TAG, "Successfully hooked method: $methodName in $wifiCountryCodeClass")
                        }
                    }
                    "pickCountryCode", "getCountryCode", "getCurrentDriverCountryCode", "getDriverCountryCode", "getConnectingModeCountryCode" -> {
                        if (hookedMethods.add(method)) {
                            module.hook(method).intercept { chain ->
                                LocalHookConfig.loadConfig(remotePrefs)
                                val config = LocalHookConfig.config
                                if (!config.enabled) {
                                    return@intercept chain.proceed()
                                }
                                val targetCode = config.countryCode
                                Log.i(TAG, "Intercepted $methodName() -> returning $targetCode")
                                module.log(Log.INFO, TAG, "Intercepted $methodName() -> returning $targetCode")
                                targetCode
                            }
                            Log.i(TAG, "Successfully hooked method: $methodName in $wifiCountryCodeClass")
                        }
                    }
                }
            }
        }.onFailure { e ->
            Log.d(TAG, "WifiCountryCode class not found in classloader $cl: ${e.message}")
        }
    }
}
