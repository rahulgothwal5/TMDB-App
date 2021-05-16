package com.warlock.tmdb.ui.fragments.homeFragment

import androidx.lifecycle.MutableLiveData
import com.warlock.tmdb.ui.MainActivity.Companion.genreList
import com.warlock.tmdb.ui.base.BaseViewModel
import com.warlock.tmdb.ui.fragments.PopularListingRepo


class HomeViewModel(val repo: HomeRepo) : BaseViewModel() {
    var userName = MutableLiveData<String?>("Warlock")
}