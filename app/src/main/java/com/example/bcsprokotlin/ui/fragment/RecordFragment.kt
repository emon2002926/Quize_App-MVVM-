package com.example.bcsprokotlin.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.bcsprokotlin.databinding.FragmentRecordBinding
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils.convertEnglishToBengaliNumber
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

        setUpProgressBar()
    }

    @SuppressLint("DefaultLocale")
    private fun setUpProgressBar() = binding.apply {

        tvTotalExam.text = convertEnglishToBengaliNumber(totalExam.toString())
        tvTotalQuestion.text = convertEnglishToBengaliNumber(totalQuestion.toString())

        correctAnswerTv.text = String.format(
            "%s (%s%%)",
            convertEnglishToBengaliNumber(overAllCorrectAnswer.toString()),
            convertEnglishToBengaliNumber(String.format("%.2f", totalPercentageCorrect))
        )
        wrongAnswerTv.text =
            String.format(
                "%s (%s%%)", convertEnglishToBengaliNumber(overAllWrongAnswer.toString()),
                convertEnglishToBengaliNumber(String.format("%.2f", totalPercentageWrong))
            )

        notAnswered.text = String.format(
            "%s (%s%%)",
            convertEnglishToBengaliNumber(overAllNotAnswered.toString()),
            convertEnglishToBengaliNumber(String.format("%.2f", totalPercentageNotAnswered))
        )


        progressBarCorrect.setProgress(Math.round(totalPercentageCorrect))
        progressBarWrong.setProgress(Math.round(totalPercentageWrong))
        progressBarNotAnswred.setProgress(Math.round(totalPercentageNotAnswered))
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

}