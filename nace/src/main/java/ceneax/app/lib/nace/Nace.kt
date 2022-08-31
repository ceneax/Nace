package ceneax.app.lib.nace

import android.app.Application
import ceneax.app.lib.nace.core.INaceViewModelProviderFactory

internal typealias ViewModelProviderFactory = () -> INaceViewModelProviderFactory

object Nace {
    private lateinit var _application: Application
    internal val application get() = _application

    private var _viewModelProviderFactory: ViewModelProviderFactory? = null
    val viewModelProviderFactory get() = _viewModelProviderFactory

    internal fun init(app: Application) {
        NLog.d("Nace init")
        _application = app
    }

    fun config(factory: ViewModelProviderFactory? = null) {
        factory?.let {
            _viewModelProviderFactory = it
        }
    }
}