package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.LayoutShowExamOptionBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ExamOptionsBottomSheet(
    private val fragment: Fragment, private val sharedViewModel: SharedViewModel
) {

    private var questionAmount = 0
    private var time = 0
    private var examType = ""

    val bottomSheetDialog =
        BottomSheetDialog(fragment.requireContext(), R.style.BottomSheetDailogTheme)
    val bindingExamOption =
        LayoutShowExamOptionBinding.inflate(LayoutInflater.from(fragment.requireContext()))

    fun show() {
        val bottomSheetDialog =
            BottomSheetDialog(fragment.requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption =
            LayoutShowExamOptionBinding.inflate(LayoutInflater.from(fragment.requireContext()))

        bindingExamOption.apply {
            layout25Min.setOnClickListener {
                questionAmount = 50
                time = 1500
                selectIcon(option25Icon)
                eraseIcon(option50Icon, option100Icon)
                examType = "50QuestionExam"
            }
            layout50Min.setOnClickListener {
                questionAmount = 100
                time = 3000
                selectIcon(option50Icon)
                eraseIcon(option25Icon, option100Icon)
                examType = "100QuestionExam"
            }
            layout100Min.setOnClickListener {
                questionAmount = 200
                time = 6000
                selectIcon(option100Icon)
                eraseIcon(option25Icon, option50Icon)
                examType = "200QuestionExam"
            }
            btnExamStart.setOnClickListener {
                if (questionAmount != 0) {
                    val data = SharedData(
                        "সামগ্রিক পরীক্ষা", "normalExam", questionAmount, examType, "", time
                    )
                    sharedViewModel.setSharedData(data)
                    fragment.findNavController()
                        .navigate(R.id.action_homeFragment_to_examFragment)
                    bottomSheetDialog.dismiss()
                } else {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Please select an option",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        }
        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()
    }

    private fun selectIcon(imageView: ImageView) {
        imageView.setImageResource(R.drawable.black_dot)
    }

    fun eraseIcon(imageView: ImageView, imageView2: ImageView) {
        imageView.setImageResource(R.drawable.round_back_white50_100)
        imageView2.setImageResource(R.drawable.round_back_white50_100)
    }

}