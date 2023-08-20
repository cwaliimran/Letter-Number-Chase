package com.cwnextgen.letternumberchase

import android.content.Intent
import com.cwnextgen.letternumberchase.databinding.ActivitySplashBinding
import com.network.base.BaseActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun clicks() {
        binding.root.setOnClickListener {
            startHome()
        }
        binding.btnPlay.setOnClickListener {
            startHome()
        }
    }

    private fun startHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}