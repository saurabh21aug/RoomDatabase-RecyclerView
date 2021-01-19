package com.roomdatabase_recyclerview.db

import androidx.room.*

@Dao
interface NoteDao {

    /*
    * without coroutine
    * */
//    @Insert
//    fun addNote(note: Note)
//
//    @Query("SELECT * FROM note")
//    fun getAllNoteS(): List<Note>

//    @Insert
//    fun addMultipleNotes(vararg note: Note)


    /*
    * with coroutine
    * */
    @Insert
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateData(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM Note")
    suspend fun deleteAll()


}