package com.example.bcsprokotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.McqLayoutBinding
import com.example.bcsprokotlin.model.Question

class QuestionAdapter:RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    inner class QuestionViewHolder(val binding:McqLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object :DiffUtil.ItemCallback<Question>(){
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem==newItem
        }
    }
    val differ =AsyncListDiffer(this,differCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(McqLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int =differ.currentList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {

        val context = holder.binding.fullLayout.context
        with(holder.binding){
            option1Layout.setBackgroundResource(0)
            option2Layout.setBackgroundResource(0)
            option3Layout.setBackgroundResource(0)
            option4Layout.setBackgroundResource(0)

            option1Icon.setImageResource(R.drawable.round_back_white50_100)
            option2Icon.setImageResource(R.drawable.round_back_white50_100)
            option3Icon.setImageResource(R.drawable.round_back_white50_100)
            option4Icon.setImageResource(R.drawable.round_back_white50_100)

            option1Layout.setEnabled(true)
            option2Layout.setEnabled(true)
            option3Layout.setEnabled(true)
            option4Layout.setEnabled(true)

            option1Tv.setTextColor(ContextCompat.getColor(context, R.color.LiteBlack))
            option2Tv.setTextColor(ContextCompat.getColor(context, R.color.LiteBlack))
            option3Tv.setTextColor(ContextCompat.getColor(context, R.color.LiteBlack))
            option4Tv.setTextColor(ContextCompat.getColor(context, R.color.LiteBlack))

            explainIV.visibility = View.GONE



        }



        differ.currentList[position].let { questionItem->

            Log.d("questinddd",questionItem.question)
            holder.binding.apply {
                textViewPosition2.text = "${position+1}"
                questionTv.text = questionItem.question
                option1Tv.text = questionItem.option1
                option2Tv.text = questionItem.option2
                option3Tv.text = questionItem.option3
                option4Tv.text = questionItem.option4
            }
        }
    }

}