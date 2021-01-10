package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.NotesViewModel

class AddEditNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]   //requireActivity() ---> ten sam co jest w rodzicu (nie zrobi nowego) !!! wlasciclelem tego fragmentu jest aktywnosc --> aktywnosc rodzic tego frsagmetu

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate rozdmuchuje widok
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }
}