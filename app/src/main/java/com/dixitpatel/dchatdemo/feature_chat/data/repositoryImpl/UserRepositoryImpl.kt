package com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl

import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.data.consts.ALEX_USERNAME
import com.dixitpatel.dchatdemo.feature_chat.data.consts.ALEX_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.data.consts.SARA_USERNAME
import com.dixitpatel.dchatdemo.feature_chat.data.consts.SARA_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.UsersDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.mapper.toUser
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [UserRepository] interface.
 * This class is responsible for handling user data operations, acting as a single source of truth
 * for user-related information. It interacts with the local database via the [UsersDao].
 *
 * @property userDao The Data Access Object for interacting with the user table in the database.
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UsersDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toUser() }
        }
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toUser()
    }

    override suspend fun initializeUsers() {
        val users = listOf(
            UserEntity(id = SARA_USER_ID, name = SARA_USERNAME, profilePicId = R.drawable.ic_sara_profile),
            UserEntity(id = ALEX_USER_ID, name = ALEX_USERNAME, profilePicId = R.drawable.ic_alex_profile)
        )
        userDao.insertUsers(users)
    }
}