package com.example.bcsprokotlin.adapter

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.ItemSubjectBinding
import com.example.bcsprokotlin.model.SubjectName
import javax.security.auth.Subject

class SubjectAdapter:RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(val binding:ItemSubjectBinding):RecyclerView.ViewHolder(binding.root)



    private val differCallback = object :DiffUtil.ItemCallback<SubjectName>(){
        override fun areItemsTheSame(oldItem: SubjectName, newItem: SubjectName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubjectName, newItem: SubjectName): Boolean {
            return  oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun getItemCount()=differ.currentList.size


    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {

        differ.currentList[position].let { subjectList->

            holder.binding.apply {
                val contex = parentLayout.context
//                ivSubjectIcon.setImageResource(subjectList.imageId)
                tvSubjectName.text=subjectList.subjects
                Log.d("dksdfhj",subjectList.subjects)

//                val gradientDrawable = GradientDrawable().apply {
//                    shape = GradientDrawable.RECTANGLE
//                    colors = intArrayOf(
//                        ContextCompat.getColor(contex, subjectList.startColor),
//                        ContextCompat.getColor(contex, subjectList.endColor)
//                    ) // Start and end colors
//                    cornerRadius = contex.resources.getDimension(R.dimen.corner_radius) // Corner radius in dp
//                    setStroke(
//                        contex.resources.getDimensionPixelSize(R.dimen.stroke_width),
//                        ContextCompat.getColor(contex, R.color.white)
//                    ) // Stroke width and color
//                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
//                }

//                parentLayout.background = gradientDrawable

            }


        }
    }
}