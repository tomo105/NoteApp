package com.example.noteapp.db

import android.content.Context
import androidx.room.Room

object NoteDatabaseBuilder {
    private var Instance: NoteDatabase? = null

    fun getInstance(context: Context): NoteDatabase {
        if (Instance == null) {
            synchronized(NoteDatabase::class) {
                Instance = roomBuild(context)
            }
        }
        return Instance!!
    }


    private fun roomBuild(context: Context) =
        Room.databaseBuilder(context,
            NoteDatabase::class.java,
            "note_database")
            .fallbackToDestructiveMigration()  //old db will be destroyed
            .build()


}