package com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl

import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.MessagesDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.mapper.toMessage
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of the [MessageRepository] interface.
 * This class handles the data operations related to messages, acting as a single source of truth
 * by interacting with the local database via [MessagesDao].
 *
 * @property messageDao The Data Access Object for message-related database operations.
 */
class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessagesDao
) : MessageRepository {

    override fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages().map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override suspend fun sendMessage(content: String, senderId: String) {
        messageDao.insertMessage(MessageEntity(
            id = UUID.randomUUID().toString(),
            content = content,
            senderId = senderId,
            timestamp = LocalDateTime.now(),
            isRead = false
        ))
    }

    override suspend fun markMessagesAsRead(currentUserId: String) {
        messageDao.markMessagesAsRead(currentUserId)
    }

    override suspend fun clearChatMessages() {
        messageDao.deleteAllMessages()
    }
}