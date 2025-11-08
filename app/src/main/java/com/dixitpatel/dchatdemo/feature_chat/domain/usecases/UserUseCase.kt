package com.dixitpatel.dchatdemo.feature_chat.domain.usecases

import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case for interacting with user data.
 *
 * This class encapsulates the business logic for fetching and initializing users,
 * acting as an intermediary between the ViewModel and the [UserRepository].
 *
 * @property userRepository The repository responsible for user data operations.
 */
class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }

    suspend fun initialiseUsers() {
        userRepository.initializeUsers()
    }
}