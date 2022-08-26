package ceneax.app.lib.nace

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel

open class NaceEffect : ViewModel(), INaceLifecycle {
    final override fun onCleared() {
        NLog.d("${this::class.java.simpleName} onDestroy")
        onDestroy()
    }

    @CallSuper
    override fun onCreated() {
        NLog.d("${this::class.java.simpleName} onCreated")
    }

    override fun onDestroy() {
    }
}