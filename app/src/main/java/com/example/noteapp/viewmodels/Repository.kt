package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.Note
import com.example.noteapp.db.NoteDatabaseBuilder

class Repository(app: Application) {
    private val notesDao = NoteDatabaseBuilder.getInstance(app.applicationContext).noteDao()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun delete(list: List<Note>) {
        notesDao.deleteNotes(list)
    }
    suspend fun clearDatabase() {
        notesDao.clearDatabase()
    }

     fun getAllNotes() : LiveData<List<Note>>{
        return notesDao.getAllNotes().asLiveData() // bo z notesDao otrzymujemy Flow a tu zamieniamy na LiveData zeby korzystac z LiveData w view modelu
    }


}