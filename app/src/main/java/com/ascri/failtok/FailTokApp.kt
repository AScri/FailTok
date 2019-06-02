package com.ascri.failtok

import com.ascri.failtok.utilities.di.AppComponent
import com.ascri.failtok.utilities.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class FailTokApp : DaggerApplication(){

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        return appComponent
    }

    companion object {
        lateinit var appComponent: AppComponent
        const val TAG = "FailTokApp"
    }
}