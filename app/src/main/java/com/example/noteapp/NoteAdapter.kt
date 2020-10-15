package com.example.noteapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val notesList: List<Note>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {


    // create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)

        return MyViewHolder(view)
    }

    //counts of elements
    override fun getItemCount(): Int {
        return notesList.size
    }

    // connect with special view element: textView or sth !!!!
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.WHITE)

        if (notesList[position].isSelected) holder.itemView.setBackgroundColor(Color.LTGRAY)
        holder.itemView.note_title.text = notesList[position].title
        holder.itemView.note_message.text = notesList[position].message

    }

    inner class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) { //explain the our view /hold it
        init {
            view.setOnClickListener {
                listener.onItemClick(
                    notesList[adapterPosition],
                    adapterPosition
                )
            }
            view.setOnLongClickListener {
                listener.onLongItemClick(notesList[adapterPosition], adapterPosition)
                true
            }
        }
    }

    //to handle clicking and  holding a element     COMMUNICATE  DATA TO FRAGMENT
    interface OnItemClickListener {
        fun onItemClick(
            note: Note,
            position: Int
        ) //po kliknieciu aktualizujemy nowa notatke w nowym fragmencie

        fun onLongItemClick(note: Note, position: Int)  //po przytrzymaniu odpalamy tryb multiselect
    }

}