package ceneax.app.nace

import android.content.Intent
import ceneax.app.nace.ada.AdaActivity
import ceneax.app.nace.databinding.ActivityFirstBinding

class FirstActivity : BaseActivity<ActivityFirstBinding>() {
    override fun bindEvent() {
        binding.btToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btToAda.setOnClickListener {
            startActivity(Intent(this, AdaActivity::class.java))
        }
    }
}