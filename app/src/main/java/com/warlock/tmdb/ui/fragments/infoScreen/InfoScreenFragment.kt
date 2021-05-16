package com.warlock.tmdb.ui.fragments.infoScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.warlock.tmdb.BR
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.FragmentInfoScreenBinding
import com.warlock.tmdb.ui.base.BaseFragment
import com.warlock.tmdb.ui.fragments.infoScreen.adapters.CastAdapter
import com.warlock.tmdb.ui.fragments.infoScreen.adapters.CrewAdapter
import com.warlock.tmdb.ui.fragments.infoScreen.adapters.VideoAdapter
import com.warlock.tmdb.ui.fragments.infoScreen.adapters.byPage.SimilarMovieAdapterPaged
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.util.Constant.MOVIE_ID
import com.warlock.tmdb.util.Toast


class InfoScreenFragment : BaseFragment<FragmentInfoScreenBinding, InfoScreenViewModel>() {

    private var movie_id: Int = 0
    private lateinit var mBinding: FragmentInfoScreenBinding

    private val infoVM by lazy {
        ViewModelProvider(
            this, getViewModelFactory(
                InfoScreenViewModel::class.java,
                InfoScreenViewModel(
                    DetailInfoRepo(getAppContainer().tmdbApi, getAppDatabase()),
                    arguments!!.getInt(MOVIE_ID)
                )
            )
        ).get(InfoScreenViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        movie_id = arguments!!.getInt(MOVIE_ID)
        setupInfoScreen()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
        }
    }

    override fun getLayoutId() = R.layout.fragment_info_screen

    override fun getBindingVariable() = BR.infoScreenVM

    override fun getViewModel() = infoVM

    private fun setupInfoScreen() {

        val castAdapter = CastAdapter(arrayListOf()) {}
        mBinding.rvCast.adapter = castAdapter

        val directorAdapter = CrewAdapter(arrayListOf()) {}
        mBinding.rvDirector.adapter = directorAdapter

        val producerAdapter = CrewAdapter(arrayListOf()) {}
        mBinding.rvProducer.adapter = producerAdapter

        val videoAdapter = VideoAdapter(arrayListOf()) {

            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=${it.key}")
                )
            )


        }
        mBinding.rvVideos.adapter = videoAdapter

        infoVM.repo.getMovieCreditList(movie_id).observe(
            viewLifecycleOwner, Observer {
                when {
                    it.status.isFailed() -> {
                        infoVM.setLoading(it.status.isLoading())
                        Toast.showSnackBar(requireActivity(), it.error?.message ?: "")
                    }
                    it.status.isLoading() -> {
                        infoVM.setLoading(it.status.isLoading())
                    }
                    it.status.isSuccessful() -> {
                        infoVM.setLoading(it.status.isLoading())
                        val castList = it.data?.cast?.filter { cast ->
                            cast.knownForDepartment.equals("Acting", true)
                        }
                        val directorList = it.data?.crew?.filter { crew ->
                            crew.job.equals("Director", true)
                        }
                        val producerList = it.data?.crew?.filter { crew ->
                            crew.job.equals("Producer", true)
                        }

                        if (castList.isNullOrEmpty().not())
                            infoVM.hasCastList.postValue(true)
                        if (producerList.isNullOrEmpty().not())
                            infoVM.hasProducerList.postValue(true)
                        if (directorList.isNullOrEmpty().not())
                            infoVM.hasDirectorList.postValue(true)


                        AppLog.d("CREDITS", "Cast Size: " + castList?.size)
                        AppLog.d("CREDITS", "Producer Size: " + producerList?.size)
                        AppLog.d("CREDITS", "Director Size: " + directorList?.size)


                        castAdapter.submitList(castList)
                        directorAdapter.submitList(directorList)
                        producerAdapter.submitList(producerList)

                    }
                }
            }
        )
        infoVM.repo.getMovieDetailsList(movie_id).observe(
            viewLifecycleOwner, Observer {
                when {
                    it.status.isFailed() -> {
                        infoVM.setLoading(it.status.isLoading())
                        Toast.showSnackBar(requireActivity(), it.error?.message ?: "")
                    }
                    it.status.isLoading() -> {
                        infoVM.setLoading(it.status.isLoading())
                    }
                    it.status.isSuccessful() -> {
                        infoVM.setLoading(it.status.isLoading())
                        infoVM.movieDetail.value = it.data
                    }
                }
            }
        )
        infoVM.repo.getMovieVideoList(movie_id).observe(
            viewLifecycleOwner, Observer {
                when {
                    it.status.isFailed() -> {
                        infoVM.setLoading(it.status.isLoading())
                        Toast.showSnackBar(requireActivity(), it.error?.message ?: "")
                    }
                    it.status.isLoading() -> {
                        infoVM.setLoading(it.status.isLoading())
                    }
                    it.status.isSuccessful() -> {
                        infoVM.setLoading(it.status.isLoading())
                        videoAdapter.submitList(it.data?.results)
                        if (it.data?.results.isNullOrEmpty().not())
                            infoVM.hasVideoList.postValue(true)
                    }
                }
            }
        )

        val adapterBaseList =
            SimilarMovieAdapterPaged(listner = {
            })
        mBinding.rvSimilar.adapter = adapterBaseList
        infoVM.pagedList.observe(viewLifecycleOwner, Observer {
            adapterBaseList.submitList(it)
            if (it.isNullOrEmpty().not())
                infoVM.hasSimilarList.postValue(true)
        })
    }
}