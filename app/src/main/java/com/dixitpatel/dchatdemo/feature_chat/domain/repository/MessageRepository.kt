package com.dixitpatel.dchatdemo.feature_chat.domain.repository

import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getAllMessages(): Flow<List<Message>>

    suspend fun sendMessage(content: String, senderId: String)

    suspend fun markMessagesAsRead(currentUserId: String)

    suspend fun clearChatMessages()
}