package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bcsprokotlin.databinding.LiveModelTestLayoutBinding
import com.example.bcsprokotlin.model.LiveExam

class LiveExamAdapter : BaseAdapter<LiveExam, LiveModelTestLayoutBinding>(
    areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }) {
    
    override fun createBinding(parent: ViewGroup, viewType: Int): LiveModelTestLayoutBinding {

        return LiveModelTestLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: LiveModelTestLayoutBinding, item: LiveExam, position: Int) {
        with(binding) {
            examDate.text = item.dailyExam
            examDetails.text = item.details
        }

    }


}