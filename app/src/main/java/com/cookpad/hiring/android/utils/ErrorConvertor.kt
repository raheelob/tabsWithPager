package com.cookpad.hiring.android.utils

import com.cookpad.hiring.android.data.model.ErrorData
import com.google.gson.Gson
import retrofit2.HttpException

object ErrorConvertor {
     fun parseErrorBody(throwable: HttpException): ErrorData? {
        return try {
            val errorJsonString = throwable.response()?.errorBody()?.string()
            return Gson().fromJson(errorJsonString, ErrorData::class.java)
        } catch (exception: Exception) {
            null
        }
    }
}