package com.roomdatabase_recyclerview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView


import com.roomdatabase_recyclerview.R
import com.roomdatabase_recyclerview.db.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private var notes: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {

        holder.itemView.text_title.text = notes[position].title
        holder.itemView.text_note.text = notes[position].note

        holder.itemView.setOnClickListener {

            val action = HomeFragmentDirections.actionAddNote()
            action.note = notes[position]
            Navigation.findNavController(it).navigate(action)
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}