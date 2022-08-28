package ceneax.app.lib.nace

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

interface INaceView : LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    fun invalidate()
}

inline fun <reified E : NaceEffect, T> INaceView.obx(effect: E, prop: KProperty0<T>, crossinline block: () -> Unit) {
    findDelegatingPropertyInstances(effect, NaceObs::class).find {
        it.property.name == prop.name
    }?.delegatingToInstance?.obx {
        block()
    }
}