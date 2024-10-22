package com.v7v.marvel.core

import android.app.Application

class MarvelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppApplication.init(application = this)
    }
}
