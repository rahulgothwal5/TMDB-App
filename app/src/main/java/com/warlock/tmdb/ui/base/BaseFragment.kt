package com.warlock.tmdb.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.warlock.TMDBApp
import com.warlock.tmdb.BR
import com.warlock.tmdb.data.AppContainer
import com.warlock.tmdb.data.db.AppDatabase
import com.warlock.tmdb.databinding.LayoutProgressBinding
import com.warlock.tmdb.ui.MainActivity
import com.warlock.tmdb.util.NavigationUtility
import com.warlock.tmdb.util.SingleLiveObserver
import com.warlock.tmdb.util.viewmodel.ViewModelFactory
import kotlin.collections.set


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment(),
    View.OnClickListener {
    private lateinit var mViewDataBinding: T
    private lateinit var mProgressAndErrorView: LayoutProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<T>(inflater, getLayoutId(), container, false).apply {
        mViewDataBinding = this
        mViewDataBinding.setVariable(getBindingVariable(), getViewModel())
        mViewDataBinding.setVariable(BR.clickListener, this@BaseFragment)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.executePendingBindings()
        addProgressLayout()
    }.root

    /**
     * use to add loading view on fragment
     *  example :-
     *             getViewModel().loading.value = SingleLiveDataEvent(true) ->>>use to show loading
     *             getViewModel().loading.value = SingleLiveDataEvent(true) ->>>use to hide loading
     */
    private fun addProgressLayout() {
        mProgressAndErrorView = LayoutProgressBinding.inflate(LayoutInflater.from(requireContext()))
        mProgressAndErrorView.lifecycleOwner = viewLifecycleOwner
        (mViewDataBinding.root as ViewGroup).addView(
            mProgressAndErrorView.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        getViewModel().getLoading().observe(viewLifecycleOwner, SingleLiveObserver {
            mProgressAndErrorView.root.isVisible = it
        })
    }

    fun getToolbar() = (activity as MainActivity).getToolbar()

    /**
     * @method used to navigate from one fragment to other with an animation without bundle data
     * @param target is the id of fragment to be pushed into stack
     * @param popUpTo is nullable which is the id of fragment to which pop is required
     * @param isInclusivePop if this is true, popUpTo fragment will be included in pop process
     */
    fun navigateToFragment(
        target: Int,
        popUpTo: Int? = null,
        isInclusivePop: Boolean = false,
        bundle: Bundle? = null,
        applyAnimation: Boolean = true,
        showEnterAnim: Boolean = true
    ) {
        NavigationUtility.navigateToFragment(
            mViewDataBinding.root,
            target,
            bundle,
            popUpTo,
            isInclusivePop,
            applyAnimation,
            showEnterAnim
        )
    }

    /**
     * @method used to pop one fragment to back
     */
    fun popBack() {
        NavigationUtility.popBack(mViewDataBinding.root)
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set binding variable
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * Override for set view model
     * @return view model instance
     */
    abstract fun getViewModel(): V

    /**
     * Method for get View data binding
     */
    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    /**
     * Used to create ViewModelFactory so we can pass argument in viewModel constructor
     * @param classType Class<out ViewModel> class of ViewModel
     * @param viewModel ViewModel object of viewModel
     * @return ViewModelFactory
     */
    fun getViewModelFactory(
        classType: Class<out ViewModel>,
        viewModel: ViewModel
    ): ViewModelFactory {
        val arrayMap = ArrayMap<Class<out ViewModel>, ViewModel>()
        arrayMap[classType] = viewModel
        return ViewModelFactory(arrayMap)
    }

    /**
     * called when view clicked
     * @param v View that is clicked
     */
    override fun onClick(v: View?) {
    }

    open fun getAppContainer(): AppContainer =
        (TMDBApp.application).appContainer

    open fun getAppDatabase(): AppDatabase =
        (TMDBApp.application).appContainer.appDatabase
}
