package ceneax.app.lib.nace.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface INaceViewModelProviderFactory : ViewModelProvider.Factory {
    val context: NaceContext
}

class DefaultNaceViewModelProviderFactory(
    override val context: NaceContext
) : INaceViewModelProviderFactory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NaceContext::class.java).newInstance(context)
    }
}