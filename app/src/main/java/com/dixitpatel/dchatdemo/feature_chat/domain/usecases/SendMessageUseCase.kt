package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import javax.inject.Inject

/**
 * A use case responsible for sending a new chat message.
 *
 * This class encapsulates the business logic for sending a message. It ensures that only
 * non-blank messages are sent after trimming any leading or trailing whitespace.
 * It relies on a [MessageRepository] to handle the actual data transmission.
 *
 * @property messageRepository The repository for handling message data operations.
 */
class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(content: String, senderId: String) {
        if (content.isNotBlank()) {
            messageRepository.sendMessage(content.trim(), senderId)
        }
    }
}