package com.picpay.desafio.android.presentation.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.domain.model.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shouldDisplayErrorImageWhenImageIsNull() {
        val testUser = User(
            id = 2,
            name = "Jane Smith",
            img = null,
            username = "janesmith"
        )

        // Definindo o conteúdo da tela
        composeTestRule.setContent {
            ContactCard(user = testUser)
        }

        // Verificar se a imagem de erro está sendo exibida
        composeTestRule.onNodeWithContentDescription("Error Image").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayNameAndUsername() {
        val testUser = User(
            id = 1,
            name = "John Doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg",
            username = "johndoe"
        )

        // Definindo o conteúdo da tela
        composeTestRule.setContent {
            ContactCard(user = testUser)
        }

        // Verificar se o nome do usuário está sendo exibido
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()

        // Verificar se o username está sendo exibido
        composeTestRule.onNodeWithText("@johndoe").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayLoadingIndicatorWhileImageIsLoading() {
        val testUser = User(
            id = 3,
            name = "Sam Lee",
            img = "https://randomuser.me/api/portraits/men/2.jpg",
            username = "samlee"
        )

        // Definindo o conteúdo da tela
        composeTestRule.setContent {
            ContactCard(user = testUser)
        }

        // Verificar se o indicador de carregamento é exibido enquanto a imagem carrega
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
}
