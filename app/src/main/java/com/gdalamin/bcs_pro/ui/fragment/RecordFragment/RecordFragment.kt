package com.gdalamin.bcs_pro.ui.fragment.RecordFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.databinding.FragmentRecordBinding
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertEnglishToBangla
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "results_for_statistics"
    var totalExam = 0
    var totalQuestion = 0
    var overAllCorrectAnswer = 0
    var overAllWrongAnswer = 0
    var overAllNotAnswered = 0
    var totalPercentageCorrect: Float = 0.0f
    var totalPercentageWrong: Float = 0.0f
    var totalPercentageNotAnswered: Float = 0.0f
    override fun loadUi() {
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        totalExam = getInt("totalExam", 0)
        totalQuestion = getInt("totalQuestions", 0)
        overAllCorrectAnswer = getInt("overAllCorrectAnswer", 0)
        overAllWrongAnswer = getInt("overAllWrongAnswer", 0)
        overAllNotAnswered = getInt("overAllNotAnswered", 0)
        
        totalPercentageCorrect = (overAllCorrectAnswer.toFloat() / totalQuestion) * 100
        totalPercentageWrong = (overAllWrongAnswer.toFloat() / totalQuestion) * 100
        totalPercentageNotAnswered = (overAllNotAnswered.toFloat() / totalQuestion) * 100
        
        if (totalQuestion > 1) {
            setUpProgressBar()
            
        }
        progress()
    }
    
    
    private fun progress() = binding.apply {
        
        correctProgressBar.setProgress(31)
        correctProgressBar.setProgressColor(
            ContextCompat.getColor(requireContext(), R.color.deepGreen)
        )
        wrongProgressBar.setProgress(Math.round(totalPercentageWrong))
        wrongProgressBar.setProgressColor(
            ContextCompat.getColor(requireContext(), R.color.liteRed)
        )
        notAnsweredProgressBar.setProgress(Math.round(totalPercentageNotAnswered))
        notAnsweredProgressBar.setProgressColor(
            ContextCompat.getColor(requireContext(), R.color.liteTangerine)
        )
//        correctTotalTv.text = convertEnglishToBangla(totalQuestion.toString())
//        wrongTotalTv.text = convertEnglishToBangla(totalQuestion.toString())
//        notAnsweredTotalTv.text = convertEnglishToBangla(totalQuestion.toString())
        
        
        val correctPercentageText = "ম ${
            String.format(
                "%s (%s%%)",
                convertEnglishToBangla(overAllCorrectAnswer.toString()),
                convertEnglishToBangla(String.format("%.2f", totalPercentageCorrect))
            )
        }"
        val wrongAnswerText = String.format(
            "%s (%s%%)", convertEnglishToBangla(overAllWrongAnswer.toString()),
            convertEnglishToBangla(String.format("%.2f", totalPercentageWrong))
        )
        val notAnsweredText = String.format(
            "%s (%s%%)",
            convertEnglishToBangla(overAllNotAnswered.toString()),
            convertEnglishToBangla(String.format("%.2f", totalPercentageNotAnswered))
        )
//        correctPercentageTv.text = correctPercentageText
//        wrongPercentageTv.text = wrongAnswerText
//        notAnsweredPercentageTv.text = notAnsweredText
        
    }
    
    @SuppressLint("DefaultLocale")
    private fun setUpProgressBar() = binding.apply {
        
        
        tvTotalExam.text = convertEnglishToBangla(totalExam.toString())
        tvTotalQuestion.text = convertEnglishToBangla(totalQuestion.toString())
        
        correctAnswerTv.text =
            String.format(
                "%s (%s%%)",
                convertEnglishToBangla(overAllCorrectAnswer.toString()),
                convertEnglishToBangla(String.format("%.2f", totalPercentageCorrect))
            )
        
        wrongAnswerTv.text =
            String.format(
                "%s (%s%%)", convertEnglishToBangla(overAllWrongAnswer.toString()),
                convertEnglishToBangla(String.format("%.2f", totalPercentageWrong))
            )
        
        notAnswered.text = String.format(
            "%s (%s%%)",
            convertEnglishToBangla(overAllNotAnswered.toString()),
            convertEnglishToBangla(String.format("%.2f", totalPercentageNotAnswered))
        )
        
        
        progressBarCorrect.setProgress(Math.round(totalPercentageCorrect))
        progressBarWrong.setProgress(Math.round(totalPercentageWrong))
        progressBarNotAnswred.setProgress(Math.round(totalPercentageNotAnswered))
    }
    
    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
    
}