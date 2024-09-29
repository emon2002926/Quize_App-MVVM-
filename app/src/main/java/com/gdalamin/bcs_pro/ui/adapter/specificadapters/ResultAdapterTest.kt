package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.data.model.ExamInfoTest
import com.gdalamin.bcs_pro.databinding.ItemResultBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertEnglishToBangla

class ResultAdapterTest : BaseAdapter<ExamInfoTest, ItemResultBinding>(
    areItemsTheSame = { oldItem, newItem -> oldItem.subjectName == newItem.subjectName },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemResultBinding {
        return ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
    
    override fun bind(binding: ItemResultBinding, item: ExamInfoTest, position: Int) {
        with(binding) {
            tvSubjectName.text = convertEnglishToBangla(item.subjectName)
            tvAnsweredQuestion.text = textConverter(item.totalSelectedAnswer)
            tvCorrectAnswer.text = textConverter(item.totalCorrectAnswer)
            tvWrongAnswer.text = textConverter(item.totalWrongAnswer)
            tvMarks.text = convertEnglishToBangla(item.totalMark.toString())
        }
        
    }
    
    
    private fun textConverter(int: Int): String =
        convertEnglishToBangla(int.toString())
}
