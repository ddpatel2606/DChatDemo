package com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl

import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.MessagesDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class MessageRepositoryImplTest {

    private lateinit var messageDao: MessagesDao
    private lateinit var repository: MessageRepositoryImpl

    @Before
    fun setup() {
        messageDao = mockk()
        repository = MessageRepositoryImpl(messageDao)
    }

    @Test
    fun `getAllMessages maps MessageEntity to Message`() = runTest {
        val entityList = listOf(
            MessageEntity(
                id = "1",
                content = "Hello",
                senderId = "user1",
                timestamp = LocalDateTime.of(2025, 11, 7, 12, 0),
                isRead = false
            )
        )

        // Mock DAO flow
        every { messageDao.getAllMessages() } returns flow { emit(entityList) }

        val result: List<List<Message>> = repository.getAllMessages().toList()

        // Only one emission
        assertThat(result).hasSize(1)
        val messages = result[0]
        assertThat(messages).hasSize(1)
        assertThat(messages[0].id).isEqualTo("1")
        assertThat(messages[0].content).isEqualTo("Hello")
        assertThat(messages[0].senderId).isEqualTo("user1")
    }

    @Test
    fun `sendMessage inserts new MessageEntity into DAO`() = runTest {
        coEvery { messageDao.insertMessage(any()) } just Runs

        repository.sendMessage("Hi there", "user1")

        coVerify(exactly = 1) {
            messageDao.insertMessage(match {
                it.content == "Hi there" && it.senderId == "user1"
            })
        }
    }

    @Test
    fun `markMessagesAsRead calls DAO method with correct currentUserId`() = runTest {
        coEvery { messageDao.markMessagesAsRead(any()) } just Runs

        repository.markMessagesAsRead("user1")

        coVerify(exactly = 1) { messageDao.markMessagesAsRead("user1") }
    }

    @Test
    fun `clearChatMessages calls DAO deleteAllMessages`() = runTest {
        coEvery { messageDao.deleteAllMessages() } just Runs

        repository.clearChatMessages()

        coVerify(exactly = 1) { messageDao.deleteAllMessages() }
    }
}