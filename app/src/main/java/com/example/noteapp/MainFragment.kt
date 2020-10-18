package com.example.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider


class MainFragment : Fragment(), NoteAdapter.OnItemClickListener {
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]   //requireActivity() ---> ten sam co jest w rodzicu nie zrobi nowego !!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onItemClick(note: Note, position: Int) {

    }

    override fun onLongItemClick(note: Note, position: Int) {
        TODO("Not yet implemented")
    }

}