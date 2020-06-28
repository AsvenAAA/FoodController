package com.example.foodcontroller.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
                @ColumnInfo(name = "product_name") var product : String,
                @ColumnInfo(name = "protein") var protein : Float,
                @ColumnInfo(name = "fat") var fat : Float,
                @ColumnInfo(name = "carbohydrates") var carbohydrates : Float,
                @ColumnInfo(name = "calories") var calories : Float

)