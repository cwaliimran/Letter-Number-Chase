package com.cwnextgen.letternumberchase

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.cwnextgen.letternumberchase.databinding.ActivityOptionsBinding
import com.cwnextgen.letternumberchase.dialogs.coinsInfoDialog
import com.cwnextgen.letternumberchase.utils.GlobalUtils.showOptionsMenu
import com.cwnextgen.letternumberchase.utils.openPlayStoreForMoreApps
import com.cwnextgen.letternumberchase.utils.shareApp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants
import com.network.utils.openPlayStoreForRating
import com.network.utils.showToast

class OptionsActivity : BaseActivity() {
    private lateinit var binding: ActivityOptionsBinding
    private val TAG = ActivityOptionsBinding::class.java.simpleName
    override fun onCreate() {
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (BuildConfig.FLAVOR != "adfree") {
            //load ad
            MobileAds.initialize(this) {}
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        val fontResourceId = this.resources.getIdentifier(
            selectedOptionFont, "font", this.packageName
        )
        val customFont = ResourcesCompat.getFont(this, fontResourceId)
        binding.tvTitle.typeface = customFont


    }


    override fun clicks() {
        binding.btnOptions.setOnClickListener {
            context.showOptionsMenu(it, R.menu.main_menu) { item ->
                when (item.itemId) {
                    R.id.share -> {
                        this@OptionsActivity.shareApp()
                        return@showOptionsMenu true
                    }

                    R.id.menu_rate_app -> {
                        this@OptionsActivity.openPlayStoreForRating()
                        return@showOptionsMenu true
                    }

                    // Add logging to check if this case is reached
                    R.id.menu_more_apps -> {
                        Log.d("MenuClick", "More Apps clicked")
                        this@OptionsActivity.openPlayStoreForMoreApps()
                        return@showOptionsMenu true
                    }

                    R.id.menu_contact_us -> {
                        // Handle "Contact Us" menu item click
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            val subject = getString(R.string.app_name) + " Contact US"
                            intent.data = Uri.parse("mailto:princelumia143@gmail.com?subject=$subject")
                            startActivity(intent)
                        } catch (e: Exception) {
                            showToast("Contact us on princelumia143@gmail.com")
                        }
                        return@showOptionsMenu true
                    }

                    R.id.menu_privacy_policy -> {
                        // Handle "Privacy Policy" menu item click
                        val url = AppConstants.PRIVACY_POLICY_URL
                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(this@OptionsActivity, Uri.parse(url))
                        return@showOptionsMenu true
                    }

                    else -> {
                        // Add logging to check if this case is reached
                        Log.d("MenuClick", "Unknown menu item clicked")
                        return@showOptionsMenu false
                    }
                }
            }
        }

        binding.tvCoins.setOnClickListener {
            coinsInfoDialog()
        }
        binding.tvDiamonds.setOnClickListener {
            coinsInfoDialog()
        }

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
        binding.btnLettersChase.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).putExtra(
                    AppConstants.BUNDLE, "lettersGame"
                )
            )
        }

        binding.btnNumbersChase.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).putExtra(
                    AppConstants.BUNDLE, "numbersGame"
                )
            )
        }

        binding.btnTables.setOnClickListener {
            startActivity(Intent(this, TablesListActivity::class.java))
        }

    }


    override fun onResume() {
        super.onResume()
        binding.adView.resume()
        binding.tvCoins.text = AppClass.sharedPref.getLong(AppConstants.COINS).toString()
        binding.tvDiamonds.text = AppClass.sharedPref.getLong(AppConstants.DIAMONDS).toString()

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