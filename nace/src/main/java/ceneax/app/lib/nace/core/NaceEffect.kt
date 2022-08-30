package ceneax.app.lib.nace.core

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.lifecycleScope
import ceneax.app.lib.nace.NLog
import ceneax.app.lib.nace.Nace
import ceneax.app.lib.nace.NaceInternalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class NaceEffect : ViewModel() {
    @NaceInternalApi
    val invalidater = NaceInvalidater(true)
    @NaceInternalApi
    val listInvalidater = NaceInvalidater(emptyList<Any>())

    @OptIn(NaceInternalApi::class)
    protected fun update() {
        invalidater.tryUpdate(!invalidater.value)
    }

    @OptIn(NaceInternalApi::class)
    protected fun updateList(newList: List<Any>) {
        listInvalidater.tryUpdate(newList)
    }

    @CallSuper
    open fun onInit() {
        NLog.d("${this::class.java.simpleName} onInit")
    }

    @CallSuper
    override fun onCleared() {
        NLog.d("${this::class.java.simpleName} onCleared")
    }
}

class NaceInvalidater<T>(initialValue: T) {
    private val mValue = MutableStateFlow(initialValue)

    internal val value: T get() = mValue.value

    internal fun tryUpdate(newVal: T) {
        mValue.tryEmit(newVal)
    }

    internal suspend fun update(newVal: T) {
        mValue.emit(newVal)
    }

    fun obs(scope: CoroutineScope, block: (T) -> Unit): Job {
        return mValue.onEach(block).launchIn(scope)
    }
}

@OptIn(NaceInternalApi::class)
inline fun <reified E : NaceEffect> INaceView<E>.naceEffect(): Lazy<E> {
    val lazyEffect = ViewModelLazy(E::class, { viewModelStore },
        Nace.viewModelProviderFactory ?: { defaultViewModelProviderFactory })

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            buildAdapter().also { adapter ->
                lazyEffect.value.listInvalidater.obs(lifecycleScope) {
                    NLog.d("${this::class.java.simpleName} invalidate list")
                    adapter.updateList(it)
                }
            }

            lazyEffect.value.invalidater.obs(lifecycleScope) {
                NLog.d("${this::class.java.simpleName} invalidate")
                invalidate()
            }

            lazyEffect.value.onInit()
        }
    })

    return lazyEffect
}