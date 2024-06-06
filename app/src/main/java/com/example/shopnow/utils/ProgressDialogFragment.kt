package com.example.shopnow.utils

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.shopnow.R

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_progress_dialog)
        dialog.setCancelable(false)
        return dialog
    }

    companion object {
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }
}
