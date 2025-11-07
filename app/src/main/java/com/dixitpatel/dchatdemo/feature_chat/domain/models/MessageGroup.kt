package com.dixitpatel.dchatdemo.feature_chat.domain.models

data class MessageGroup(
    val messages: List<Message>,
    val timestamp: String? = null,
    val isTimestampShow: Boolean = false
)