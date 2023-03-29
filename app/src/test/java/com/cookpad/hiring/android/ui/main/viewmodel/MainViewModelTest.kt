package com.cookpad.hiring.android.ui.main.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cookpad.hiring.android.data.api.CookpadHiringService
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.local.RecipeDao
import com.cookpad.hiring.android.data.local.RecipeDatabase
import com.cookpad.hiring.android.data.model.ErrorData
import com.cookpad.hiring.android.data.repository.CollectionListRepository
import com.cookpad.hiring.android.data.repository.CollectionListRepositoryImpl
import com.cookpad.hiring.android.ui.favorite.event.FavoriteRecipeDataEvent
import com.cookpad.hiring.android.ui.main.usecase.LocalDbUseCase
import com.cookpad.hiring.android.ui.CoroutineTestRule
import com.cookpad.hiring.android.ui.recipecollection.event.CollectionRecipeDataEvent
import com.cookpad.hiring.android.ui.recipecollection.usecase.CollectionRecipeUseCase
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class MainViewModelTest {

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
    private val apiService = mock<CookpadHiringService>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var collectionListRepository: CollectionListRepository
    private lateinit var collectionRecipeUseCase: CollectionRecipeUseCase
    private lateinit var favoriteUseCase: LocalDbUseCase
    private lateinit var recipeDao: RecipeDao
    private lateinit var db: RecipeDatabase
    private lateinit var context: Context

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
      fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java).build()
        recipeDao = db.RecipeDao()
        collectionListRepository = CollectionListRepositoryImpl(apiService, recipeDao)
        collectionRecipeUseCase = CollectionRecipeUseCase(collectionListRepository)
        favoriteUseCase = LocalDbUseCase(collectionListRepository)
        mainViewModel = MainViewModel(collectionRecipeUseCase, favoriteUseCase)
    }

    @Test
    fun testDiscoverErrorEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.discoverCollectionTasksEvent.collect {
                when (it) {
                    is CollectionRecipeDataEvent.Error -> {
                        it.errorData.error?.let { errorStr -> assert(errorStr.isNotEmpty()) }
                    }
                    is CollectionRecipeDataEvent.Loading ->{}
                    is CollectionRecipeDataEvent.RecipeCollectionList ->{}
                    is CollectionRecipeDataEvent.RemoteErrorByNetwork ->{}
                    is CollectionRecipeDataEvent.FavoriteRemovedFromTheList -> {}
                    is CollectionRecipeDataEvent.UpdateCollectionList -> {}
                }
            }
        }
        mainViewModel.sendErrorEvent(
            ErrorData(
                ok = true,
                errorCode = 1,
                error = "Error Fetching lists"
            )
        )
        collectJob.cancel()
    }

    @Test
    fun sendFavoriteRemoveFromTheList() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.discoverCollectionTasksEvent.collect {
                when (it) {
                    is CollectionRecipeDataEvent.Error -> {}
                    is CollectionRecipeDataEvent.Loading ->{}
                    is CollectionRecipeDataEvent.RecipeCollectionList ->{}
                    is CollectionRecipeDataEvent.RemoteErrorByNetwork ->{}
                    is CollectionRecipeDataEvent.FavoriteRemovedFromTheList -> {
                        assert(it.collectionEntity.id != null)
                    }
                    is CollectionRecipeDataEvent.UpdateCollectionList -> {}
                }
            }
        }
        mainViewModel.sendFavoriteRemoveFromTheList(CollectionEntity(id = 6))
        collectJob.cancel()
    }

    @Test
    fun getCollectionListIsNotEmpty() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.discoverCollectionTasksEvent.collect {
                when (it) {
                    is CollectionRecipeDataEvent.Error -> {}
                    is CollectionRecipeDataEvent.Loading ->{}
                    is CollectionRecipeDataEvent.RecipeCollectionList ->{
                        assert(it.recipeCollectionList.isNotEmpty())
                    }
                    is CollectionRecipeDataEvent.RemoteErrorByNetwork ->{}
                    is CollectionRecipeDataEvent.FavoriteRemovedFromTheList -> {}
                    is CollectionRecipeDataEvent.UpdateCollectionList -> {}
                }
            }
        }
        mainViewModel.getCollectionListEvent(initCollectionList())
        collectJob.cancel()
    }

    @Test
    fun getCollectionListIsEmpty() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.discoverCollectionTasksEvent.collect {
                when (it) {
                    is CollectionRecipeDataEvent.Error -> {}
                    is CollectionRecipeDataEvent.Loading ->{}
                    is CollectionRecipeDataEvent.RecipeCollectionList ->{
                        assert(it.recipeCollectionList.isEmpty())
                    }
                    is CollectionRecipeDataEvent.RemoteErrorByNetwork ->{}
                    is CollectionRecipeDataEvent.FavoriteRemovedFromTheList -> {}
                    is CollectionRecipeDataEvent.UpdateCollectionList -> {}
                }
            }
        }
        mainViewModel.getCollectionListEvent(emptyList())
        collectJob.cancel()
    }

    @Test
    fun updateCollectionListIsNotEmpty() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.discoverCollectionTasksEvent.collect {
                when (it) {
                    is CollectionRecipeDataEvent.Error -> {}
                    is CollectionRecipeDataEvent.Loading ->{}
                    is CollectionRecipeDataEvent.RecipeCollectionList ->{}
                    is CollectionRecipeDataEvent.RemoteErrorByNetwork ->{}
                    is CollectionRecipeDataEvent.FavoriteRemovedFromTheList -> {}
                    is CollectionRecipeDataEvent.UpdateCollectionList -> {
                        assert(it.recipeCollectionList.isNotEmpty())
                    }
                }
            }
        }
        mainViewModel.updateCollectionListEvent(initCollectionList())
        collectJob.cancel()
    }

    @Test
    fun sendFavoriteList() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.favoriteTasksEvent.collect {
                when (it) {
                    is FavoriteRecipeDataEvent.FavoriteList -> {
                        assert(it.favoriteList.isNotEmpty())
                    }
                    is FavoriteRecipeDataEvent.UpdateFavoriteList -> {}
                }
            }
        }
        mainViewModel.sendFavoriteList(initFavoriteList())
        collectJob.cancel()
    }

    @Test
    fun sendUpdatedFavoriteList() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            mainViewModel.favoriteTasksEvent.collect {
                when (it) {
                    is FavoriteRecipeDataEvent.FavoriteList -> {
                        assert(it.favoriteList.isNotEmpty())
                    }
                    is FavoriteRecipeDataEvent.UpdateFavoriteList -> {}
                }
            }
        }
        mainViewModel.sendUpdatedFavoriteList(initFavoriteList())
        collectJob.cancel()
    }

    private fun initCollectionList() = object : ArrayList<CollectionEntity>() {
        init { // dummy list added to test DB...
            add(CollectionEntity())
            add(CollectionEntity())
            add(CollectionEntity())
        }
    }

    private fun initFavoriteList() = object : ArrayList<CollectionEntity>() {
        init { // dummy list added to test DB...
            add(CollectionEntity(isFavorite = true))
            add(CollectionEntity(isFavorite = true))
            add(CollectionEntity(isFavorite = true))
        }
    }




}