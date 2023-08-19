package com.cwnextgen.letternumberchase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cwnextgen.letternumberchase.databinding.ActivitySplashBinding
import com.network.base.BaseActivity

class SplashActivity : BaseActivity() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)


    }

    override fun onCreate() {

    }

    override fun clicks() {

    }
}