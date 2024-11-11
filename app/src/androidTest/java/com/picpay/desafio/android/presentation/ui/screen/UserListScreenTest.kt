package com.picpay.desafio.android.presentation.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.presentation.ui.theme.DesafioAndroidPicPayTheme
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    class FakeUserViewModel : UserViewModel(
        getUsersUseCase = object : GetUsersUseCase {
            override fun invoke() = flowOf(ResultState<List<User>>())
        }
    ) {
        private val _userState =
            MutableStateFlow<ResultState<List<User>>>(ResultState(isLoading = true))
        override var userState: StateFlow<ResultState<List<User>>> = _userState

        fun setUserState(state: ResultState<List<User>>) {
            _userState.value = state
        }
    }

    @Test
    fun shouldDisplayLoadingState() {
        val fakeViewModel = FakeUserViewModel().apply {
            setUserState(ResultState(isLoading = true))
        }

        composeTestRule.setContent {
            DesafioAndroidPicPayTheme {
                UserListScreen(
                    userViewModel = fakeViewModel,
                    navController = rememberNavController()
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayUserList_whenDataIsLoaded() {
        val testUsers = listOf(
            User(id = 1, img = "url1", name = "John Doe", username = "johndoe"),
            User(id = 2, img = "url2", name = "Jane Smith", username = "janesmith")
        )
        val fakeViewModel = FakeUserViewModel().apply {
            setUserState(ResultState(data = testUsers))
        }

        composeTestRule.setContent {
            DesafioAndroidPicPayTheme {
                UserListScreen(
                    userViewModel = fakeViewModel,
                    navController = rememberNavController()
                )
            }
        }

        testUsers.forEach { user ->
            composeTestRule.onNodeWithText(user.name ?: "Nome não disponível").assertIsDisplayed()
            composeTestRule.onNodeWithText("@${user.username}").assertIsDisplayed()
        }
    }

    @Test
    fun shouldDisplayErrorMessage_whenErrorOccurs() {
        val fakeViewModel = FakeUserViewModel().apply {
            setUserState(ResultState(error = "Erro: Falha de rede"))
        }

        composeTestRule.setContent {
            DesafioAndroidPicPayTheme {
                UserListScreen(
                    userViewModel = fakeViewModel,
                    navController = rememberNavController()
                )
            }
        }

        composeTestRule.onNodeWithTag("error_message")
            .assertTextContains("Erro", substring = true)
    }
}
