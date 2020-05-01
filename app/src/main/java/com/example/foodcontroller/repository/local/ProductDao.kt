package com.example.foodcontroller.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table ORDER by product_name ASC")
    fun getAllProducts() : LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productEntity: ProductEntity)

    @Query("DELETE FROM product_table")
    fun deleteAllProducts()

    @Update()
    suspend fun updateTarget(productEntity: ProductEntity)

    @Delete()
    suspend fun deleteTarget(productEntity: ProductEntity)

}