package com.bagmanovam.rikiandmorti.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RikMortiEntity::class
    ], version = 1, exportSchema = false
)
abstract class RikMortiDatabase : RoomDatabase() {
    abstract fun getDao(): RikMoritDao
}