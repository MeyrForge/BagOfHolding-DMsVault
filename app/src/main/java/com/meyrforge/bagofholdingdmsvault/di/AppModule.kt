package com.meyrforge.bagofholdingdmsvault.di

import android.content.Context
import androidx.room.Room
import com.meyrforge.bagofholdingdmsvault.common.data.BagOfHoldingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "bagofholding_database"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BagOfHoldingDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration(false)
            .build()
}