package com.zhboy.ycwwz.base_library.extend

import com.zhboy.ycwwz.base_library.utils.TLog
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class Weak<T : Any>(initializer: () -> T?) {
    var weakReference = WeakReference<T?>(initializer())

    constructor() : this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        TLog.i("Weak Delegate", "-----------getValue")
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        TLog.i("Weak Delegate", "-----------setValue")
        weakReference = WeakReference(value)
    }

}