package com.my.search.common

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class SearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}