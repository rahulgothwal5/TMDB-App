package com.warlock.tmdb.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import retrofit2.Call

/**
 * Base class for loading pages of snapshot data into a {@link PagedList}.
 */
abstract class DataSourceFactory<Value> : DataSource.Factory<Int, Value>() {
    val sourceLiveData = MutableLiveData<BasePageKeyedDataSource<Value>>()
    override fun create(): DataSource<Int, Value> {

        val source = object : BasePageKeyedDataSource<Value>() {
            override fun makeCall(page: Int, pageSize: Int): Call<BaseListingResponse<Value>> {
                return this@DataSourceFactory.makeCall(page, pageSize)
            }

        }
        sourceLiveData.postValue(source)
        return source
    }

    abstract fun makeCall(page: Int, pageSize: Int): Call<BaseListingResponse<Value>>
}


