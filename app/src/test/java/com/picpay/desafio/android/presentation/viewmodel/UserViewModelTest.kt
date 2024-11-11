package com.picpay.desafio.android.presentation.viewmodel

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test

class UserViewModelTest {

    private lateinit var userViewModel: UserViewModel
    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setUp() {
        getUsersUseCase = mockk()
        userViewModel = UserViewModel(getUsersUseCase)
    }

    @Test
    fun `should emit loading state initially followed by success state when users are fetched`() = runTest {
        val users = listOf(
            User(1, "User 1", "Guilherme", "guilherme"),
            User(2, "User 2", "Jorge", "jorge")
        )

        coEvery { getUsersUseCase() } returns flow {
            emit(ResultState(isLoading = true))
            delay(100)
            emit(ResultState(data = users))
        }

        // Testando os estados com Turbine
        userViewModel.userState.test {
            // Verifica se o primeiro estado é de "loading"
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verifica se o segundo estado tem os dados
            val successState = awaitItem()
            assertEquals(users, successState.data)

            // Finaliza o teste
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should emit error state when an error occurs`() = runTest {
        coEvery { getUsersUseCase() } returns flow {
            emit(ResultState(isLoading = true))
            delay(100)
            emit(ResultState(error = "Erro: Falha de rede", isLoading = false))
        }

        // Testando os estados com Turbine
        userViewModel.userState.test {
            // Verifica se o primeiro estado é de "loading"
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verifica se o segundo estado é o erro
            val errorState = awaitItem()
            assertEquals("Erro: Falha de rede", errorState.error)

            // Finaliza o teste
            cancelAndConsumeRemainingEvents()
        }
    }
}
