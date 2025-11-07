package com.dixitpatel.dchatdemo.feature_chat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dixitpatel.dchatdemo.feature_chat.data.local.converter.DateConverter
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.MessagesDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.UsersDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity

@Database(
    entities = [MessageEntity::class, UserEntity::class], version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao
    abstract fun usersDao(): UsersDao

    companion object {
        const val DATABASE_NAME = "chat_db"
    }
}