package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseImplTest {

    private lateinit var getUsersUseCase: GetUsersUseCaseImpl
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        getUsersUseCase = GetUsersUseCaseImpl(userRepository)
    }

    @Test
    fun `should return loading state first, followed by success state`() = runTest {
        val users = listOf(
            User(1, "User 1", "Guilherme", "guilherme"),
            User(2, "User 2", "Jorge", "jorge")
        )

        coEvery { userRepository.getUsers() } returns flow {
            emit(ResultState<List<User>>(isLoading = true))
            delay(100)
            emit(ResultState(data = users))
        }

        // Usando Turbine para verificar os estados emitidos
        getUsersUseCase().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verificando o estado de sucesso
            val successState = awaitItem()
            assertEquals(users, successState.data)

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return error state if repository fails`() = runTest {
        coEvery { userRepository.getUsers() } returns flow {
            emit(ResultState<List<User>>(isLoading = true))
            delay(100)
            emit(ResultState<List<User>>(error = "Erro: Falha de rede.", isLoading = false))
        }

        // Usando Turbine para verificar os estados emitidos
        getUsersUseCase().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verificando o estado de erro
            val errorState = awaitItem()
            assertEquals("Erro: Falha de rede.", errorState.error)

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }
}
