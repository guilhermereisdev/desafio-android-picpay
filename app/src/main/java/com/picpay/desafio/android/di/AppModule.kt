package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<GetUsersUseCase> { GetUsersUseCaseImpl(get<UserRepository>()) }

    viewModel { UserViewModel(get()) }
}
