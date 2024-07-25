package com.gdalamin.bcs_pro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.databinding.ItemQuestionBankBinding
import com.gdalamin.bcs_pro.model.BcsYearName
import com.gdalamin.bcs_pro.util.Animations.setAnimationFadeIn
import com.gdalamin.bcs_pro.util.GeneralUtils

class QuestionBankAdapter(val listener: HandleClickListener) :
    BaseAdapter<BcsYearName, ItemQuestionBankBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
    ) {


    override fun createBinding(parent: ViewGroup, viewType: Int): ItemQuestionBankBinding {

        return ItemQuestionBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemQuestionBankBinding, item: BcsYearName, position: Int) {
        with(binding) {

            setAnimationFadeIn(binding.root.context, binding.root, position)

            questionBatch.text = item.bcsYearName
            numOfQuestion.text =
                "প্রশ্নের পরিমাণ : ${GeneralUtils.convertEnglishToBangla(item.totalQuestion)}"
            fullLayout.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface HandleClickListener {
        fun onClick(bscYearName: BcsYearName)
    }


}