package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.databinding.SubjectItemLinerBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.utilities.Animations.setAnimationLeftIn

class SubjectAdapterHomeScreen(val listener: HandleClickListener) :
    BaseAdapter<SubjectName, SubjectItemLinerBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    ) {
    
    private var lastPosition = -1
    
    
    override fun createBinding(parent: ViewGroup, viewType: Int): SubjectItemLinerBinding {
        return SubjectItemLinerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
    
    override fun bind(binding: SubjectItemLinerBinding, item: SubjectName, position: Int) {
        
        with(binding) {
            
            setAnimationLeftIn(binding.root.context, binding.root, position)
            
            tvSubjectName.text = item.subject_name
            parentLayout.setOnClickListener { listener.onSubjectClick(item) }
        }
    }
    
    interface HandleClickListener {
        fun onSubjectClick(subjectName: SubjectName)
    }

//    fun setAnimation(ctx: Context?, viewToAnimate: View, position: Int) {
//        if (position > lastPosition) {
//            val slideIn = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left)
//            viewToAnimate.animation = slideIn
//            lastPosition = position
//        }
//    }

}