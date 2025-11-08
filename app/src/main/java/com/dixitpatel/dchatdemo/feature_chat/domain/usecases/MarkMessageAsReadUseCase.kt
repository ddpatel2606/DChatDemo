package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import javax.inject.Inject

/**
 * A use case responsible for marking all unread messages as read for the current user.
 *
 * This class acts as an intermediary between the ViewModel and the data layer (repository).
 * It encapsulates the specific business logic of updating the read status of messages.
 *
 * @property messageRepository The repository for accessing and manipulating message data.
 */
class MarkMessagesAsReadUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(currentUserId: String) {
        messageRepository.markMessagesAsRead(currentUserId)
    }
}