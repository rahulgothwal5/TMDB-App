package com.warlock.tmdb.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.warlock.tmdb.util.SingleLiveDataEvent

abstract class BaseViewModel : ViewModel() {
    private var loading: MutableLiveData<SingleLiveDataEvent<Boolean>> = MutableLiveData()
    var toast: MutableLiveData<SingleLiveDataEvent<String>> = MutableLiveData()
    var snackBar: MutableLiveData<SingleLiveDataEvent<String>> = MutableLiveData()

    fun getLoading() = loading

    fun setLoading(visible: Boolean) {
        loading.value = SingleLiveDataEvent(visible)
    }


}