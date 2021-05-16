package com.warlock.tmdb.util.network

import com.google.gson.annotations.SerializedName


data class ApiErrorModel(
    @SerializedName("errorCode")
    var errorCode: String?,
    @SerializedName("errorKey")
    var errorKey: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("errorList")
    var errorList: ArrayList<KeyValuePair>?
)


class ErrorResponse {
    @SerializedName("errorCode")
    var errorCode: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("errorList")
    var errorList: ArrayList<KeyValuePair>?= arrayListOf()
}


data class NetworkError(
    @SerializedName("errorMessage")
    val errorMessage: String = "No network available",
    @SerializedName("processId")
    val processId: Int
)


data class KeyValuePair(
    @SerializedName("error_code")
    val key: String,
    @SerializedName("message")
    val value: String
)


