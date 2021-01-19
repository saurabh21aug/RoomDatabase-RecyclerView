package com.roomdatabase_recyclerview.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation

import com.roomdatabase_recyclerview.R

import com.roomdatabase_recyclerview.db.Note
import com.roomdatabase_recyclerview.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note: Note? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edit_text_title.setText(note?.title)
            edit_text_note.setText(note?.note)
        }

        button_save.setOnClickListener { view ->
            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()

            if (noteTitle.isEmpty()) {
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }
            if (noteBody.isEmpty()) {
                edit_text_note.error = "note required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

//            val note: Note = Note(title = noteTitle, note = noteBody)


//            here we use room database with the help of  launch function, which is extend from BaseFragment were use implement coroutine
            launch {
                context?.let {

                    val mNote = Note(noteTitle, noteBody)

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateData(mNote)
                        it.toast("Note Updated")
                    }
                    view?.hideKeyboard()

                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }


//            now we can update data in database
//            but we can not use room in side main thread
//            NoteDatabase(requireActivity()).getNoteDao().addNote(note)


            //so we use asyncTask for background thread, and it is very lengthy process
//            saveNote(note)


//            //    so we can use this in case of AsyncTask , but the correct way of use background thread Coroutine
//            val executor = Executors.newSingleThreadExecutor()
//            val handler = Handler(Looper.getMainLooper())
//            executor.execute {
//                NoteDatabase(requireActivity()).getNoteDao().addNote(note)
//
//                handler.post {
//                    Toast.makeText(activity, "Note Saved", Toast.LENGTH_LONG).show()
//
//                }
//            }


        }
    }

//    // AsyncTask is now deprecated on android api version 30 (Android 11)
//    private fun saveNote(note: Note) {
//        class SaveNote : AsyncTask<Void, Void, Void>() {
//            override fun doInBackground(vararg params: Void?): Void? {
//                NoteDatabase(requireActivity()).getNoteDao().addNote(note)
//                return null
//            }
//
//            override fun onPostExecute(result: Void?) {
//                super.onPostExecute(result)
//                Toast.makeText(activity, "Note Saved", Toast.LENGTH_LONG).show()
//            }
//        }
//        SaveNote().execute()
//    }

    private fun deleteNote() {
        view?.hideKeyboard()

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You can not undo this operation")

            setPositiveButton("Yes") { dialog, which ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_note, menu)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}