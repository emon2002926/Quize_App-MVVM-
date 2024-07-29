package com.gdalamin.bcs_pro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.databinding.ItemSubjectBinding
import com.gdalamin.bcs_pro.model.SubjectName

class SubjectAdapter(val listener: HandleClickListener) :
    BaseAdapter<SubjectName, ItemSubjectBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemSubjectBinding {
        return ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemSubjectBinding, item: SubjectName, position: Int) {

        with(binding) {
            tvSubjectName.text = item.subject_name
            parentLayout.setOnClickListener { listener.onClick(item) }

        }
    }

    interface HandleClickListener {
        fun onClick(subjectName: SubjectName)
    }
}