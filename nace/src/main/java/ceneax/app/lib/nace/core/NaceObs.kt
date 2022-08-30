package ceneax.app.lib.nace.core

import androidx.lifecycle.lifecycleScope
import ceneax.app.lib.nace.NLog
import ceneax.app.lib.nace.NaceInternalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

class NaceObs<E : NaceEffect, T>(initialValue: T) {
    private val value = MutableStateFlow(initialValue)
    private var mObx = false

    operator fun getValue(thisRef: E, property: KProperty<*>): T {
        return value.value
    }

    @OptIn(NaceInternalApi::class)
    operator fun setValue(thisRef: E, property: KProperty<*>, value: T) {
        if (this@NaceObs.value.tryEmit(value)) {
            if (!mObx) {
                thisRef.invalidater.tryUpdate(!thisRef.invalidater.value)
            }
        } else {
            NLog.e("NaceObs setValue: $value => 执行失败")
        }
    }

    fun obx(scope: CoroutineScope, block: (T) -> Unit) {
        mObx = true
        value.onEach(block).launchIn(scope)
    }
}

fun <E : NaceEffect, T> E.obs(value: T) = NaceObs<E, T>(value)

//val <T, E : NaceEffect> T.obs
//   get() = NaceObs<E, T>(this)

inline fun <reified E : NaceEffect, T> INaceView<E>.obx(
    prop: KProperty0<T>,
    crossinline block: () -> Unit
) {
    findDelegatingPropertyInstances(effect, NaceObs::class).find {
        it.property.name == prop.name
    }?.delegatingToInstance?.obx(lifecycleScope) {
        NLog.d("${this::class.java.simpleName} $${prop.name} obx")
        block()
    }
}