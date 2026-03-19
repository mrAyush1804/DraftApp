package com.example.draftapp.core

import android.content.Context
import androidx.room.Room
import com.example.draftapp.feature.home.data.DraftDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "draft_database"
        ).build()
    }

    @Provides
    fun provideDraftDao(database: AppDatabase): DraftDao {
        return database.draftDao()
    }
}
