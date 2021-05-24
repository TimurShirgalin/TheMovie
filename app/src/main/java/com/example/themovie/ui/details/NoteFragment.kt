package com.example.themovie.ui.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.themovie.R
import com.example.themovie.databinding.FragmentNoteBinding
import com.example.themovie.model.NotesData
import com.example.themovie.viewModel.MainViewModel

class NoteFragment(model: MainViewModel, note: NotesData) : DialogFragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel = model
    private val noteData = note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_shape)
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() = with(binding) {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * widthRate).toInt()
        val height = (resources.displayMetrics.heightPixels * heightRate).toInt()
        dialog!!.window?.setLayout(width, height)
        if (noteData.note != null) noteEditText.setText(noteData.note)
        noteButtonCancel.setOnClickListener { dialog!!.dismiss() }
        noteButtonSave.setOnClickListener {
            when (noteData.note) {
                "" -> {
                    if (noteEditText.text.toString() != "")
                        viewModel.saveNote(NotesData(noteData.id, noteEditText.text.toString()))
                }
                else -> {
                    if (noteEditText.text.toString() != "")
                        viewModel.saveNote(NotesData(noteData.id, noteEditText.text.toString()))
                    else viewModel.deleteNote(noteData.id)
                }
            }
            dialog!!.dismiss()
        }
    }

    companion object {
        private const val widthRate = 0.85
        private const val heightRate = 0.40
    }
}