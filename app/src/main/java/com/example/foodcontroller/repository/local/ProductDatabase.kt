package com.example.foodcontroller.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ProductEntity::class), version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {

            val savedDatabaseInstance = INSTANCE
            if(savedDatabaseInstance != null) {
                return savedDatabaseInstance
            }

            synchronized(this) {
                val instanceOfDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_table").build()
                INSTANCE = instanceOfDatabase
                return instanceOfDatabase
            }
        }

    }

}