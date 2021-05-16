package com.warlock.tmdb.util

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.warlock.tmdb.R


object NavigationUtility {

    /**
     * @method used to navigate from one fragment to other with an animation with bundle data
     * @param mViewDataBindingRoot navigator root view
     * @param target is the id of fragment to be pushed into stack
     * @param bundle bundle of data to be passed to nex fragment in arguments
     * @param popUpTo is nullable which is the id of fragment to which pop is required
     * @param isInclusivePop if this is true, popUpTo fragment will be included in pop process
     * @param applyAnimation should applu animation or not on transaction
     * @param showEnterAnim should show enter animation or not
     */
    fun navigateToFragment(
        mViewDataBindingRoot: View,
        target: Int,
        bundle: Bundle?,
        popUpTo: Int? = null,
        isInclusivePop: Boolean = false,
        applyAnimation: Boolean = true,
        showEnterAnim: Boolean = true
    ) {

        val navOptionBuilder = NavOptions.Builder()

        if (popUpTo != null) {
            navOptionBuilder.setPopUpTo(popUpTo, isInclusivePop)
        }
        if (applyAnimation)
            if (showEnterAnim) {
                applyAnimation(navOptionBuilder)
            } else {
                applyReverseAnimation(navOptionBuilder)
            }
        val navOptions = navOptionBuilder.build()

        try {
            Navigation.findNavController(mViewDataBindingRoot).navigate(
                target,
                bundle,
                navOptions
            )
        } catch (e: IllegalStateException) {
            AppLog.printStackTrace(e)
        }
    }

    /**
     * @method used to pop one fragment to back
     * @param mViewDataBindingRoot navigator root view
     */
    fun popBack(mViewDataBindingRoot: View) {
        Navigation.findNavController(mViewDataBindingRoot).popBackStack()
    }

    /**
     * @method used to apply animation in fragment navigation
     */
    private fun applyAnimation(navOptionBuilder: NavOptions.Builder) {
        navOptionBuilder.apply {
            setEnterAnim(R.anim.right_in)
            setExitAnim(R.anim.left_out)
            setPopExitAnim(R.anim.right_out)
            setPopEnterAnim(R.anim.left_in)
        }
    }

    /**
     * @method used to apply animation when current screen need to be popped out
     */
    private fun applyReverseAnimation(navOptionBuilder: NavOptions.Builder) {
        navOptionBuilder.apply {
            setEnterAnim(R.anim.left_in)
            setExitAnim(R.anim.left_out)
            setPopExitAnim(R.anim.right_out)
            setPopEnterAnim(R.anim.left_in)
        }
    }
}