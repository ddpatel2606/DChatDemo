package com.dixitpatel.dchatdemo.di

import com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl.MessageRepositoryImpl
import com.dixitpatel.dchatdemo.feature_chat.data.repositoryImpl.UserRepositoryImpl
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that provides repository bindings.
 * This abstract class is used by Dagger Hilt to provide instances of repository interfaces.
 *
 * It is installed in the [SingletonComponent], meaning the provided repositories will have a singleton scope
 * and live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}