package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.note_item.*
import java.util.*

class AddEditNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel =
            ViewModelProvider(requireActivity())[NotesViewModel::class.java]   //requireActivity() ---> ten sam co jest w rodzicu (nie zrobi nowego) !!! wlasciclelem tego fragmentu jest aktywnosc --> aktywnosc rodzic tego frsagmetu

        requireActivity()                                       //iesli ktos bedzie w naszym fragmencie i kliknie ze chce wrocic to dostaniemy taka informacje
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (title_addeditFrag.text.isNotEmpty() || message_addeditFrag.text.isNotEmpty()) {
                        val title = title_addeditFrag.text.toString()
                        val message = message_addeditFrag.text.toString()
                        val date = Calendar.getInstance().timeInMillis

                        if (notesViewModel.getSelectedNote().value == null) {              //add new note

                            val note = Note(title, message, date)
                            notesViewModel.insert(note)
                        } else {                                                            //update note
                            val selectedNote = notesViewModel.getSelectedNote().value!!
                            if (selectedNote.title != title || selectedNote.message != message) {                           // check if make any difference !!!
                                val note = Note(title, message, date).apply {
                                    rowId = notesViewModel.getSelectedNote().value!!.rowId                               // the same rowId to update not create new!!
                                }
                                notesViewModel.update(note)
                            }
                        }
                    }

                    isEnabled = false
                    requireActivity().onBackPressed()                                   // nothing happen, not data inserted, maybe a missclick -  //cofamy sie bo robocie
                    Log.d("TAG", "Pressed collback from fragment ")

                }

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate rozdmuchuje widok
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesViewModel.getSelectedNote().observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                title_addeditFrag.setText(it.title)
                message_addeditFrag.setText(it.message)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        notesViewModel.setSelectedNote(null)                                        // when creating a new note you need to have a nothing in your note, so when you go out from fragment you need to set selected note to null!! override previous note which you can update before!!
    }
}