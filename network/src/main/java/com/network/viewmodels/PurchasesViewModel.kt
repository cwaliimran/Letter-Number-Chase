package com.network.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.network.models.InAppPurchase
import com.network.repositories.InAppPurchasesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchasesViewModel constructor(application: Application) : AndroidViewModel(application) {
    private val repository: InAppPurchasesRepo = InAppPurchasesRepo()

    val getPurchases: LiveData<List<InAppPurchase>>
        get() = repository.getPurchases

    fun addInAppPurchase(purchase: InAppPurchase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addInAppPurchase(purchase)
        }
    }

    fun updateInAppPurchase(purchase: InAppPurchase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateInAppPurchase(purchase)
        }
    }

    fun deleteInAppPurchase(purchase: InAppPurchase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteInAppPurchase(purchase)
        }
    }
    fun deleteAllInAppPurchase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPurchases()
        }
    }
}