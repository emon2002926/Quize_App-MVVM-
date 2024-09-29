package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.app.Dialog
import android.view.LayoutInflater
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.BackpressDialogConfirmationBinding

class CustomConfirmationDialog(private val fragment: HomeFragment) {
    
    fun show() {
        val context = fragment.requireContext()
        val binding = BackpressDialogConfirmationBinding.inflate(LayoutInflater.from(context))
        
        
        val alertDialog = Dialog(context)
        alertDialog.setContentView(binding.root)
        
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
        
        binding.btnConfirm.setOnClickListener {
            alertDialog.dismiss()
            fragment.requireActivity().finish()
        }
        binding.btnCancel.setOnClickListener { alertDialog.dismiss() }
        
        alertDialog.show()
        
    }
    
}