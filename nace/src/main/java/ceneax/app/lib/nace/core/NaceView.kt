package ceneax.app.lib.nace.core

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import ceneax.app.lib.nace.adapter.NaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface INaceView<E : NaceEffect> : LifecycleOwner, ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory {
    val effect: E

    fun invalidate() {}

    fun buildAdapter(): NaceAdapter = NaceAdapter() // error("请在目标类中覆写此方法或移除覆写后的 super.buildAdapter()")
}

fun <E : NaceEffect> INaceView<E>.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch(context, start, block)