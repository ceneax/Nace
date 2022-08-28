package ceneax.app.nace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import ceneax.app.lib.nace.INaceView
import ceneax.app.lib.nace.naceEffect
import ceneax.app.lib.nace.obx
import ceneax.app.nace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), INaceView {
    private val effect by naceEffect<MainEffect>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

//        effect.test()

        binding.btInvalidate.setOnClickListener {
            effect.test++
        }

        obx(effect, effect::test) {
            logger("obx()")
        }
    }

    override fun invalidate() {
//        logger("invalidate() effect.test = ${effect.test}")
        logger("invalidate()")
    }
}