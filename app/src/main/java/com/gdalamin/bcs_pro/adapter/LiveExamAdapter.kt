package com.gdalamin.bcs_pro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.databinding.LiveModelTestLayoutBinding
import com.gdalamin.bcs_pro.model.LiveExam

class LiveExamAdapter(val listener: HandleClickListener) :
    BaseAdapter<LiveExam, LiveModelTestLayoutBinding>(
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }) {
    private var lastPosition = -1


    override fun createBinding(parent: ViewGroup, viewType: Int): LiveModelTestLayoutBinding {

        return LiveModelTestLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: LiveModelTestLayoutBinding, item: LiveExam, position: Int) {
        with(binding) {
            setAnimationEveryTime(binding.root.context, binding.root, position)
            examDate.text = item.dailyExam
            examDetails.text = item.details
            parentLayout.setOnClickListener { listener.onClickLiveExam(item) }
        }

    }

    interface HandleClickListener {
        fun onClickLiveExam(item: LiveExam)
    }

    fun setAnimationEveryTime(ctx: Context?, viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val slideIn = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left)
            viewToAnimate.animation = slideIn
            lastPosition = position
        }
    }

}