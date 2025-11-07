package com.dixitpatel.dchatdemo.feature_chat.data.local.mapper

import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        content = content,
        senderId = senderId,
        timestamp = timestamp,
        isRead = isRead
    )
}

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        profilePic = profilePicId
    )
}