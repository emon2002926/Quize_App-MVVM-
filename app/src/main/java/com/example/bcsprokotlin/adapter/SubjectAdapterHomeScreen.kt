package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.databinding.SubjectItemLinerBinding
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectName

class SubjectAdapterHomeScreen :
    RecyclerView.Adapter<SubjectAdapterHomeScreen.SubjectHomeScreenViewHolder>() {
    inner class SubjectHomeScreenViewHolder(val binding: SubjectItemLinerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SubjectName>() {
        override fun areItemsTheSame(oldItem: SubjectName, newItem: SubjectName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubjectName, newItem: SubjectName): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectHomeScreenViewHolder {
        return SubjectHomeScreenViewHolder(
            SubjectItemLinerBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SubjectHomeScreenViewHolder, position: Int) {
        differ.currentList[position].let { subjectName ->
            with(holder.binding) {
                tvSubjectName.text = subjectName.subject_name
            }
        }
    }
}