package ceneax.app.lib.nace.core

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import ceneax.app.lib.nace.adapter.NaceAdapter

interface INaceView<E : NaceEffect> : LifecycleOwner, ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory {
    val effect: E

    fun invalidate() {}

    fun buildAdapter(): NaceAdapter = NaceAdapter() // error("请在目标类中覆写此方法或移除覆写后的 super.buildAdapter()")
}