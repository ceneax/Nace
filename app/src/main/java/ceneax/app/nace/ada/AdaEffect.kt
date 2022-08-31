package ceneax.app.nace.ada

import ceneax.app.lib.nace.core.NaceContext
import ceneax.app.lib.nace.core.NaceEffect
import ceneax.app.lib.nace.core.launch

data class Item(
    val one: String = ""
)

data class Item2(
    val two: String = ""
)

class AdaEffect : NaceEffect() {
    override fun onInit() {
        super.onInit()
        updateList(listOf(
            Item("one"),
            Item2("two"),
            Item("one1"),
            Item2("two1"),
            Item("one2"),
            Item2("two2"),
            Item("one3"),
            Item2("two3"),
        ))
    }

    private fun scope() = launch {
    }
}