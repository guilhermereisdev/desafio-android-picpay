package com.picpay.desafio.android.presentation.ui.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class PicPayServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var picPayService: PicPayService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        picPayService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicPayService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetUsersReturnsDataSuccessfully() = runBlocking {
        val testUsers = listOf(
            User(img = "url1", name = "John Doe", id = 1, username = "johndoe"),
            User(img = "url2", name = "Jane Smith", id = 2, username = "janesmith")
        )
        val responseJson = Gson().toJson(testUsers)

        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))

        val result = picPayService.getUsers().body() ?: emptyList()
        assertEquals(2, result.size)
        assertEquals(testUsers[0].id, result[0].id)
        assertEquals(testUsers[0].img, result[0].img)
        assertEquals(testUsers[0].name, result[0].name)
        assertEquals(testUsers[0].username, result[0].username)

        assertEquals(testUsers[1].id, result[1].id)
        assertEquals(testUsers[1].img, result[1].img)
        assertEquals(testUsers[1].name, result[1].name)
        assertEquals(testUsers[1].username, result[1].username)
    }

    @Test
    fun testGetUsersHandlesErrorResponse() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val response = picPayService.getUsers()
        assertEquals(false, response.isSuccessful)
        assertEquals(404, response.code())
    }
}
