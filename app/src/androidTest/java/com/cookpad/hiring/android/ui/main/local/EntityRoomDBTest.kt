package com.cookpad.hiring.android.ui.main.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.local.RecipeDao
import com.cookpad.hiring.android.data.local.RecipeDatabase
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EntityRoomDBTest {

    private lateinit var recipeDao: RecipeDao
    private lateinit var db: RecipeDatabase
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java).build()
        recipeDao = db.RecipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addToFavorite() = runBlocking {
        recipeDao.insertSingleCollection(CollectionEntity(isFavorite = true))
        val collectionList = recipeDao.getAllCollection()
        assertTrue(collectionList[0].isFavorite)
    }

    @Test
    @Throws(Exception::class)
    fun insertAllCollection() = runBlocking {
        recipeDao.insertAllCollection(initCollectionList())
        val collectionList = recipeDao.getAllCollection()
        assertTrue(collectionList.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun removeFromFavorite() = runBlocking {
        recipeDao.insertSingleCollection(CollectionEntity(isFavorite = false))
        val collectionList = recipeDao.getAllCollection()
        assertTrue(!collectionList[0].isFavorite)
    }

    @Test
    @Throws(Exception::class)
    fun getFavoriteCollection() = runBlocking {
        recipeDao.insertAllCollection(initCollectionListWithFavorite())
        val collectionList = recipeDao.getFavoriteCollection()
        assertTrue(collectionList.isNotEmpty())
    }

    private fun initCollectionList() = object : ArrayList<CollectionEntity>() {
        init { // dummy list added to test DB...
            add(CollectionEntity())
            add(CollectionEntity())
            add(CollectionEntity())
        }
    }

    private fun initCollectionListWithFavorite() = object : ArrayList<CollectionEntity>() {
        init { // dummy list added to test DB with item as favorite...
            add(CollectionEntity(isFavorite = false))
            add(CollectionEntity(isFavorite = false))
            add(CollectionEntity(isFavorite = true))
        }
    }

}





