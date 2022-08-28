package ceneax.app.lib.nace

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class NaceEffect : ViewModel() {
    @NaceInternalApi
    val invalidater = MutableStateFlow(true)

    @CallSuper
    open fun onInit() {
        NLog.d("${this::class.java.simpleName} onInit")
    }

    @CallSuper
    override fun onCleared() {
        NLog.d("${this::class.java.simpleName} onClear")
    }
}

@OptIn(NaceInternalApi::class)
inline fun <reified E : NaceEffect> INaceView.naceEffect(): Lazy<E> {
    val lazyEffect = ViewModelLazy(E::class, { viewModelStore },
        Nace.viewModelProviderFactory ?: { defaultViewModelProviderFactory })

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            lazyEffect.value.invalidater.onEach {
                invalidate()
            }.launchIn(lifecycleScope)

            lazyEffect.value.onInit()
        }
    })

    return lazyEffect
}