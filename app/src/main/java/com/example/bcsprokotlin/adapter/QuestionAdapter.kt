package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.McqLayoutBinding
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.util.GeneralUtils

class QuestionAdapter : BaseAdapter<Question, McqLayoutBinding>(
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
    areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): McqLayoutBinding {
        return McqLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: McqLayoutBinding, questionList: Question, position: Int) {

        val context = binding.fullLayout.context

        with(binding) {
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
            explainIv.visibility = View.GONE

            
            tvQuestionPosition.text =
                GeneralUtils.convertEnglishToBengaliNumber("${position + 1}" + ")")

            //For Question
            showImgOrTextView(questionList.question, questionList.image, questionIv, questionTv)
            //For Option1
            showImgOrTextView(
                questionList.option1,
                questionList.option1Image,
                option1Iv,
                option1Tv
            )
            //For Option2
            showImgOrTextView(
                questionList.option2,
                questionList.option2Image,
                option2Iv,
                option2Tv
            )
            //For Option3
            showImgOrTextView(
                questionList.option3,
                questionList.option3Image,
                option3Iv,
                option3Tv
            )
            //For Option4
            showImgOrTextView(
                questionList.option4,
                questionList.option4Image,
                option4Iv,
                option4Tv
            )
            //For Explain
            showImgOrTextView(
                questionList.explanation,
                questionList.explanationImage,
                explainIv,
                explainTv
            )


        }
    }

    fun showImgOrTextView(
        text: String,
        base64ImageString: String,
        imageView: ImageView,
        textView: TextView
    ) {
        if (text.isNotEmpty()) {
            imageView.visibility = View.GONE
            textView.text = text
            textView.visibility = View.VISIBLE
        } else {
            if (base64ImageString.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                textView.visibility = View.GONE
                imageView.setImageBitmap(GeneralUtils.convertBase64ToBitmap(base64ImageString))
            }

        }

    }

}