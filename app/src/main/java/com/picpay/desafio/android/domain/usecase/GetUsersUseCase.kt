package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    operator fun invoke(): Flow<ResultState<List<User>>>
}
