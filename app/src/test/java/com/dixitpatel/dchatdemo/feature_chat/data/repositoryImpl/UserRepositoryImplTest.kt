package com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl

import com.dixitpatel.dchatdemo.feature_chat.data.consts.ALEX_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.data.consts.SARA_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.UsersDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private lateinit var userDao: UsersDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        userDao = mockk()
        repository = UserRepositoryImpl(userDao)
    }

    @Test
    fun `getAllUsers maps UserEntity to User`() = runTest {
        val entities = listOf(
            UserEntity(id = "1", name = "Alex", profilePicId = 1),
            UserEntity(id = "2", name = "Sara", profilePicId = 2)
        )

        every { userDao.getAllUsers() } returns flow { emit(entities) }

        val result = repository.getAllUsers().toList()

        assertThat(result).hasSize(1) // Flow emits once
        val users = result[0]
        assertThat(users).hasSize(2)
        assertThat(users.map { it.name }).containsExactly("Alex", "Sara")
    }

    @Test
    fun `getUserById returns mapped User when exists`() = runTest {
        val entity = UserEntity(id = "1", name = "Alex", profilePicId = 1)
        coEvery { userDao.getUserById("1") } returns entity

        val user = repository.getUserById("1")

        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo("1")
        assertThat(user?.name).isEqualTo("Alex")
    }

    @Test
    fun `getUserById returns null when user does not exist`() = runTest {
        coEvery { userDao.getUserById("nonexistent") } returns null

        val user = repository.getUserById("nonexistent")

        assertThat(user).isNull()
    }

    @Test
    fun `initializeUsers inserts default users`() = runTest {
        coEvery { userDao.insertUsers(any()) } just Runs

        repository.initializeUsers()

        coVerify(exactly = 1) {
            userDao.insertUsers(match { users ->
                users.any { it.id == SARA_USER_ID } &&
                        users.any { it.id == ALEX_USER_ID }
            })
        }
    }
}