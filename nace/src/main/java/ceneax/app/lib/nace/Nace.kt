package ceneax.app.lib.nace

import android.app.Application
import androidx.lifecycle.ViewModelProvider

internal typealias NaceViewModelProviderFactory = () -> ViewModelProvider.Factory

object Nace {
    private lateinit var _application: Application
    internal val application get() = _application

    private var _viewModelProviderFactory: NaceViewModelProviderFactory? = null
    val viewModelProviderFactory get() = _viewModelProviderFactory

    internal fun init(app: Application) {
        _application = app
    }

    fun config(factory: NaceViewModelProviderFactory? = null) {
        factory?.let {
            _viewModelProviderFactory = it
        }
    }
}