package com.dixitpatel.dchatdemo.di

import android.content.Context
import androidx.room.Room
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.MessagesDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.UsersDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMessagesDao(database: AppDatabase): MessagesDao {
        return database.messagesDao()
    }

    @Provides
    @Singleton
    fun provideUsersDao(database: AppDatabase): UsersDao {
        return database.usersDao()
    }
}