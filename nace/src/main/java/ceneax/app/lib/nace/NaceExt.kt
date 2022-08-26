package ceneax.app.lib.nace

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelLazy

inline fun <reified S : INaceState> NaceEffect.naceState(): Lazy<S> =
    lazy(LazyThreadSafetyMode.NONE) {
        S::class.java.newInstance()
    }

inline fun <reified E : NaceEffect> INaceView.naceEffect(): Lazy<E> {
    val lazy = ViewModelLazy(E::class, { viewModelStore },
        Nace.viewModelProviderFactory ?: { defaultViewModelProviderFactory })

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            lazy.value.onCreated()
        }
    })

    return lazy
}