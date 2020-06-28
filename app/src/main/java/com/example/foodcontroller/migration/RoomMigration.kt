package com.example.foodcontroller.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class RoomMigration {
    val MIGRATION_1_2 =
        object :
            Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE product_table ADD COLUMN id INTEGER DEFAULT 0 NOT NULL")
            }
    }
}