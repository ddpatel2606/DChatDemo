package com.dixitpatel.dchatdemo.feature_chat.domain.repository

import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing chat messages.
 * This repository acts as an abstraction layer for the data sources (e.g., local database, remote API)
 * related to chat messages.
 */
interface MessageRepository {

    fun getAllMessages(): Flow<List<Message>>

    suspend fun sendMessage(content: String, senderId: String)

    suspend fun markMessagesAsRead(currentUserId: String)

    suspend fun clearChatMessages()
}