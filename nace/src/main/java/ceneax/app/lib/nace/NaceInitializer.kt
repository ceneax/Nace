package ceneax.app.lib.nace

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class NaceInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Nace.init(context as Application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}