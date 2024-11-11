package com.picpay.desafio.android.di

import android.app.Application
import androidx.room.Room
import com.picpay.desafio.android.data.local.UserDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get<Application>(), UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<UserDatabase>().userDao() }
}
