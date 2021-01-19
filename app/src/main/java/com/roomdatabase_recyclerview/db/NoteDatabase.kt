package com.roomdatabase_recyclerview.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var instant: NoteDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instant ?: synchronized(LOCK)
        {
            instant ?: buildDatabase(context).also {
                instant = it
            }

        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notedatabase"
            )
                .build()

    }
}

