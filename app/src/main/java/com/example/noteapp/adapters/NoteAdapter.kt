package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val notesList: List<Note>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {


    // create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    //counts number of elements
    override fun getItemCount(): Int {
        return notesList.size
    }

    // connect with special view element: textView or sth (sponner, radio button etc) !!!!
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.WHITE)

        if (notesList[position].isSelected)
            holder.itemView.setBackgroundColor(Color.LTGRAY)                                                //change color to ltgrey

        holder.itemView.note_title.text = notesList[position].title
        holder.itemView.note_message.text = notesList[position].message

    }
      // klasa wewnetrzna
    inner class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
          //explain the our view /hold it podpinanie widoku
        init {
            view.setOnClickListener { listener.onItemClick(notesList[adapterPosition], adapterPosition) }
            view.setOnLongClickListener {
                listener.onLongItemClick(notesList[adapterPosition], adapterPosition)
                true
            }
        }
    }

    //to handle clicking and  holding a element  COMMUNICATE  DATA TO FRAGMENT
    // komunikacja miedzy Fragmentem a Adapterem (dane z adaptera --> fragmentu)
    // interfejs ktory implementujemy na obiekcie ktory chcemy nasluchiwac

    interface OnItemClickListener {
        fun onItemClick(note: Note, position: Int)          //po kliknieciu aktualizujemy nowa notatke w nowym fragmencie

        fun onLongItemClick(note: Note, position: Int)      //po przytrzymaniu odpalamy tryb multiselect
    }

}