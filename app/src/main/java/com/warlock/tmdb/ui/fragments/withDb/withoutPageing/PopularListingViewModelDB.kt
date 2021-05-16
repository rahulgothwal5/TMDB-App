package com.warlock.tmdb.ui.fragments.withDb.withoutPageing

import androidx.lifecycle.*
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.ui.fragments.withDb.DbRepo
import com.warlock.tmdb.data.db.entity.MovieResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


class PopularListingViewModelDB(val repo: DbRepo) : BaseViewModel() {
    var query = MutableLiveData<String>("")
    var homeData = ArrayList<MovieResult>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    var repoResult = query.asFlow()
        .debounce(1000).filter {
            it.length > 2
        }.distinctUntilChanged().flatMapLatest {
            (repo.serachMovies(it)).asFlow()
        }.asLiveData()
    var data = ArrayList<MovieResult>()

    @FlowPreview
    @ExperimentalCoroutinesApi
    val posts = repoResult.switchMap {
        liveData { emit(it.data) }
    }
    val status = repoResult.switchMap { liveData { emit(it.status) } }

    fun clearQuery() {
        query.value = ""
    }
}