package com.ascri.failtok.utilities.di

import android.content.Context
import com.ascri.failtok.FailTokApp
import com.ascri.failtok.data.repositories.FailRepository
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        AppModule::class,
        ViewModelBuilder::class,
        DataModule::class
    ])
@Singleton
interface AppComponent : AndroidInjector<FailTokApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: FailTokApp): Builder

        fun build(): AppComponent
    }

    fun getAppContext(): Context
    fun getFailRepository(): FailRepository
}