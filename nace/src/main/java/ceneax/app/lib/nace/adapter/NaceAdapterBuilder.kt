package ceneax.app.lib.nace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope

val RecyclerView.naceAdapter get() = adapter as NaceAdapter

inline fun <reified I : Any, reified VB : ViewBinding> RecyclerView.NaceAdapter(
    nestedScrollingEnabled: Boolean = true,
    layoutManager: RecyclerView.LayoutManager? = null,
    builder: ItemProviderBuilder<I, VB>.() -> Unit
) = NaceAdapter(nestedScrollingEnabled, layoutManager) {
    add(builder)
}

@JvmName("NaceAdapterRecyclerView")
inline fun RecyclerView.NaceAdapter(
    nestedScrollingEnabled: Boolean = true,
    layoutManager: RecyclerView.LayoutManager? = null,
    builder: NaceAdapterBuilder.() -> Unit
): NaceAdapter {
    isNestedScrollingEnabled = nestedScrollingEnabled
    if (this.layoutManager == null && layoutManager == null) {
        this.layoutManager = LinearLayoutManager(context)
    } else if (this.layoutManager == null) {
        this.layoutManager = layoutManager
    }
    adapter = (this.context as LifecycleOwner).NaceAdapter(builder)
    return naceAdapter
}

@JvmName("NaceAdapterSingle")
inline fun <reified I : Any, reified VB : ViewBinding> LifecycleOwner.NaceAdapter(
    builder: ItemProviderBuilder<I, VB>.() -> Unit
) = NaceAdapter(lifecycleScope, builder)

@JvmName("NaceAdapterSingle")
inline fun <reified I : Any, reified VB : ViewBinding> NaceAdapter(
    scope: CoroutineScope,
    builder: ItemProviderBuilder<I, VB>.() -> Unit
) = NaceAdapter(scope) {
    add(builder)
}

inline fun LifecycleOwner.NaceAdapter(
    builder: NaceAdapterBuilder.() -> Unit
) = NaceAdapter(lifecycleScope, builder)

inline fun NaceAdapter(
    scope: CoroutineScope,
    builder: NaceAdapterBuilder.() -> Unit
) = NaceAdapterBuilder(scope).apply(builder).build()

class NaceAdapterBuilder(val scope: CoroutineScope) {
    val adapter = NaceAdapter()

    fun build() = adapter

    inline fun <reified I : Any, reified VB : ViewBinding> add(
        block: ItemProviderBuilder<I, VB>.() -> Unit
    ) {
        val inflateMethod = VB::class.java.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return adapter.addItemConfig(
            I::class,
            ItemProviderBuilder<I, VB>(scope, inflateMethod).apply(block).build()
        )
    }
}