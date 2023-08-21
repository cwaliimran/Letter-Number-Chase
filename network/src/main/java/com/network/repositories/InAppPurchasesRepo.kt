package com.network.repositories

import androidx.lifecycle.LiveData
import com.network.models.InAppPurchase
import com.network.network.ApiClient
import com.network.network.ApiInterface
import com.network.network.BaseApiResponse
import com.network.roomdb.AppDatabase
import com.network.utils.AppClass


class InAppPurchasesRepo : BaseApiResponse() {
    private val apiService: ApiInterface by lazy {
        ApiClient.getInstance()!!.create(ApiInterface::class.java)
    }

    val daoPurchases = AppDatabase.getDatabase(AppClass.instance).purchasesDao()

    val getPurchases: LiveData<List<InAppPurchase>> = daoPurchases.getPurchases()

    suspend fun addInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.addPurchase(purchase)
    }

    suspend fun updateInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.updatePurchase(purchase)
    }

    suspend fun deleteInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.deletePurchase(purchase)
    }

    suspend fun deleteAllPurchases() {
        daoPurchases.deleteAllPurchases()
    }

}


