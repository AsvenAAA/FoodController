package com.example.foodcontroller.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
class ProductEntity(
    @PrimaryKey @ColumnInfo(name = "product_name") val product : String,
                @ColumnInfo(name = "protein") val fat : Float,
                @ColumnInfo(name = "protein") val carbohydrates : Float,
                @ColumnInfo(name = "protein") val calories : Float

)