package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(content: String, senderId: String) {
        if (content.isNotBlank()) {
            messageRepository.sendMessage(content.trim(), senderId)
        }
    }
}