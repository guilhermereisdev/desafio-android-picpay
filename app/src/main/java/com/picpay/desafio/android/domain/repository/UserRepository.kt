package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.model.ResultState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<ResultState<List<User>>>
}
