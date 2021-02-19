package com.example.noteapp.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R

class SortDialogFragment : DialogFragment() {

    private lateinit var listener: OnItemClickDialogListener
    private lateinit var radioGroup: RadioGroup
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Sorting notes"

        listener = try {
            targetFragment as OnItemClickDialogListener
        } catch (e :TypeCastException) {
            throw TypeCastException()
        }

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_dialog_sort, null)                            // olewamy bo to dialog fragment

        radioGroup = view.findViewById(R.id.sort_RG)                                                        // lapiemy nasz radio group
        builder.setView(view)
            .setTitle("Sort date:")
            .setPositiveButton("OK") { d, i ->
                when (radioGroup.checkedRadioButtonId) {
                    R.id.sort_desc_RB -> { listener.onItemClickDialog(true) }
                    R.id.sort_asc_RB -> { listener.onItemClickDialog(false) }
                }
            }
            .setNegativeButton("Cancel") { di, i -> }


        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your notes"

    }

    interface OnItemClickDialogListener {
        fun onItemClickDialog(sortDesc : Boolean) {

        }
    }
}