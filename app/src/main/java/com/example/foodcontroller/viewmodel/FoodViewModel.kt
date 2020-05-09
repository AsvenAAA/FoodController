package com.example.foodcontroller.viewmodel

import android.app.Application
import androidx.core.math.MathUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.foodcontroller.repository.Repository
import com.example.foodcontroller.repository.local.ProductDao
import com.example.foodcontroller.repository.local.ProductDatabase
import com.example.foodcontroller.repository.local.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allProducts: LiveData<List<ProductEntity>>

    init {
        val productDao = ProductDatabase.getDatabase(application, viewModelScope).getProductDao()
        repository = Repository(productDao)
        allProducts = repository.allProducts
    }

    fun insert(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(productEntity)
    }

    fun update(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(productEntity)
    }

    fun deleteTarget(productEntity: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTarget(productEntity)
    }
    //Почему для удаления всего использую корутины?
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}