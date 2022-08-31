package ceneax.app.lib.nace.core

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

sealed interface INaceContext {
    val activity: FragmentActivity
    val fragmentManager: FragmentManager
}

data class NaceContext(
    override val activity: FragmentActivity,
    override val fragmentManager: FragmentManager
) : INaceContext

internal object EmptyNaceContext : INaceContext {
    override val activity: FragmentActivity = error("NaceContext activity 尚未初始化")
    override val fragmentManager: FragmentManager = error("NaceContext fragmentManager 尚未初始化")
}