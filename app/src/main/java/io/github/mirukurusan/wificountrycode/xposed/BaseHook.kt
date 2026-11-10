package io.github.mirukurusan.wificountrycode.xposed

import io.github.libxposed.api.XposedModule

abstract class BaseHook {
    abstract fun init(module: XposedModule, classLoader: ClassLoader)
    abstract val name: String
    var isInit: Boolean = false
}
