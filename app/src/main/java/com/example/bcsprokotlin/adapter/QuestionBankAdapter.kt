package com.example.bcsprokotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bcsprokotlin.databinding.ItemQuestionBankBinding
import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.util.GeneralUtils

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
            questionBatch.text = item.bcsYearName
            numOfQuestion.text =
                "প্রশ্নের পরিমাণ : ${GeneralUtils.convertEnglishToBengaliNumber(item.totalQuestion)}"
            fullLayout.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface HandleClickListener {
        fun onClick(bscYearName: BcsYearName)
    }


}