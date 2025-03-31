package com.ivax.descarregarvideos.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ivax.descarregarvideos.R

class ChoosePlaylistDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            builder.setView(inflater.inflate(R.layout.dialog_choose_playlist, null))
                // Add action buttons.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}