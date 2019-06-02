package com.ascri.failtok.utilities.di
import com.ascri.failtok.BuildConfig
import com.ascri.failtok.data.dataSources.RemoteFailDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class DataModule{

//    @Singleton
//    @Provides
//    fun provideFailRepository(): FailRepository {
//        return FailRepository()
//    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideFailDataSource(httpClient: OkHttpClient): RemoteFailDataSource {
        return RemoteFailDataSource(httpClient)
    }
}