package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun deleteNotes(notes: List<Note>)

    @Query("SELECT * FROM note_table ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM note_table")
    suspend fun clearDatabase()
}