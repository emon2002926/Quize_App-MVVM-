package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.databinding.LiveModelTestLayoutBinding
import com.example.bcsprokotlin.model.LiveExam

class LiveExamAdapter : RecyclerView.Adapter<LiveExamAdapter.LiveExamViewHolder>() {
    inner class LiveExamViewHolder(val binding: LiveModelTestLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallback = object : DiffUtil.ItemCallback<LiveExam>() {
        override fun areItemsTheSame(oldItem: LiveExam, newItem: LiveExam): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LiveExam, newItem: LiveExam): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveExamViewHolder {

        return LiveExamViewHolder(
            LiveModelTestLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: LiveExamViewHolder, position: Int) {

        differ.currentList[position].let { liveExam ->
            holder.binding.apply {
                examDate.text = liveExam.dailyExam
                examDetails.text = liveExam.details
            }

        }
    }
}