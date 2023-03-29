package com.cookpad.hiring.android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cookpad.hiring.android.data.entities.CollectionEntity

@Dao
interface RecipeDao {
    //Add a single recipe...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleCollection(singleRecipe: CollectionEntity)

    //Add all recipe...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCollection(list: List<CollectionEntity>)

    //Get all the recipe...
    @Query("Select * from CollectionEntity")
    fun getAllCollection(): List<CollectionEntity>

    @Query("Select * from CollectionEntity Where isFavorite")
    fun getFavoriteCollection(): List<CollectionEntity>
}