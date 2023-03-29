package com.cookpad.hiring.android.data.api

import com.cookpad.hiring.android.data.model.ErrorData
import com.cookpad.hiring.android.utils.ErrorConvertor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import okio.IOException
import retrofit2.HttpException

abstract class UseCaseExecutor<in P, out R>() {

    abstract fun runUseCase(parameter: P?): Flow<RemoteData<R>>

    fun execute(parameters: P?): Flow<RemoteData<R>> { // buffer so that there is no overflow from producer to consumer...
        return runUseCase(parameters).buffer().catch { e: Throwable ->
            when (e) {
                is IOException -> {
                    emit(RemoteData.RemoteErrorByNetwork)
                }
                is HttpException -> {
                    val code = e.code()
                    val errorResponse = ErrorConvertor.parseErrorBody(e)
                    emit(RemoteData.Error(code, errorResponse))
                }
                else -> {
                    emit(
                        RemoteData.Error(
                            -1,
                            ErrorData(ok = false, errorCode = 1, error = "Remote Error")
                        )
                    )
                }
            }
        }
    }

}