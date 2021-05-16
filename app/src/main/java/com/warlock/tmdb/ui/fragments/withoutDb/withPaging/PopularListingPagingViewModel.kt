package com.warlock.tmdb.ui.fragments.withoutDb.withPaging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.PopularListingData
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.ui.fragments.PopularListingRepo
import com.warlock.tmdb.util.network.Listing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


class PopularListingPagingViewModel(val repo: PopularListingRepo, TMDBApi: TMDBApi) :
    BaseViewModel() {
    var query = MutableLiveData<String>("")
    var response: MutableLiveData<Listing<PopularListingData>>? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    val repoResult = query.asFlow()
        .debounce(1000)
        .filter {
            it.length > 2
        }
        .distinctUntilChanged()
        .flatMapLatest {
            (repo.searchMovies(it)).asFlow()
        }.asLiveData()

    var data = ArrayList<MovieResult>()
    val result = MutableLiveData<Listing<MovieResult>>(repo.getMoviePaged())
    var pagedList = result.switchMap { it.pagedList }
    val refreshState = result.switchMap { it.refreshState }
    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun clearQuery() {
        query.value = ""
    }

}