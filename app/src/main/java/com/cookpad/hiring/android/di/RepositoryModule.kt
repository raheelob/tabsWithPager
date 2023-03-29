package com.cookpad.hiring.android.di;

import com.cookpad.hiring.android.data.repository.CollectionListRepository
import com.cookpad.hiring.android.data.repository.CollectionListRepositoryImpl
import dagger.Binds
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindCollectionListRepository(collectionListRepositoryImpl: CollectionListRepositoryImpl): CollectionListRepository
}
