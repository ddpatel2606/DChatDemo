package com.dixitpatel.dchatdemo.feature_chat.data.local.mapper

import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User

/**
 * Maps a [MessageEntity] (data layer) to a [Message] (domain layer).
 * This is an extension function on [MessageEntity].
 * @return The corresponding [Message] domain model.
 */
fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        content = content,
        senderId = senderId,
        timestamp = timestamp,
        isRead = isRead
    )
}

/**
 * Maps a [UserEntity] (data layer) to a [User] (domain layer).
 * This is an extension function on [UserEntity].
 *
 * @return The corresponding [User] domain model.
 */
fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        profilePic = profilePicId
    )
}