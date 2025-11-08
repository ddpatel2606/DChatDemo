package com.dixitpatel.dchatdemo.feature_chat.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dixitpatel.dchatdemo.feature_chat.data.local.database.AppDatabase
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test class for [UsersDao].
 *
 * This class tests the database operations for the `users` table, ensuring that the DAO
 * functions for inserting, retrieving, and updating user data work as expected. It uses
 * an in-memory Room database to isolate the tests from the actual application database and
 * to ensure a clean state for each test run.
 *
 * The tests cover:
 * - Inserting a single user and verifying its retrieval.
 * - Inserting a list of users and verifying their retrieval.
 * - Fetching a specific user by their ID.
 * - Handling cases where a user ID does not exist.
 * - Verifying that inserting a user with a conflicting ID overwrites the existing entry.
 *
 * @see UsersDao
 * @see AppDatabase
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UsersDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: UsersDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.usersDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertUser_and_getAllUsers_returnsInsertedUser() = runTest {
        val user = UserEntity(id = "1", name = "Alex", profilePicId = 0)
        dao.insertUser(user)

        val allUsers = dao.getAllUsers().first()
        assertThat(allUsers).hasSize(1)
        assertThat(allUsers[0].id).isEqualTo(user.id)
        assertThat(allUsers[0].name).isEqualTo(user.name)
    }

    @Test
    fun insertUsers_and_getAllUsers_returnsAllInsertedUsers() = runTest {
        val users = listOf(
            UserEntity("1", "Alex", 1),
            UserEntity("2", "Sara", 2)
        )
        dao.insertUsers(users)

        val allUsers = dao.getAllUsers().first()
        assertThat(allUsers).hasSize(2)
        assertThat(allUsers.map { it.name }).containsExactly("Alex", "Sara")
    }

    @Test
    fun getUserById_returnsCorrectUser() = runTest {
        val user = UserEntity("1", "Alex", 2)
        dao.insertUser(user)

        val fetched = dao.getUserById("1")
        assertThat(fetched).isNotNull()
        assertThat(fetched?.name).isEqualTo("Alex")
    }

    @Test
    fun getUserById_returnsNull_forNonExistingUser() = runTest {
        val fetched = dao.getUserById("nonexistent")
        assertThat(fetched).isNull()
    }

    @Test
    fun insertUser_overwritesOnConflict() = runTest {
        val user1 = UserEntity("1", "Alex", 2)
        val user2 = UserEntity("1", "AlexUpdated", 1)

        dao.insertUser(user1)
        dao.insertUser(user2) // should replace previous

        val allUsers = dao.getAllUsers().first()
        assertThat(allUsers).hasSize(1)
        assertThat(allUsers[0].name).isEqualTo("AlexUpdated")
        assertThat(allUsers[0].profilePicId).isEqualTo(1)
    }
}