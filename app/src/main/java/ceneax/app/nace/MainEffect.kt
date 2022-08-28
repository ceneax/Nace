package ceneax.app.nace

import ceneax.app.lib.nace.INaceState
import ceneax.app.lib.nace.NaceEffect
import ceneax.app.lib.nace.obs

data class MainState(
    val title: String = ""
) : INaceState

class MainEffect : NaceEffect() {
    var test by obs(1)

    override fun onInit() {
        super.onInit()
//        test++
    }

    fun test2() {
        logger("MainEffect test()")
    }
}