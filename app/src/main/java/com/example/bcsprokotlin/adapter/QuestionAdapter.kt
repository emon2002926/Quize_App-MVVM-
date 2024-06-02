package com.example.bcsprokotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    override fun bind(binding: McqLayoutBinding, item: Question, position: Int) {

        val context = binding.fullLayout.context

        var userSelectedAnswer = item.userSelectedAnswer
        val correctAnswer = item.answer.toInt()

        with(binding) {

            explainIv.visibility = View.GONE

            tvQuestionPosition.text =
                GeneralUtils.convertEnglishToBengaliNumber("${position + 1})")

            showImgOrTextView(item.question, item.image, questionIv, questionTv)
            showImgOrTextView(item.option1, item.option1Image, option1Iv, option1Tv)
            showImgOrTextView(item.option2, item.option2Image, option2Iv, option2Tv)
            showImgOrTextView(item.option3, item.option3Image, option3Iv, option3Tv)
            showImgOrTextView(item.option4, item.option4Image, option4Iv, option4Tv)

            // Reset all options to default state
            resetOption(option1Layout, option1Icon)
            resetOption(option2Layout, option2Icon)
            resetOption(option3Layout, option3Icon)
            resetOption(option4Layout, option4Icon)

            // Handle option selection
            option1Layout.setOnClickListener {
                if (item.userSelectedAnswer == 0) selectOption(
                    1,
                    item,
                    option1Layout,
                    option1Icon,
                    binding
                )

//                if (item.userSelectedAnswer == 0) {
//                    correctAnswer(1, item, option1Layout, option1Icon)
//                }
            }
            option2Layout.setOnClickListener {
                if (item.userSelectedAnswer == 0) selectOption(
                    2,
                    item,
                    option2Layout,
                    option2Icon,
                    binding
                )
            }
            option3Layout.setOnClickListener {
                if (item.userSelectedAnswer == 0) selectOption(
                    3,
                    item,
                    option3Layout,
                    option3Icon,
                    binding
                )
            }
            option4Layout.setOnClickListener {
                if (item.userSelectedAnswer == 0) selectOption(
                    4,
                    item,
                    option4Layout,
                    option4Icon,
                    binding
                )
            }

            // Highlight selected option and disable others
            if (userSelectedAnswer > 0) {
                when (userSelectedAnswer) {
                    1 -> {
                        if (correctAnswer == 1) {

                        }
                        markSelectedOption(option1Layout, option1Icon)
                    }

                    2 -> markSelectedOption(option2Layout, option2Icon)
                    3 -> markSelectedOption(option3Layout, option3Icon)
                    4 -> markSelectedOption(option4Layout, option4Icon)
                }
                disableAllOptions(binding)
            }
        }
    }

    private fun selectOption(
        selectedOption: Int,
        item: Question,
        optionLayout: View,
        optionIcon: ImageView,
        binding: McqLayoutBinding
    ) {
        item.userSelectedAnswer = selectedOption
        markSelectedOption(optionLayout, optionIcon)
        disableAllOptions(binding)
    }

    private fun markSelectedOption(optionLayout: View, image: ImageView) {
        optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
        image.setImageResource(R.drawable.black_dot)
    }

    private fun correctAnswer(
        selectedOption: Int,
        item: Question,
        optionLayout: View, image: ImageView
    ) {
        item.userSelectedAnswer = selectedOption
        if (item.answer.toInt() == item.userSelectedAnswer) {
            optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
            image.setImageResource(R.drawable.black_dot)
        } else {
            optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
            image.setImageResource(R.drawable.baseline_close_24)
        }

    }

    private fun resetOption(optionLayout: View, optionIcon: ImageView) {
        optionIcon.setImageResource(0)
        optionLayout.setBackgroundResource(0)
        optionLayout.isEnabled = true
    }

    private fun disableAllOptions(binding: McqLayoutBinding) {
        with(binding) {
            option1Layout.isEnabled = false
            option2Layout.isEnabled = false
            option3Layout.isEnabled = false
            option4Layout.isEnabled = false
        }
    }

    private fun showImgOrTextView(
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

