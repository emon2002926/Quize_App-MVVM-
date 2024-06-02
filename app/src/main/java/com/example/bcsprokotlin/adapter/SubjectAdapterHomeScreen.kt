package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bcsprokotlin.databinding.SubjectItemLinerBinding
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectName

class SubjectAdapterHomeScreen(val listener: HandleClickListener) :
    BaseAdapter<SubjectName, SubjectItemLinerBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    ) {


    override fun createBinding(parent: ViewGroup, viewType: Int): SubjectItemLinerBinding {
        return SubjectItemLinerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: SubjectItemLinerBinding, item: SubjectName, position: Int) {
        with(binding) {
            tvSubjectName.text = item.subject_name
            parentLayout.setOnClickListener { listener.onSubjectClick(item) }
        }
    }

    interface HandleClickListener {
        fun onSubjectClick(subjectName: SubjectName)
    }
}