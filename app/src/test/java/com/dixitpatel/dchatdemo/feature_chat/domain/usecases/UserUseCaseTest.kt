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

/**
 * Unit tests for the [UserUseCase].
 *
 * This test class verifies the behavior of the `UserUseCase` by mocking its
 * dependency, [UserRepository], and asserting that the use case interacts with the
 * repository as expected. It uses `MockK` for mocking and `kotlinx-coroutines-test`
 * for testing coroutine-based logic.
 *
 * The tests cover two main scenarios:
 * 1.  Invoking the use case to ensure it correctly fetches and returns a flow of users
 *     from the repository.
 * 2.  Calling the `initialiseUsers` method to confirm it properly delegates the call
 *     to the repository's `initializeUsers` function.
 */
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