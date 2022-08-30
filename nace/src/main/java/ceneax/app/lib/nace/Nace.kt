package ceneax.app.lib.nace

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import ceneax.app.lib.nace.NLog

internal typealias NaceViewModelProviderFactory = () -> ViewModelProvider.Factory

object Nace {
    private lateinit var _application: Application
    internal val application get() = _application

    private var _viewModelProviderFactory: NaceViewModelProviderFactory? = null
    val viewModelProviderFactory get() = _viewModelProviderFactory

    internal fun init(app: Application) {
        NLog.d("Nace init")
        _application = app
    }

    fun config(factory: NaceViewModelProviderFactory? = null) {
        factory?.let {
            _viewModelProviderFactory = it
        }
    }
}