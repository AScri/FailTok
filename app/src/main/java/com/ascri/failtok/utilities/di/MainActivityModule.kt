package com.ascri.failtok.utilities.di

import androidx.lifecycle.ViewModel
import com.ascri.failtok.ui.main.HotFailsFragmentFails
import com.ascri.failtok.ui.main.NewFailsFragmentFails
import com.ascri.failtok.ui.main.TopFailsFragmentFails
import com.ascri.failtok.ui.viewModels.FailsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule{

    @ContributesAndroidInjector
    abstract fun mainActivityHotFragment(): HotFailsFragmentFails

    @ContributesAndroidInjector
    abstract fun mainActivityNewFragment(): NewFailsFragmentFails

    @ContributesAndroidInjector
    abstract fun mainActivityTopFragment(): TopFailsFragmentFails

    @Binds
    @IntoMap
    @ViewModelKey(FailsViewModel::class)
    abstract fun bindMainViewModel(failsViewModel: FailsViewModel): ViewModel
}