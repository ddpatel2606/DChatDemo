package com.dixitpatel.dchatdemo.feature_chat.domain.repository

import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun initializeUsers()

    fun getAllUsers(): Flow<List<User>>

    suspend fun getUserById(userId: String): User?

}