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
class SendMessageUseCaseTest {

    private lateinit var repository: MessageRepository
    private lateinit var useCase: SendMessageUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SendMessageUseCase(repository)
    }

    @Test
    fun `invoke calls repository sendMessage for non-blank content`() = runTest {
        coEvery { repository.sendMessage(any(), any()) } returns Unit

        val content = "Hello world"
        val senderId = "user1"
        useCase(content, senderId)

        coVerify(exactly = 1) {
            repository.sendMessage(content, senderId)
        }
    }

    @Test
    fun `invoke trims content before sending`() = runTest {
        coEvery { repository.sendMessage(any(), any()) } returns Unit

        val content = "   Trimmed message   "
        val senderId = "user2"
        useCase(content, senderId)

        coVerify(exactly = 1) {
            repository.sendMessage("Trimmed message", senderId)
        }
    }

    @Test
    fun `invoke does not call repository sendMessage for blank content`() = runTest {
        coEvery { repository.sendMessage(any(), any()) } returns Unit

        val blankContents = listOf("", "   ", "\n\t")
        val senderId = "user1"

        for (content in blankContents) {
            useCase(content, senderId)
        }

        coVerify(exactly = 0) { repository.sendMessage(any(), any()) }
    }
}