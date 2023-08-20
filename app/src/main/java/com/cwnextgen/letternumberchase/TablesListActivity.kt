package com.cwnextgen.letternumberchase

import android.content.Intent
import androidx.core.content.res.ResourcesCompat
import com.cwnextgen.letternumberchase.databinding.ActivityTablesListBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants

class TablesListActivity : BaseActivity() {
    private lateinit var binding: ActivityTablesListBinding
    private val TAG = ActivityTablesListBinding::class.java.simpleName
    private lateinit var adapter: TablesListAdapter

    private var mData = mutableListOf<String>()

    override fun onCreate() {
        binding = ActivityTablesListBinding.inflate(layoutInflater)
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
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
// Add numbers from 1 to 20 to the list
        for (i in 1..20) {
            mData.add(i.toString())
        }
    }

    override fun initAdapter() {
        adapter = TablesListAdapter { selectedOption ->
            startActivity(
                Intent(this, TableActivity::class.java).putExtra(
                    AppConstants.BUNDLE, selectedOption
                )
            )
        }
        binding.recyclerView.adapter = adapter

        adapter.updateData(mData)

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