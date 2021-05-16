package com.warlock.tmdb.util.paging

import com.google.gson.annotations.SerializedName

/**
 * Base api response class listing api
 */
data class BaseListingResponse<T>(
    @SerializedName("results")
    val data: List<T>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_records")
    val totalRecords: Int
)

