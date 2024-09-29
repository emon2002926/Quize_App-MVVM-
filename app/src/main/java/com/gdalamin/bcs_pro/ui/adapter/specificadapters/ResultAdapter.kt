package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.data.model.ExamResult
import com.gdalamin.bcs_pro.databinding.ItemResultBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertEnglishToBangla

class ResultAdapter : BaseAdapter<ExamResult, ItemResultBinding>(
    areItemsTheSame = { oldItem, newItem -> oldItem.subjectName == newItem.subjectName },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemResultBinding {
        return ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
    
    override fun bind(binding: ItemResultBinding, item: ExamResult, position: Int) {
        with(binding) {
            tvSubjectName.text = convertEnglishToBangla(item.subjectName)
            tvAnsweredQuestion.text = textConverter(item.answeredQuestions)
            tvCorrectAnswer.text = textConverter(item.correctAnswer)
            tvWrongAnswer.text = textConverter(item.wrongAnswer)
            tvMarks.text = "${item.mark}"
        }
    }
    
    private fun textConverter(int: Int): String =
        convertEnglishToBangla(int.toString())
}
