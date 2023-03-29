package com.cookpad.hiring.android.data.model

import com.google.gson.annotations.SerializedName

data class ErrorData(
    @SerializedName("ok") var ok: Boolean? = null,
    @SerializedName("error_code") var errorCode: Int? = null,
    @SerializedName("error") var error: String? = null
)