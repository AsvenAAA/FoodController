package com.example.foodcontroller.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodcontroller.migration.RoomMigration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(ProductEntity::class), version = 2)
abstract class ProductDatabase : RoomDatabase() {

    private class ProducatDatabseCallBack(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var productDao = database.getProductDao()
                }
            }
        }
    }


    abstract fun getProductDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null
        //private val pathToDatabase = context.getDatabasePath("product_table").absolutePath

        fun getDatabase(context: Context, scope: CoroutineScope): ProductDatabase {
            val pathToDatabase = context.getDatabasePath("product_table").absolutePath
            //val roomMigration : RoomMigration = RoomMigration()
            val savedDatabaseInstance = INSTANCE
            if(savedDatabaseInstance != null) {
                return savedDatabaseInstance
            }

            return INSTANCE ?: synchronized(this) {
                val instanceOfDatabase = Room.databaseBuilder(
                    context,
                    ProductDatabase::class.java,
                    "product_table")
                    //.addMigrations(roomMigration.MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .addCallback(ProducatDatabseCallBack(scope))
                    .build()
                INSTANCE = instanceOfDatabase
                instanceOfDatabase
            }
        }
    }
}

/*
synchronized(this) {
    val instanceOfDatabase = Room.databaseBuilder(
        context.applicationContext,
        ProductDatabase::class.java,
        "product_table").build()
    INSTANCE = instanceOfDatabase
    return instanceOfDatabase
}*/
