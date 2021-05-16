package com.warlock.tmdb.ui.fragments.withoutDb.withoutPageing

import androidx.lifecycle.*
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.util.network.Resource
import com.warlock.tmdb.ui.fragments.PopularListingRepo
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.PopularListingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


class PopularListingViewModel(val repo: PopularListingRepo) : BaseViewModel() {
    var query = MutableLiveData<String>("")
    val page = 1
    var homeData = ArrayList<MovieResult>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    var repoResult = query.asFlow()
        .debounce(1000).filter {
            it.length > 2
        }.distinctUntilChanged().flatMapLatest {
            (repo.searchMovies(it)).asFlow()
        }.asLiveData()
    var data = ArrayList<MovieResult>()


    @FlowPreview
    @ExperimentalCoroutinesApi
    val posts = repoResult.switchMap {
        liveData { emit(it.data?.movieResults) }
    }
    val status = repoResult.switchMap { liveData { emit(it.status) } }

    fun getPopularMovies(page: Int): LiveData<Resource<PopularListingData?>> {
        return repo.getPopular(page)
    }

    fun refresh() {
        repo.getPopular(page)
    }

    fun clearQuery() {
        query.value = ""
    }

}