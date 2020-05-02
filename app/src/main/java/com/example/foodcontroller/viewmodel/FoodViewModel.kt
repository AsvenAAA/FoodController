package com.example.foodcontroller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foodcontroller.repository.Repository
import com.example.foodcontroller.repository.local.ProductDao
import com.example.foodcontroller.repository.local.ProductDatabase
import com.example.foodcontroller.repository.local.ProductEntity

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allProducts: LiveData<List<ProductEntity>>

    init {
        val productDao = ProductDatabase.getDatabase(application).getProductDao()
        repository = Repository(productDao)
        allProducts = repository.allProducts
    }

    fun insert(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(productEntity)
    }
}