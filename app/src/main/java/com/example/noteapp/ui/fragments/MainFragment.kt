package com.example.noteapp.ui.fragments

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

// NoteAdapter.OnItemClickListener  -- moj interfejs obslugujacy klikniecie na notatce z adaptera
// dzieki temu mamy dane z Adaptera dostepne w Fragmencie

class MainFragment : Fragment(), NoteAdapter.OnItemClickListener,
    SortDialogFragment.OnItemClickDialogListener {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter
    private val requestCode = -123
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        notesViewModel =
            ViewModelProvider(requireActivity())[NotesViewModel::class.java]                                                  //requireActivity() ---> ten sam co jest w rodzicu (nie zrobi nowego )!!!

        requireActivity()                                                                                                    // if we are in 'selection' mode  we can go out from it using back press !!
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!searchView.isIconified) {   // gdy nie jest ikonka
                        searchView.onActionViewCollapsed()
                        updateModeUi()
                    } else {
                        if (notesViewModel.multiSelectMode)
                            exitMultiSelectMode()
                        else {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }

                }

            })
        //        CoroutineScope(Dispatchers.IO).launch {                           // create 1000 notes
//            for (i in 0..1000) {
//                val note = Note("$i", "$i", Calendar.getInstance().timeInMillis, false)
//                notesViewModel.insert(note)
//            }
//        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(requireActivity() as AppCompatActivity).supportActionBar?.title = "Your notes"
        recyclerView.layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) GridLayoutManager(requireContext(), 3)
        else GridLayoutManager(requireContext(), 2)

        addNote_fb.setOnClickListener {
            if (notesViewModel.multiSelectMode) {
                notesViewModel.delete(notesViewModel.selectedNotesToDelete.toList())                                    // toList gdyz funkcja delete przyjmuje List anie ArrayList !!!
                exitMultiSelectMode()
            } else {
                notesViewModel.setSelectedNote(null)                                        // when creating a new note you need to have a nothing in your note, so when you go out from fragment you need to set selected note to null!! override previous note which you can update before!!
                findNavController().navigate(R.id.addEditNoteFragment)                                                           //zarzada nawigacją, odnosnik do fragmentu
            }
        }
        sortData_fb.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            sortDialogFragment.setTargetFragment(this, requestCode)
            // sortDialogFragment

            sortDialogFragment.show(parentFragmentManager, "SortDialogFragment")
        }

        updateModeUi()   // to prevent change when you rotate your phone --> update ui !!
    }
    // wykonuje sie tuz przed startem
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesViewModel.allNotes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateNotes(it)                                                                                          // nasluchiwanie na nasza livedata, a jak sie zmienia to zmininmy dane w funkcji update i przypize donasze recycler view
        })

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onItemClick(note: Note, position: Int) {
        if (notesViewModel.multiSelectMode) {
            if (notesViewModel.selectedNotesToDelete.contains(note))
                unselectNote(note, position)
            else
                selectNote(note, position)
        } else {
            notesViewModel.setSelectedNote(note)
            findNavController().navigate(R.id.addEditNoteFragment)
            Log.d("TAG", "$note position: $position")
        }
    }
    override fun onLongItemClick(note: Note, position: Int) {
        if (!notesViewModel.multiSelectMode) {
            notesViewModel.multiSelectMode = !notesViewModel.multiSelectMode
            selectNote(note, position)
            updateModeUi()
        }
    }
    override fun onItemClickDialog(sortDesc: Boolean) {
        //ustawienie sortowania w viewmodel
        notesViewModel.sortDesc = sortDesc
        updateNotes(notesViewModel.allNotes.value!!)


        Log.d("OK", "udało sie on item click dialog  $sortDesc")
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {  //rozdmuchanie xml na obiekt i poslugiwnaie sie nim
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        searchView =
            menuItem.actionView as SearchView                                                      // nasz Search View bedzie obslugiwany jak normalny Search view ma obsluge klikania i wychodzenia !!!
        searchView.queryHint =
            "Search in notes"                                                            // dziala to przez uzycie w xml-u lini  actionViewClass

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {  // po zatwierdzeniu

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {  // przy wpisywaniu
                updateNotes(notesViewModel.findInNotes(newText.toString()))                                              // zeby uzywac mvvm wiec logika w view modelu
                Log.d("OK", "nie ok $newText")
                return false
            }

        })

    }

    private fun updateNotes(list: List<Note>) {
        noteAdapter = if (notesViewModel.sortDesc) NoteAdapter(
            list,
            this
        )                                                                //this bo implemetujemy w naszym fragmencie  obiekt ktory jest listenerem : NoteAdapter.OnItemClickListener
        else NoteAdapter(list.asReversed(), this)
        recyclerView.adapter = noteAdapter
    }
    private fun updateModeUi() {                              //c
        if (notesViewModel.multiSelectMode) {
            addNote_fb.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_delete))
            addNote_fb.labelText = "Delete notes"

            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Multi-select mode"
        } else {
            addNote_fb.setImageIcon(
                Icon.createWithResource(
                    requireContext(),
                    R.drawable.ic_note_add
                )
            )
            addNote_fb.labelText = "Add note"
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your notes"
        }
    }
    private fun selectNote(note: Note, position: Int) {
        note.isSelected = true
        notesViewModel.selectedNotesToDelete.add(note)
        noteAdapter.notifyItemChanged(position)                                 // pzeladuje on bind holder dla tej notatki - pozycji tzn zmienimy tlo na jasniejsze
    }
    private fun unselectNote(note: Note, position: Int) {
        note.isSelected = false
        notesViewModel.selectedNotesToDelete.remove(note)

        noteAdapter.notifyItemChanged(position)
        if (notesViewModel.selectedNotesToDelete.isEmpty())                     // if nothing in array list  -- we unselected all so we need to exit multi select mode
            exitMultiSelectMode()
    }
    private fun exitMultiSelectMode() {
        notesViewModel.multiSelectMode = false
        notesViewModel.selectedNotesToDelete.forEach { it.isSelected = false }
        notesViewModel.selectedNotesToDelete.clear()
        updateModeUi()
        noteAdapter.notifyDataSetChanged()                                          // layout odwiezy wszystkie stworzone elementy --> jak usuniemy to znikna, jak odznaczymy to odznaczy
    }
}