package com.dixitpatel.dchatdemo.feature_chat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dixitpatel.dchatdemo.feature_chat.data.local.converter.DateConverter
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.MessagesDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.dao.UsersDao
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.MessageEntity
import com.dixitpatel.dchatdemo.feature_chat.data.local.entity.UserEntity

/**
 * The main database class for the application.
 *
 * This class is annotated with `@Database` and serves as the main access point
 * for the underlying connection to the app's persisted, relational data.
 * It lists the entities that belong in the database and provides abstract methods
 * for accessing the Data Access Objects (DAOs).
 *
 * @property entities The list of entity classes that are managed by this database.
 * @property version The version of the database schema.
 * @property exportSchema Set to false to avoid exporting the schema into a folder.
 *
 * @see RoomDatabase
 * @see MessagesDao
 * @see UsersDao
 * @see MessageEntity
 * @see UserEntity
 * @see DateConverter
 */
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