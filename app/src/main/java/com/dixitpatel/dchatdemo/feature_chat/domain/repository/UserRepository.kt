package com.dixitpatel.dchatdemo.feature_chat.domain.repository

import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Represents a repository for managing user data.
 * This interface defines the contract for data operations related to users,
 * abstracting the data source from the rest of the application.
 */
interface UserRepository {

    suspend fun initializeUsers()

    fun getAllUsers(): Flow<List<User>>

    suspend fun getUserById(userId: String): User?

}