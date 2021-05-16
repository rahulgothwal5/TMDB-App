package com.warlock

import android.app.Application
import com.warlock.tmdb.data.AppContainer

class TMDBApp : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        application = this
        appContainer = AppContainer(this)
    }

    companion object {
        lateinit var application: TMDBApp
    }
}