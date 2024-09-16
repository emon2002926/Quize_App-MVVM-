package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.app.Dialog
import android.view.LayoutInflater
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.BackpressDialogConfirmationBinding

class CustomConfirmationDialog(private val fragment: HomeFragment) {
    
    fun show() {
        val context = fragment.requireContext()
        val binding = BackpressDialogConfirmationBinding.inflate(LayoutInflater.from(context))
//        val alertDialog = Dialog.Builder(context)
//            .setView(binding.root)
//            .create()
        
        val alertDialog = Dialog(context)
        alertDialog.setContentView(binding.root)
        
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
//        alertDialog.window?.setLayout(100, 100)
        
        binding.btnConfirm.setOnClickListener {
            alertDialog.dismiss()
            fragment.requireActivity().finish()
        }
        binding.btnCancel.setOnClickListener { alertDialog.dismiss() }
        
        alertDialog.show()

//        // Set margin to the left and right side
//        val layoutParams = alertDialog.window?.attributes
//        layoutParams?.width =
//            WindowManager.LayoutParams.WRAP_CONTENT  // Make the dialog take up full width
//        val marginInPixels =
//            context.resources.getDimensionPixelSize(R.dimen.dialog_margin)  // Get margin in pixels
//        alertDialog.window?.setLayout(
//            layoutParams?.width ?: WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        alertDialog.window?.decorView?.setPadding(
//            marginInPixels,
//            0,
//            marginInPixels,
//            0
//        )  // Apply left and right margin
    }
    
}