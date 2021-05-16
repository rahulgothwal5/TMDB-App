package com.warlock.tmdb.util.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warlock.tmdb.util.PagingRequestHelper
import com.warlock.tmdb.util.network.Status


private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<Status> {
    val liveData = MutableLiveData<Status>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(Status.RUNNING)
            report.hasError() -> liveData.postValue(Status.FAILED)
            else -> liveData.postValue(Status.SUCCESS)
        }
    }
    return liveData
}
