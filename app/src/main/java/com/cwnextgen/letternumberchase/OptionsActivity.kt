package com.cwnextgen.letternumberchase

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.cwnextgen.letternumberchase.databinding.ActivityOptionsBinding
import com.cwnextgen.letternumberchase.utils.GlobalUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants

class OptionsActivity : BaseActivity() {
    private lateinit var binding: ActivityOptionsBinding
    private val TAG = ActivityOptionsBinding::class.java.simpleName
    override fun onCreate() {
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //load ad
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        val fontResourceId = this.resources.getIdentifier(
            selectedOptionFont, "font", this.packageName
        )
        val customFont = ResourcesCompat.getFont(this, fontResourceId)
        binding.tvTitle.typeface = customFont


    }


    override fun clicks() {

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)

            // Define the animation options (fade-in and fade-out)
            val animationOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, Pair.create(
                    binding.btnSettings, ViewCompat.getTransitionName(binding.btnSettings)!!
                )
                // You can add more pairs for other views if needed
            )

            // Start the activity with animation options
            startActivity(intent, animationOptions.toBundle())

        }
        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnTables.setOnClickListener {
            startActivity(Intent(this, TablesListActivity::class.java))
        }

    }


    override fun onResume() {
        super.onResume()

        binding.adView.resume()
    }

    public override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    public override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }
}