package com.warlock.tmdb.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.warlock.tmdb.util.network.ApiErrorModel
import com.warlock.tmdb.util.network.ErrorResponse
import com.warlock.tmdb.util.network.KeyValuePair
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set



object ErrorHandling {


    /**
     * @param response is the response of api in case of not success
     * @return returns a model (ApiErrorModel) having errorCode, message and list of errors
     */
    fun getApiErrorList(response: Response<*>): ApiErrorModel {

        val errorArray: ArrayList<KeyValuePair> = ArrayList()
        val apiErrorModel =
            ApiErrorModel("", "","", errorArray)

        try {
            when (response.code()) {
                Constant.HTTP_STATUS_UNAUTHORIZED,
                Constant.HTTP_STATUS_BAD_REQUEST,
                Constant.HTTP_STATUS_FORBIDDEN,
                Constant.HTTP_STATUS_TOO_MANY_REQUESTS,
                Constant.HTTP_STATUS_INTERNAL_SERVER_ERROR -> {
                    val gSon = Gson()
                    val adapter =
                        gSon.getAdapter<ErrorResponse>(
                            ErrorResponse::class.java
                        )
                    val errorBody = response.errorBody()?.string()
                    val errorParser = adapter.fromJson(errorBody)
                    if (null != errorBody) {
                        apiErrorModel.message = errorParser.message
                        apiErrorModel.errorKey = errorParser.errorCode
                        apiErrorModel.errorCode = response.code().toString()
                    }
                    val errorsList = errorParser.errorList
                    if (apiErrorModel.message.isNullOrEmpty() && errorsList.isNullOrEmpty().not()) {
                        apiErrorModel.errorKey = errorsList?.get(0)?.value
                        apiErrorModel.errorList = errorsList
                        apiErrorModel.message = errorsList?.get(0)?.value
                    }
                }

                Constant.HTTP_STATUS_UN_PROCESSABLE_ENTITY -> {
                    val error = response.errorBody()?.string()
                    if (null != response.errorBody()) {
                        val errors = parseErrorList(error)
                        apiErrorModel.errorList = errors
                        apiErrorModel.errorCode =
                            Constant.HTTP_STATUS_UN_PROCESSABLE_ENTITY.toString()
                        if (error.isNullOrEmpty().not()) {
                            apiErrorModel.message = errors?.get(0)?.value

                        }

                    } else {
                        apiErrorModel.message = response.message()
                    }
                }
                else -> {
                    apiErrorModel.message = response.message()
                }
            }
        } catch (e: IOException) {
            apiErrorModel.message = response.message()
            e.printStackTrace()
        }
        return apiErrorModel
    }

    private fun parseErrorList(errorBody: String?): ArrayList<KeyValuePair>? {
        val errorArray = ArrayList<KeyValuePair>()
        val array = Gson().fromJson(
            errorBody,
            LinkedTreeMap::class.java
        ).toList()
        if (array.isNotEmpty()) {
            for ((first, second) in array) {
                errorArray.add(
                    KeyValuePair(
                        first.toString(),
                        second.toString()
                    )
                )
            }
        }
        return errorArray
    }

    /**
     * @param t is the json string with key values
     * @return a map of String keys and String values
     */
    @Throws(JSONException::class)
    private fun jsonToMap(t: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        val jObject = JSONObject(t)
        val keys = jObject.keys()

        while (keys.hasNext()) {
            val key = keys.next() as String
            val value = jObject.getString(key)
            map[key] = value
        }
        return map
    }

    /**
     * @param keyValueJson is the json having key values only
     * @return a list of KeyValuePair
     */
    private fun convertToKeyValueList(keyValueJson: JsonElement): ArrayList<KeyValuePair> {
        val keyValueList: ArrayList<KeyValuePair> = ArrayList()
        if (keyValueJson is JsonObject) {
            val keyValueData = jsonToMap(keyValueJson.toString())
            for ((key, value) in keyValueData) {
                if (key != "")
                    keyValueList.add(
                        KeyValuePair(
                            key,
                            value
                        )
                    )
            }
        }
        return keyValueList
    }
}