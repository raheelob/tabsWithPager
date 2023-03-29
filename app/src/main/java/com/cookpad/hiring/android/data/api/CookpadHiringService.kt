package com.cookpad.hiring.android.data.api

import com.cookpad.hiring.android.data.dtos.CollectionDTO
import com.cookpad.hiring.android.data.entities.CollectionEntity
import retrofit2.http.GET

interface CookpadHiringService {

    @GET("collections")
    suspend fun getCollections(): List<CollectionDTO>

}