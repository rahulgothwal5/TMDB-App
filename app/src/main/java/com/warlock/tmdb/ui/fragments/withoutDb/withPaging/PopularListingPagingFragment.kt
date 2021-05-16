package com.warlock.tmdb.ui.fragments.withoutDb.withPaging

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.warlock.tmdb.BR
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.FragmentPopularListingPagingBinding
import com.warlock.tmdb.ui.base.BaseFragment
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.util.network.NetworkState
import com.warlock.tmdb.ui.fragments.PopularListingRepo
import com.warlock.tmdb.ui.fragments.withoutDb.adapter.SearchAdapterBaseList
import com.warlock.tmdb.ui.fragments.withoutDb.adapter.SearchAdapterPaged
import com.warlock.tmdb.util.Constant.MOVIE_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi


class PopularListingPagingFragment :
    BaseFragment<FragmentPopularListingPagingBinding, PopularListingPagingViewModel>() {

    private var adapterBaseList: SearchAdapterPaged? = null
    private var searchAdapter: SearchAdapterBaseList? = null
    private lateinit var mBinding: FragmentPopularListingPagingBinding

    private val popularVM: PopularListingPagingViewModel by lazy {
        ViewModelProvider(
            this,
            getViewModelFactory(
                PopularListingPagingViewModel::class.java,
                PopularListingPagingViewModel(
                    repo = PopularListingRepo(
                        getAppContainer().tmdbApi
                    ), TMDBApi = getAppContainer().tmdbApi
                )
            )
        ).get(PopularListingPagingViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        setAdapter()
        setObservers()
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        popularVM.refreshState.observe(viewLifecycleOwner, Observer {
            mBinding.swipeRefreshLayout.isRefreshing = it == NetworkState.LOADING
//            getViewModel().loading.value = SingleLiveDataEvent(it.status.isLoading())
        })
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            popularVM.refresh()
        }
    }

    private fun setAdapter() {
        adapterBaseList =
            SearchAdapterPaged(listner = {
                navigateToFragment(
                    target = R.id.infoScreenFragment, bundle = bundleOf(
                        MOVIE_ID to it.id
                    )
                )
            })

        searchAdapter =
            SearchAdapterBaseList(popularVM.data) {
                navigateToFragment(
                    target = R.id.infoScreenFragment, bundle = bundleOf(
                        MOVIE_ID to it.id
                    )
                )
            }
        mBinding.rc.adapter = adapterBaseList
    }

    @ExperimentalCoroutinesApi
    private fun setObservers() {
        popularVM.pagedList.observe(this, Observer {
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
                popularVM.data.addAll(it.data!!.movieResults)
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

    override fun getLayoutId() = R.layout.fragment_popular_listing_paging

    override fun getBindingVariable() = BR.popularListingPageVM

    override fun getViewModel() = popularVM


}
