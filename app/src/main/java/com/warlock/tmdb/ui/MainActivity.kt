package com.warlock.tmdb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.warlock.tmdb.R
import com.warlock.tmdb.data.db.entity.GenreX
import com.warlock.tmdb.databinding.ActivityMainBinding
import com.warlock.tmdb.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var appToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        appToolbar = mBinding.toolbar
        setUpNavController()
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
    }

    private fun setUpNavController() {
        val navController = findNavController(R.id.mainNavHost)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        mBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    mBinding.toolbar.visibility = View.VISIBLE
                    mBinding.toolbar.title = destination.label
                }
                else -> {
                    mBinding.toolbar.visibility = View.GONE
                }
            }
        }
    }

    fun getToolbar() = appToolbar

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavHost).navigateUp()

    companion object {
        val genreList = HashMap<Int,String>()
    }
}
