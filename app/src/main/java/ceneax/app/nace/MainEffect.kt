package ceneax.app.nace

import ceneax.app.lib.nace.core.INaceState
import ceneax.app.lib.nace.core.NaceContext
import ceneax.app.lib.nace.core.NaceEffect

data class MainState(
    val title: String = ""
) : INaceState {
    var a = ""
}

class MainEffect : NaceEffect() {
    var test by 1.obs

    override fun onInit() {
        super.onInit()
    }

    fun test2() {
    }
}