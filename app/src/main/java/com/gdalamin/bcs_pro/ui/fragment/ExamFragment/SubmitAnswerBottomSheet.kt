package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import androidx.fragment.app.Fragment

class SubmitAnswerBottomSheet(
    private val fragment: Fragment,
    private val totalQuestion: Int,
    private val answeredQuestions: Int,
    private val resultViewmodel: ResultViewModel
) {

//    @SuppressLint("SetTextI18n")
//    fun submitAnswer() {
//        val bottomSheetDialog = BottomSheetDialog(fragment.requireContext(), R.style.BottomSheetDailogTheme)
//        val bindingResult = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(fragment.requireContext()))
//
//        bindingResult.tvDis.text =
//            "আপনি ${convertEnglishToBangla(totalQuestion.toString())}" +
//                    " প্রশ্নের মধ্যে ${
//                        convertEnglishToBangla(
//                            answeredQuestions.toString()
//                        )
//                    } টি প্রশ্নের উত্তর দিয়েছেন"
//
//        bindingResult.apply {
//            btnSubmit.setOnClickListener {
//                if (answeredQuestions == 0) {
//                    Toast.makeText(
//                        fragment.requireContext(),
//                        "Please answer Question to see result",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    resultViewmodel.overallResult.observe(fragment.viewLifecycleOwner) {
//                        saveResultForStatic(it)
//                    }
//                    bottomSheetDialog.dismiss()
//                    //old
//                    binding.tvTimer.visibility = View.GONE
//                    showResultView()
//                }
//            }
//        }
//        bottomSheetDialog.setContentView(bindingResult.root)
//        bottomSheetDialog.show()
//    }
}