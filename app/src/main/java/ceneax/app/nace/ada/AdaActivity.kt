package ceneax.app.nace.ada

import ceneax.app.lib.nace.adapter.NaceAdapter
import ceneax.app.lib.nace.adapter.bind
import ceneax.app.lib.nace.core.INaceView
import ceneax.app.lib.nace.core.naceEffect
import ceneax.app.nace.BaseActivity
import ceneax.app.nace.databinding.ActivityAdaBinding
import ceneax.app.nace.databinding.ItemOneBinding
import ceneax.app.nace.databinding.ItemTwoBinding

class AdaActivity : BaseActivity<ActivityAdaBinding>(), INaceView<AdaEffect> {
    override val effect by naceEffect()

//    override fun buildAdapter() = binding.recyclerView.NaceAdapter<Item, ItemOneBinding> {
//        bind { item ->
//            tvOne.text = item.one
//        }
//    }

    override fun buildAdapter() = binding.recyclerView.NaceAdapter {
        add<Item, ItemOneBinding> {
            bind { item ->
                tvOne.text = item.one
            }
        }

        add<Item2, ItemTwoBinding> {
            bind { item ->
                tvTwo.text = item.two
            }
        }
    }
}