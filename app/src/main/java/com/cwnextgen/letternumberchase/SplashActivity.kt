package com.cwnextgen.letternumberchase

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.cwnextgen.letternumberchase.databinding.ActivitySplashBinding
import com.network.base.BaseActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OptionsActivity::class.java))
            finish()
        }, 2500)

    }

    override fun clicks() {

    }

}