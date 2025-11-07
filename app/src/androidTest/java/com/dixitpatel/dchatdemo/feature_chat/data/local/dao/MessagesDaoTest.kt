package com.dixitpatel.dchatdemo.feature_chat.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dixitpatel.dchatdemo.feature_chat.data.local.database.AppDatabase
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MessagesDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: MessagesDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Only for testing
            .build()
        dao = database.messagesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert_and_getAllMessages_returnsInsertedMessage() = runTest {
        val message = MessageEntity(
            id = "1",
            senderId = "user1",
            content = "Hello",
            timestamp = LocalDateTime.now(),
            isRead = false
        )

        dao.insertMessage(message)

        val allMessages = dao.getAllMessages().first()
        Truth.assertThat(allMessages.size).isEqualTo(1)
        Truth.assertThat(allMessages[0].id).isEqualTo(message.id)
    }

    @Test
    fun updateMessage_changesExistingMessage() = runTest {
        val message = MessageEntity(
            id = "1", senderId = "user1", content = "Hello", timestamp = LocalDateTime.now(),
            isRead = false
        )
        dao.insertMessage(message)

        val updatedMessage = message.copy(content = "Updated")
        dao.updateMessage(updatedMessage)

        val allMessages = dao.getAllMessages().first()
        Truth.assertThat(allMessages[0].content).isEqualTo("Updated")

    }

    @Test
    fun markMessagesAsRead_setsUnreadMessagesFromOthersToRead() = runTest {
        val currentUserId = "user1"
        val messages = listOf(
            MessageEntity(
                id = "1", senderId = "user2", content = "Hi", timestamp = LocalDateTime.now(),
                isRead = false
            ), // from other user
            MessageEntity(
                "2", senderId = "user1", content = "Hello", timestamp = LocalDateTime.now(),
                isRead = false
            ) // from current user
        )
        messages.forEach { dao.insertMessage(it) }

        dao.markMessagesAsRead(currentUserId)

        val allMessages = dao.getAllMessages().first()
        val otherUserMessage = allMessages.find { it.senderId == "user2" }
        val currentUserMessage = allMessages.find { it.senderId == "user1" }

        Truth.assertThat(currentUserMessage?.isRead).isEqualTo(false)
        Truth.assertThat(otherUserMessage?.isRead).isEqualTo(true)

    }

    @Test
    fun deleteAllMessages_clearsDatabase() = runTest {
        val message = MessageEntity(
            id = "1", senderId = "user1", content = "Hello", timestamp = LocalDateTime.now(),
            isRead = false
        )
        dao.insertMessage(message)

        dao.deleteAllMessages()
        val allMessages = dao.getAllMessages().first()
        Truth.assertThat(allMessages.isEmpty()).isTrue()
    }
}