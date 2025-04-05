package com.ivax.descarregarvideos.dialog_fragments.codecs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ivax.descarregarvideos.responses.AdaptiveFormats

class CodecsConfirmDialogFragment(val formats: List<AdaptiveFormats>?, private val itemClickListener: (url: String?) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setPositiveButton("ok") { dialog, which ->
                itemClickListener(formats?.get(5)?.url)
            }
            .setNegativeButton("cancel"){ _,_ -> }
            .setSingleChoiceItems(
                this.formats?.map { it.mimeType }?.toTypedArray()
                , 0
            ) { dialog, which ->
                {
                    itemClickListener(formats?.get(which)?.url)
                }
                // Do something.
            }
            .create()
}