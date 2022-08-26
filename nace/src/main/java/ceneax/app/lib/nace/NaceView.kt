package ceneax.app.lib.nace

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

interface INaceView : LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    fun invalidate()
}