package com.picpay.desafio.android.presentation.ui.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.data.local.User
import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.local.UserDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: UserDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveUsers() = runBlocking {
        val user = User(id = 1, img = "url", name = "John Doe", username = "johndoe")
        userDao.insertAll(listOf(user))

        val retrievedUsers = userDao.getAllUsers().first()
        assertEquals(1, retrievedUsers.size)
        assertEquals(user, retrievedUsers[0])
    }

    @Test
    fun deleteAllUsers() = runBlocking {
        val user1 = User(id = 1, img = "url1", name = "John Doe", username = "johndoe")
        val user2 = User(id = 2, img = "url2", name = "Jane Smith", username = "janesmith")
        userDao.insertAll(listOf(user1, user2))

        userDao.deleteAll()
        val retrievedUsers = userDao.getAllUsers().first()
        assertEquals(0, retrievedUsers.size)
    }
}
