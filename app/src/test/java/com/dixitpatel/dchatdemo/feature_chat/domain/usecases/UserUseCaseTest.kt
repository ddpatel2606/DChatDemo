package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: UserUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UserUseCase(repository)
    }

    @Test
    fun `invoke returns all users from repository`() = runTest {
        val users = listOf(
            User(id = "1", name = "Alex", profilePic = 1),
            User(id = "2", name = "Sara", profilePic = 2)
        )

        every { repository.getAllUsers() } returns flow { emit(users) }

        val result = useCase().toList()

        assertThat(result).hasSize(1)
        val emittedUsers = result[0]
        assertThat(emittedUsers).hasSize(2)
        assertThat(emittedUsers.map { it.name }).containsExactly("Alex", "Sara")
    }

    @Test
    fun `initialiseUsers calls repository initializeUsers`() = runTest {
        coEvery { repository.initializeUsers() } returns Unit

        useCase.initialiseUsers()

        coVerify(exactly = 1) { repository.initializeUsers() }
    }
}