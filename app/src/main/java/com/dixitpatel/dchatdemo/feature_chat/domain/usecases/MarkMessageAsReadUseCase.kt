package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import javax.inject.Inject

class MarkMessagesAsReadUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(currentUserId: String) {
        messageRepository.markMessagesAsRead(currentUserId)
    }
}