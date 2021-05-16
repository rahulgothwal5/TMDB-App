package com.warlock.tmdb.ui.fragments.withDb.withPaging

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.warlock.tmdb.BR
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.FragmentPopularListingPagingDbBinding
import com.warlock.tmdb.ui.base.BaseFragment
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.ui.fragments.withDb.DbRepo
import com.warlock.tmdb.ui.fragments.withoutDb.adapter.SearchAdapterBaseList
import com.warlock.tmdb.ui.fragments.withoutDb.adapter.SearchAdapterPaged
import com.warlock.tmdb.util.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi


class PopularListingPagingFragmentDB :
    BaseFragment<FragmentPopularListingPagingDbBinding, PopularListingPagingViewModelDB>() {

    private var adapterBaseList: SearchAdapterPaged? = null
    private var searchAdapter: SearchAdapterBaseList? = null
    private lateinit var mBinding: FragmentPopularListingPagingDbBinding

    private val popularVM: PopularListingPagingViewModelDB by lazy {
        ViewModelProvider(
            this,
            getViewModelFactory(
                PopularListingPagingViewModelDB::class.java,
                PopularListingPagingViewModelDB(
                    repo = DbRepo(
                        getAppContainer().tmdbApi,getAppDatabase().movieDao()
                    ), TMDBApi = getAppContainer().tmdbApi
                )
            )
        ).get(PopularListingPagingViewModelDB::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        setAdapter()
        setObservers()
    }

    private fun setAdapter() {
        adapterBaseList =
            SearchAdapterPaged(listner = {
                navigateToFragment(
                    target = R.id.infoScreenFragment, bundle = bundleOf(
                        Constant.MOVIE_ID to it.id
                    )
                )
            })

        searchAdapter =
            SearchAdapterBaseList(popularVM.data, listner = {
                navigateToFragment(
                    target = R.id.infoScreenFragment, bundle = bundleOf(
                        Constant.MOVIE_ID to it.id
                    )
                )
            })
        mBinding.rc.adapter = adapterBaseList
    }

    @ExperimentalCoroutinesApi
    private fun setObservers() {
        popularVM.pagedList.observe(viewLifecycleOwner, Observer {
            Log.i("countObserver","="+it.size)
            adapterBaseList?.submitList(it)
        })

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
                mBinding.rc.adapter = searchAdapter
                searchAdapter?.submitList(popularVM.data)
            }
        })
        popularVM.query.observe(viewLifecycleOwner, Observer {
            if (it.length < 3) {
                mBinding.rc.adapter = adapterBaseList
                adapterBaseList?.submitList(popularVM.pagedList.value)
            }
        })
    }

    override fun getLayoutId() = R.layout.fragment_popular_listing_paging_db

    override fun getBindingVariable() = BR.popularListingPageDBVM

    override fun getViewModel() = popularVM


}
