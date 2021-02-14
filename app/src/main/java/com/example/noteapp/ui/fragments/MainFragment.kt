package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

// NoteAdapter.OnItemClickListener  -- moj interfejs obslugujacy klikniecie na notatce z adaptera
// dzieki temu mamy dane z Adaptera dostepne w Fragmencie

class MainFragment : Fragment(), NoteAdapter.OnItemClickListener {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel =
            ViewModelProvider(requireActivity())[NotesViewModel::class.java]                                                  //requireActivity() ---> ten sam co jest w rodzicu (nie zrobi nowego )!!!
//        CoroutineScope(Dispatchers.IO).launch {
//            for (i in 0..1000) {
//                val note = Note("$i", "$i", Calendar.getInstance().timeInMillis, false)
//                notesViewModel.insert(note)
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        addNote_fb.setOnClickListener {
            findNavController().navigate(R.id.addEditNoteFragment)                                                           //zarzada nawigacją, odnosnik do fragmentu

        }
    }

    // wykonuje sie tuz przed startem
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesViewModel.allNotes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateNotes(it)                                                                                          // nasluchiwanie na nasza livedata, a jak sie zmienia to zmininmy dane w funkcji update i przypize donasze recycler view
        })

    }

    private fun updateNotes(list: List<Note>) {
        noteAdapter = NoteAdapter(
            list,
            this
        )                                                                //this bo implemetujemy w naszym fragmencie  obiekt ktory jest listenerem : NoteAdapter.OnItemClickListener
        recyclerView.adapter = noteAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onItemClick(note: Note, position: Int) {
        notesViewModel.setSelectedNote(note)
        findNavController().navigate(R.id.addEditNoteFragment)
        Log.d("TAG", "$note position: $position")

    }

    override fun onLongItemClick(note: Note, position: Int) {
        TODO("Not yet implemented")
    }

}