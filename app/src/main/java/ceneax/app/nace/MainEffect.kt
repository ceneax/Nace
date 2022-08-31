package ceneax.app.nace

import ceneax.app.lib.nace.core.INaceState
import ceneax.app.lib.nace.core.NaceContext
import ceneax.app.lib.nace.core.NaceEffect
import ceneax.app.lib.nace.core.obs

data class MainState(
    val title: String = ""
) : INaceState

class MainEffect(context: NaceContext) : NaceEffect(context) {
    var test by obs(1)

    override fun onInit() {
        super.onInit()
    }

    fun test2() {
    }
}