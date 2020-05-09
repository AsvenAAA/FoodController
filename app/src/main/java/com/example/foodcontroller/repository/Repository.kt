package com.example.foodcontroller.repository

import androidx.lifecycle.LiveData
import com.example.foodcontroller.repository.local.ProductDao
import com.example.foodcontroller.repository.local.ProductEntity

class Repository(private val productDao: ProductDao) {

    val allProducts : LiveData<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun update(productEntity: ProductEntity) {
        productDao.updateTarget(productEntity)
    }

    suspend fun insert(productEntity: ProductEntity) {
        productDao.insert(productEntity)
    }

    suspend fun deleteTarget(productEntity: ProductEntity) {
        productDao.deleteTarget(productEntity)
    }

    suspend fun deleteAll() {
        productDao.deleteAllProducts()
    }

}