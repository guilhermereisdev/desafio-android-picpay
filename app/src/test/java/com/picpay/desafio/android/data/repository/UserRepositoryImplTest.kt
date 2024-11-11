package com.picpay.desafio.android.data.repository

import app.cash.turbine.test
import com.picpay.desafio.android.data.local.User
import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.remote.PicPayService
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import java.io.IOException
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var apiService: PicPayService
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        // Mockando as dependências
        apiService = mockk()
        userDao = mockk()

        // Criando a instância do repositório com as dependências mockadas
        userRepository = UserRepositoryImpl(apiService, userDao)
    }

    @Test
    fun `should return loading, success from API and store in DB`() = runTest {
        val users = listOf(
            User(1, "img1", "User 1", "user1"),
            User(2, "img2", "User 2", "user2")
        )

        // Mockando a resposta da API
        coEvery { apiService.getUsers() } returns Response.success<List<User>>(users)

        // Mockando o comportamento do UserDao
        coEvery { userDao.deleteAll() } just Runs
        coEvery { userDao.insertAll(any()) } just Runs

        // Mockando o retorno do fluxo vazio no UserDao
        coEvery { userDao.getAllUsers() } returns flow { emit(emptyList()) }

        // Testando o fluxo
        userRepository.getUsers().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            assertEquals(null, loadingState.data)

            // Verificando o estado de sucesso após a resposta da API
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)

            // Checando os campos separadamente
            val usersData = successState.data ?: emptyList()
            assertEquals(2, usersData.size)

            assertEquals(1, usersData[0].id)
            assertEquals("img1", usersData[0].img)
            assertEquals("User 1", usersData[0].name)
            assertEquals("user1", usersData[0].username)

            assertEquals(2, usersData[1].id)
            assertEquals("img2", usersData[1].img)
            assertEquals("User 2", usersData[1].name)
            assertEquals("user2", usersData[1].username)

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return error when API returns failure`() = runTest {
        // Mockando a resposta da API com erro
        coEvery { apiService.getUsers() } throws IOException("Network failure")

        // Mockando o retorno do fluxo vazio no UserDao
        coEvery { userDao.getAllUsers() } returns flow { emit(emptyList()) }

        // Testando o fluxo
        userRepository.getUsers().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verificando o estado de erro após falha de rede
            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals("Erro: Falha de rede. Descrição: Network failure", errorState.error)

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return cached users if API call fails and cache is available`() = runTest {
        // Simulando dados em cache
        val cachedUsers = listOf(
            User(1, "img1", "User 1", "user1"),
            User(2, "img2", "User 2", "user2")
        )

        // Mockando a falha na chamada da API
        coEvery { apiService.getUsers() } throws IOException("Network failure")

        // Mockando o retorno dos dados no UserDao
        coEvery { userDao.getAllUsers() } returns flow { emit(cachedUsers) }

        // Testando o fluxo
        userRepository.getUsers().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verificando o estado de sucesso após pegar os dados do cache
            val successState = awaitItem()
            assertEquals(cachedUsers, successState.data)

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return error if API fails and cache is empty`() = runTest {
        // Mockando a falha na chamada da API
        coEvery { apiService.getUsers() } throws IOException("Não há dados salvos localmente")

        // Mockando o retorno vazio do UserDao
        coEvery { userDao.getAllUsers() } returns flow { emit(emptyList()) }

        // Testando o fluxo
        userRepository.getUsers().test {
            // Verificando o estado de loading
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            // Verificando o estado de erro devido a falha de rede e cache vazio
            val errorState = awaitItem()
            assertEquals(
                "Erro: Falha de rede. Descrição: Não há dados salvos localmente",
                errorState.error
            )

            // Finaliza o teste após consumir todos os itens
            cancelAndConsumeRemainingEvents()
        }
    }
}
