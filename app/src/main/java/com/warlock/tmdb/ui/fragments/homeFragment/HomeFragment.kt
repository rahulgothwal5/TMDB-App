package com.warlock.tmdb.ui.fragments.homeFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.warlock.tmdb.BR
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.FragmentHomeBinding
import com.warlock.tmdb.ui.MainActivity.Companion.genreList
import com.warlock.tmdb.ui.base.BaseFragment
import com.warlock.tmdb.util.AppLog


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private lateinit var mBinding: FragmentHomeBinding

    private val homeVM by lazy {
        ViewModelProvider(
            this, getViewModelFactory(
                HomeViewModel::class.java,
                HomeViewModel(HomeRepo(getAppContainer().tmdbApi, getAppDatabase()))
            )
        ).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        homeVM.repo.getGenreList().observe(
            viewLifecycleOwner, Observer {
                if (it.status.isSuccessful()) {
                    for (item in it.data!!)
                    {
                        genreList[item.id!!] = item.name?:""
                    }
                }
            }
        )
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            mBinding.btWithPaging.id -> {
                navigateToFragment(R.id.moviesWithPagingFragment)
            }
            mBinding.btWithoutPaging.id -> {
                navigateToFragment(R.id.moviesWithoutPagingFragment)
            }
            mBinding.btWithPagingDb.id -> {
                navigateToFragment(R.id.moviesWithPagingFragmentDB)
            }
            mBinding.btWithoutPagingDb.id -> {
                navigateToFragment(R.id.moviesWithoutPagingFragmentDB)
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun getBindingVariable() = BR.homeVM

    override fun getViewModel() = homeVM


}
