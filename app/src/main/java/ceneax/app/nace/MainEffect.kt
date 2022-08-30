package ceneax.app.nace

import ceneax.app.lib.nace.core.INaceState
import ceneax.app.lib.nace.core.NaceEffect
import ceneax.app.lib.nace.core.obs

data class MainState(
    val title: String = ""
) : INaceState

class MainEffect : NaceEffect() {
    var test by obs(1)

    override fun onInit() {
        super.onInit()
        test ++
    }

    fun test2() {
        logger("MainEffect test()")
    }
}