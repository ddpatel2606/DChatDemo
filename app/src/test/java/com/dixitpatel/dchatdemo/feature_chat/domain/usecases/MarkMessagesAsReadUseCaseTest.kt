package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MarkMessagesAsReadUseCaseTest {

    private lateinit var repository: MessageRepository
    private lateinit var useCase: MarkMessagesAsReadUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = MarkMessagesAsReadUseCase(repository)
    }

    @Test
    fun `invoke calls repository with correct userId`() = runTest {
        val currentUserId = "user1"

        // Mock repository suspend function
        coEvery { repository.markMessagesAsRead(currentUserId) } returns Unit

        // Execute use case
        useCase(currentUserId)

        // Verify repository method was called exactly once
        coVerify(exactly = 1) { repository.markMessagesAsRead(currentUserId) }
    }
}