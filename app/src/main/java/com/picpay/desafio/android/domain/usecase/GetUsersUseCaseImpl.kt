package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCaseImpl(
    private val repository: UserRepository
) : GetUsersUseCase {
    override operator fun invoke(): Flow<ResultState<List<User>>> {
        return repository.getUsers()
    }
}
