package com.cookpad.hiring.android.di

import android.content.Context
import com.cookpad.hiring.android.data.local.RecipeDao
import com.cookpad.hiring.android.data.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return RecipeDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: RecipeDatabase): RecipeDao {
        return appDatabase.RecipeDao()
    }

}
