package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class UserRepositoryImpl(
    private val apiService: PicPayService,
    private val userDao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<ResultState<List<User>>> = flow {
        emit(ResultState(isLoading = true))

        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { users ->
                    userDao.deleteAll()
                    userDao.insertAll(users)

                    emit(ResultState(data = users.map { apiUser ->
                        User(
                            id = apiUser.id,
                            img = apiUser.img,
                            name = apiUser.name,
                            username = apiUser.username
                        )
                    }, isLoading = false))
                    return@flow
                }
            } else {
                emit(ResultState(error = "Erro: ${response.code()}", isLoading = false))
            }
        } catch (e: IOException) {
            emit(ResultState(error = "Erro: Falha de rede. Descrição: ${e.message}", isLoading = false))
        } catch (e: HttpException) {
            emit(ResultState(error = "Erro: ${e.message()}", isLoading = false))
        }

        val cachedUsers = userDao.getAllUsers().map { localUsers ->
            if (localUsers.isEmpty()) {
                ResultState(error = "Não há dados salvos localmente", isLoading = false)
            } else {
                ResultState(data = localUsers.map { localUser ->
                    User(
                        id = localUser.id,
                        img = localUser.img,
                        name = localUser.name,
                        username = localUser.username
                    )
                }, isLoading = false)
            }
        }
        emitAll(cachedUsers)
    }
}
