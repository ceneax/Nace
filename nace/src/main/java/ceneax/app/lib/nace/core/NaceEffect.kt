package ceneax.app.lib.nace.core

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import ceneax.app.lib.nace.NLog
import ceneax.app.lib.nace.Nace
import ceneax.app.lib.nace.NaceInternalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class NaceEffect @JvmOverloads constructor(
    val context: INaceContext = EmptyNaceContext
) : ViewModel() {
    val <T : Any> T.obs
        get() = NaceObs<NaceEffect, T>(this)

    @NaceInternalApi
    val invalidater = NaceInvalidater(true)
    @NaceInternalApi
    val listInvalidater = NaceInvalidater(emptyList<Any>())

    @OptIn(NaceInternalApi::class)
    protected val list get() = listInvalidater.value

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
    val lazyEffect = ViewModelLazy(
        E::class, { viewModelStore },
        Nace.viewModelProviderFactory ?: {
            DefaultNaceViewModelProviderFactory(when (this) {
                is FragmentActivity -> NaceContext(this, supportFragmentManager)
                is Fragment -> NaceContext(requireActivity(), parentFragmentManager)
                else -> error("必须在 FragmentActivity 或 Fragment 中使用 Nace")
            })
        })

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

fun NaceEffect.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(context, start, block)