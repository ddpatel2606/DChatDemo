package com.dixitpatel.dchatdemo.feature_chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the local database.
 * This data class is used by Room to create the "users" table.
 *
 * @property id The unique identifier for the user. This is the primary key.
 * @property name The name of the user.
 * @property profilePicId The resource ID for the user's profile picture, defaults to 0.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val profilePicId: Int = 0
)