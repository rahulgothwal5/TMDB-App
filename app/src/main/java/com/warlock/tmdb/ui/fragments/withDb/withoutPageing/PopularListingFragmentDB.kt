package com.warlock.tmdb.ui.fragments.withDb.withoutPageing

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.warlock.tmdb.BR
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.FragmentPopularListingDbBinding
import com.warlock.tmdb.ui.base.BaseFragment
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.ui.fragments.withDb.DbRepo
import com.warlock.tmdb.ui.fragments.withoutDb.adapter.SearchAdapterBaseList
import com.warlock.tmdb.util.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi

class PopularListingFragmentDB :
    BaseFragment<FragmentPopularListingDbBinding, PopularListingViewModelDB>() {
    var page = 1
    private var adapterBaseList: SearchAdapterBaseList? = null
    private lateinit var mBinding: FragmentPopularListingDbBinding

    private val popularVM: PopularListingViewModelDB by lazy {
        ViewModelProvider(
            this,
            getViewModelFactory(
                PopularListingViewModelDB::class.java,
                PopularListingViewModelDB(
                    repo = DbRepo(
                        getAppContainer().tmdbApi, getAppDatabase().movieDao()
                    )
                )
            )
        ).get(PopularListingViewModelDB::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        setAdapter()
        setObservers()
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            if (popularVM.query.value.isNullOrEmpty()) {
                fetchPopularMovies()
            } else {
                mBinding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun fetchPopularMovies() {

        popularVM.repo.getPopular(page++).observe(viewLifecycleOwner, Observer {
            if (it.status.isFailed()) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                AppLog.d(it.error?.message ?: "")
            } else if (it.status.isLoading()) {
                mBinding.swipeRefreshLayout.isRefreshing = true
            } else if (it.status.isSuccessful()) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                popularVM.homeData.clear()
                popularVM.homeData.addAll(it.data!!)
                adapterBaseList?.submitList(popularVM.homeData)
            }
        })
    }

    private fun setAdapter() {
        adapterBaseList = SearchAdapterBaseList(popularVM.data) {
            navigateToFragment(
                target = R.id.infoScreenFragment, bundle = bundleOf(
                    Constant.MOVIE_ID to it.id
                )
            )
        }
        mBinding.rc.adapter = adapterBaseList
    }

    @ExperimentalCoroutinesApi
    private fun setObservers() {
        popularVM.repoResult.observe(viewLifecycleOwner, Observer {
            if (it.status.isFailed()) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                AppLog.d(it.error?.message ?: "")
            } else if (it.status.isLoading()) {
                mBinding.swipeRefreshLayout.isRefreshing = true
            } else if (it.status.isSuccessful()) {
                mBinding.swipeRefreshLayout.isRefreshing = false
                popularVM.data.clear()
                popularVM.data.addAll(it.data!!)
                adapterBaseList?.submitList(popularVM.data)
            }
        })
        fetchPopularMovies()
        popularVM.query.observe(viewLifecycleOwner, Observer {
            if (it.length < 3) {
                adapterBaseList?.submitList(popularVM.homeData)
            }
        })
    }

    override fun getLayoutId() = R.layout.fragment_popular_listing_db

    override fun getBindingVariable() = BR.popularListingDBVM

    override fun getViewModel() = popularVM


}
