package com.warlock.tmdb.ui.fragments.infoScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.warlock.tmdb.data.db.entity.MovieDetailDataResponse
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.RelatedVideos
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.util.network.Listing

class InfoScreenViewModel(val repo: DetailInfoRepo, val movie_id: Int) : BaseViewModel() {
    var hasCastList = MutableLiveData<Boolean>(false)
    var hasDirectorList = MutableLiveData<Boolean>(false)
    var hasProducerList = MutableLiveData<Boolean>(false)
    var hasSimilarList = MutableLiveData<Boolean>(false)


    var hasVideoList = MutableLiveData<Boolean>(false)
    var movieDetail = MutableLiveData<MovieDetailDataResponse>()

    val result = MutableLiveData<Listing<MovieResult>>(repo.getMoviePaged(movie_id))
    var pagedList = result.switchMap { it.pagedList }
}