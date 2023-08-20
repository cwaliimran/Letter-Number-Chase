package com.cwnextgen.letternumberchase

import androidx.core.content.res.ResourcesCompat
import com.cwnextgen.letternumberchase.databinding.ActivityTableBinding
import com.cwnextgen.letternumberchase.models.TableItem
import com.cwnextgen.letternumberchase.utils.GlobalUtils.generateMultiplicationTable
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants

class TableActivity : BaseActivity() {
    private lateinit var binding: ActivityTableBinding
    private val TAG = ActivityTableBinding::class.java.simpleName
    private lateinit var adapter: TableAdapter

    private var mData = mutableListOf<String>()
    private var tableNo = 1
    private var tableData = mutableListOf<TableItem>()
    override fun onCreate() {
        binding = ActivityTableBinding.inflate(layoutInflater)
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

        binding.tvTitle.text = "Table Of -> " + bundle?.getString(AppConstants.BUNDLE)
        tableNo = bundle?.getString(AppConstants.BUNDLE)?.toInt() ?: 1

    }


    override fun clicks() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        val tableCount = AppClass.sharedPref.getInt(AppConstants.TABLE_COUNT, 10)

        tableData = generateMultiplicationTable(tableNo, tableCount)

    }

    override fun initAdapter() {
        adapter = TableAdapter()
        binding.recyclerView.adapter = adapter

        adapter.updateData(tableData)

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