package com.dixitpatel.dchatdemo.feature_chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Represents a single message entity in the local database.
 * This data class is used by Room to create the "chats" table.
 *
 * @property id The unique identifier for the message. This is the primary key.
 * @property content The text content of the message.
 * @property senderId The ID of the user who sent the message.
 * @property timestamp The exact date and time the message was sent.
 * @property isRead A boolean flag indicating whether the message has been read by the recipient. Defaults to false.
 */
@Entity(tableName = "chats")
data class MessageEntity(
    @PrimaryKey val id: String,
    val content: String,
    val senderId: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)