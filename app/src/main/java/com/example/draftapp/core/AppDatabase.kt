package com.example.draftapp.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.draftapp.feature.home.data.Draft
import com.example.draftapp.feature.home.data.DraftDao

@Database(entities = [Draft::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun draftDao(): DraftDao
}
