package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
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
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class MessagesUseCaseTest {

    private lateinit var repository: MessageRepository
    private lateinit var useCase: MessagesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = MessagesUseCase(repository)
    }

    @Test
    fun `invoke returns grouped messages`() = runTest {
        val messages = listOf(
            Message(
                id = "1",
                content = "Hi",
                senderId = "user1",
                timestamp = LocalDateTime.of(2025, 11, 7, 12, 0),
                isRead = false
            ),
            Message(id = "2", content = "Hello again", senderId = "user1", timestamp = LocalDateTime.of(2025,11,7,12,0,5), isRead = false),
            Message(id = "3", content = "Hey", senderId = "user2", timestamp = LocalDateTime.of(2025,11,7,13,35), isRead = false)
        )

        every { repository.getAllMessages() } returns flow { emit(messages) }

        val result = useCase().toList() // collect all emissions

        assertThat(result).hasSize(1)
        val groups = result[0]
        // The first two messages are same sender, within 20 seconds → grouped
        assertThat(groups.any { it.messages.size == 2 && it.messages[0].senderId == "user1" }).isFalse()
        // The third message should be in a new group
        assertThat(groups.any { it.messages.size == 1 && it.messages[0].senderId == "user2" }).isTrue()
    }

    @Test
    fun `clearMessages calls repository clearChatMessages`() = runTest {
        coEvery { repository.clearChatMessages() } returns Unit

        useCase.clearMessages()

        coVerify(exactly = 1) { repository.clearChatMessages() }
    }

    @Test
    fun `invoke returns empty list when no messages`() = runTest {
        every { repository.getAllMessages() } returns flow { emit(emptyList()) }

        val result = useCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEmpty()
    }
}