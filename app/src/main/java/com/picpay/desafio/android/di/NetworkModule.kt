package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.remote.PicPayService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module

val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(PicPayService::class.java) }
}
