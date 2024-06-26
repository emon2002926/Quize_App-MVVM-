package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bcsprokotlin.databinding.ItemResultBinding
import com.example.bcsprokotlin.model.ExamResult
import com.example.bcsprokotlin.util.GeneralUtils

class ResultAdapter : BaseAdapter<ExamResult, ItemResultBinding>(
    areItemsTheSame = { oldItem, newItem -> oldItem.subjectName == newItem.subjectName },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemResultBinding {
        return ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemResultBinding, item: ExamResult, position: Int) {
        with(binding) {
            tvSubjectName.text = GeneralUtils.convertEnglishToBengaliNumber(item.subjectName)
            tvAnsweredQuestion.text = textConverter(item.answeredQuestions)
            tvCorrectAnswer.text = textConverter(item.correctAnswer)
            tvWrongAnswer.text = textConverter(item.wrongAnswer)
            tvMarks.text = "${item.mark}"
        }
    }

    private fun textConverter(int: Int): String =
        GeneralUtils.convertEnglishToBengaliNumber(int.toString())
}
