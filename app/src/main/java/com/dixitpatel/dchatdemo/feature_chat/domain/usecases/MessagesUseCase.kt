package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.MessageGroup
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import com.dixitpatel.dchatdemo.feature_chat.domain.utils.hourUntil
import com.dixitpatel.dchatdemo.feature_chat.domain.utils.formatPrettyTimestamp
import com.dixitpatel.dchatdemo.feature_chat.domain.utils.secondsUntil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A use case that retrieves and processes chat messages.
 *
 * This class is responsible for fetching all messages from the [MessageRepository],
 * grouping them into [MessageGroup]s based on sender and time, and providing a flow of these groups.
 * It also offers a method to clear all chat messages.
 *
 * @property messageRepository The repository for accessing message data.
 */
class MessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<List<MessageGroup>> {
        return messageRepository.getAllMessages().map { messages ->
            messages.groupMessagesBySenderAndTime()
        }
    }

    suspend fun clearMessages() {
        messageRepository.clearChatMessages()
    }


    /**
     * Groups messages by:
     * 1. Same sender
     * 2. Within 20 seconds of last message
     * Adds timestamp header if gap >= 1 hour or first message
     */
    private fun List<Message>.groupMessagesBySenderAndTime(): List<MessageGroup> {
        if (isEmpty()) return emptyList()

        val result = mutableListOf<MessageGroup>()
        val currentGroup = mutableListOf<Message>()
        var last: Message? = null

        fun flushCurrent() {
            if (currentGroup.isNotEmpty()) {
                result.add(MessageGroup(messages = currentGroup.toList()))
                currentGroup.clear()
            }
        }

        for (msg in this) {
            val startNewGroup = last?.let { prev ->
                prev.senderId != msg.senderId ||
                        prev.timestamp.secondsUntil(msg.timestamp) > 20
            } ?: true

            val showTimestamp = last?.let { prev ->
                prev.timestamp.hourUntil(msg.timestamp) >= 1
            } ?: true

            if (showTimestamp) {
                flushCurrent()
                result.add(
                    MessageGroup(
                        messages = listOf(msg),
                        timestamp = msg.timestamp.formatPrettyTimestamp(),
                        isTimestampShow = true
                    )
                )
            } else {
                if (startNewGroup) flushCurrent()
                currentGroup.add(msg)
            }

            last = msg
        }

        flushCurrent()
        return result
    }

}