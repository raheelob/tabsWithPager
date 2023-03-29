package com.cookpad.hiring.android.data.api

import com.cookpad.hiring.android.data.model.ErrorData

sealed class RemoteData<out R> {
    data class Success<out T>(val value: T?) : RemoteData<T>()
    data class Error(val code: Int? = null, val error: ErrorData? = null) : RemoteData<Nothing>()
    object RemoteErrorByNetwork : RemoteData<Nothing>()
    object Loading : RemoteData<Nothing>()
}
