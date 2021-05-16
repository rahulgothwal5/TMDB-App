package com.warlock.tmdb.ui.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.warlock.tmdb.ui.base.BaseFragment

abstract class BaseViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<BaseFragment<*, *>>()

    /**
     * use to add fragment to viewPager
     * @param fragment that want to add in viewPager adapter
     */
    fun addFragment(fragment: BaseFragment<*, *>) {
        fragmentList.add(fragment)
    }


    /**
     * use to get fragment
     * @param position from where you want to get fragment
     * @return BaseFragment<*, *>? fragment on position
     */
    fun getFragment(position: Int): BaseFragment<*, *>? {
        return (if ((position >= 0) && (position < fragmentList.size)) fragmentList[position] else null)
    }


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }


    override fun getCount(): Int {
        return fragmentList.size
    }

}