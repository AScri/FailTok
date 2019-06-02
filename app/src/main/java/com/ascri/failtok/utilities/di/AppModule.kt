package com.ascri.failtok.utilities.di

import android.app.Application
import android.content.Context
import com.ascri.failtok.FailTokApp
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(application: FailTokApp): Context {
        return application.applicationContext
    }

    @Provides
    fun providesApplication(application: FailTokApp): Application {
        return application
    }
}