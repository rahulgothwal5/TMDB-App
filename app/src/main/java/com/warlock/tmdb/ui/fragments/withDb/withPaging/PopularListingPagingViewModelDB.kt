package com.warlock.tmdb.ui.fragments.withDb.withPaging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.util.network.Listing
import com.warlock.tmdb.ui.fragments.withDb.DbRepo
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.PopularListingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


class PopularListingPagingViewModelDB(val repo: DbRepo, TMDBApi: TMDBApi) :
    BaseViewModel() {
    var query = MutableLiveData<String>()
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
            (repo.serachMovies(it)).asFlow()
        }.asLiveData()

    var data = ArrayList<MovieResult>()

    val result = repo.getListingFromDataBase()
    var pagedList = result.pagedList

    fun clearQuery() {
        query.value = ""
    }
}