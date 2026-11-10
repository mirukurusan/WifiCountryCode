package io.github.mirukurusan.wificountrycode.xposed

import android.util.Log
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam
import io.github.libxposed.api.XposedModuleInterface.SystemServerStartingParam
import io.github.mirukurusan.wificountrycode.xposed.hook.WifiCountryCodeHook

class HookEntry : XposedModule() {

    override fun onPackageReady(param: PackageReadyParam) {
    }

    override fun onSystemServerStarting(param: SystemServerStartingParam) {
        log(Log.INFO, TAG, "Package ready in system server (android)")
        Log.i(TAG, "Package ready in system server (android)")
        initHooks(param.classLoader, WifiCountryCodeHook)
    }

    private fun initHooks(classLoader: ClassLoader, vararg hooks: BaseHook) {
        for (h in hooks) {
            try {
                if (h.isInit) continue
                h.init(this, classLoader)
                h.isInit = true
            } catch (e: Exception) {
                log(Log.ERROR, TAG, "Failed to initialize hook: ${h.name}", e)
                Log.e(TAG, "Failed to initialize hook: ${h.name}", e)
            }
        }
    }

    companion object {
        private const val TAG = "WifiCountryCodeHookEntry"
    }
}
