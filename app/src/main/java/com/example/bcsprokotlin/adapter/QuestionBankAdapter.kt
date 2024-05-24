package com.example.bcsprokotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.databinding.ItemQuestionBankBinding
import com.example.bcsprokotlin.model.BcsYearName
import com.example.bcsprokotlin.util.GeneralUtils
import javax.inject.Inject

class QuestionBankAdapter @Inject constructor(private val converter: GeneralUtils) :
    RecyclerView.Adapter<QuestionBankAdapter.QuestionBankViwHolder>() {

    inner class QuestionBankViwHolder(val binding: ItemQuestionBankBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<BcsYearName>() {
        override fun areItemsTheSame(oldItem: BcsYearName, newItem: BcsYearName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BcsYearName, newItem: BcsYearName): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionBankViwHolder {
        return QuestionBankViwHolder(
            ItemQuestionBankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionBankViwHolder, position: Int) {

        differ.currentList.let { bcsYearNames ->

            with(holder.binding) {
                questionBatch.text = bcsYearNames[position].bcsYearName
                numOfQuestion.text =
                    "প্রশ্নের পরিমাণ : ${GeneralUtils.convertEnglishToBengaliNumber(bcsYearNames[position].totalQuestion)}"
            }
        }
    }

}