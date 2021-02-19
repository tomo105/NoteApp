package com.example.noteapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.NotesViewModel
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private var random = Random.nextInt(1, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]

        Log.d("COS", "{$random, elko tworze view model typie")

    }
}