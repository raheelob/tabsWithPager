package com.cookpad.hiring.android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.local.converter.CollectionConverter

@Database(entities = [CollectionEntity::class], version = 1, exportSchema = false)
@TypeConverters(CollectionConverter::class)
abstract class RecipeDatabase : RoomDatabase(){
    abstract fun RecipeDao(): RecipeDao

    companion object{
        @Volatile
        private var INSTANCE : RecipeDatabase? = null
        private const val DB_NAME = "recipe_database.db"
        fun getInstance(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}