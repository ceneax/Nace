package ceneax.app.nace

import ceneax.app.lib.nace.core.INaceView
import ceneax.app.lib.nace.core.naceEffect
import ceneax.app.lib.nace.core.obx
import ceneax.app.nace.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(), INaceView<MainEffect> {
    override val effect by naceEffect()

    override fun bindEvent() {
        binding.btInvalidate.setOnClickListener {
            effect.test ++
        }
    }

    override fun initObserver() {
        obx(effect::test) {
        }
    }

    override fun invalidate() {
    }
}