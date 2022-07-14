package com.example.currencydekho.di

import com.example.currencydekho.data.CurrencyApi
import com.example.currencydekho.main.DefaultMainRepository
import com.example.currencydekho.main.MainRepository
import com.example.currencydekho.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val Base_URL = "https://api.apilayer.com/exchangerates_data/"

//telling dagger hilt its a module amd we need to install this module
@Module
@InstallIn(ApplicationComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(Base_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun  provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}