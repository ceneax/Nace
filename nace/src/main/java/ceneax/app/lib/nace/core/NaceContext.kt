package ceneax.app.lib.nace.core

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

data class NaceContext(
    val activity: FragmentActivity,
    val fragmentManager: FragmentManager
)