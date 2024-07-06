package com.example.bcsprokotlin.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import com.example.bcsprokotlin.databinding.FragmentRecordBinding
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "results_for_statistics"

    override fun loadUi() {
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        GeneralUtils.logger("totalExam:  ${getInt("totalExam", 0)}")
        GeneralUtils.logger("totalQuestions:  ${getInt("totalQuestions", 0)}")
        GeneralUtils.logger("overAllCorrectAnswer:  ${getInt("overAllCorrectAnswer", 0)}")
        GeneralUtils.logger("overAllWrongAnswer:  ${getInt("overAllWrongAnswer", 0)}")
        GeneralUtils.logger("overAllNotAnswered:  ${getInt("overAllNotAnswered", 0)}")

        setUpProgressBar()
    }

    private fun setUpProgressBar() = binding.apply {

        progressBarCorrect.setProgress(getInt("overAllCorrectAnswer", 0))
        progressBarWrong.setProgress(getInt("overAllWrongAnswer", 0))
        progressBarNotAnswred.setProgress(getInt("overAllNotAnswered", 0))
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

}