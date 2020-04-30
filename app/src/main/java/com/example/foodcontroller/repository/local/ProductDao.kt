package com.example.foodcontroller.repository.local

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table")
    fun getAllProducts() : List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productEntity: ProductEntity)

    @Query("DELETE FROM product_table")
    fun deleteAllProducts()

    @Update()
    fun updateTarget(productEntity: ProductEntity)

    @Delete()
    fun deleteTarget(productEntity: ProductEntity)

}