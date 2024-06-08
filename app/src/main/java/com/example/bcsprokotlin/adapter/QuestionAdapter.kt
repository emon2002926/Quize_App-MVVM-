package com.example.bcsprokotlin.adapter

import android.content.Context
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

    override fun bind(binding: McqLayoutBinding, item: Question, position: Int) {

        val context = binding.fullLayout.context

        with(binding) {
            explainIv.visibility = View.GONE

            tvQuestionPosition.text =
                GeneralUtils.convertEnglishToBengaliNumber("${position + 1})")

            showImgOrTextView(item.question, item.image, questionIv, questionTv)
            showImgOrTextView("ক) ${item.option1}", item.option1Image, option1Iv, option1Tv)
            showImgOrTextView("খ) ${item.option2}", item.option2Image, option2Iv, option2Tv)
            showImgOrTextView("গ) ${item.option3}", item.option3Image, option3Iv, option3Tv)
            showImgOrTextView("ঘ) ${item.option4}", item.option4Image, option4Iv, option4Tv)

            // Reset all options to default state
            resetOption(option1Layout, option1Icon, option1Tv, context)
            resetOption(option2Layout, option2Icon, option2Tv, context)
            resetOption(option3Layout, option3Icon, option3Tv, context)
            resetOption(option4Layout, option4Icon, option4Tv, context)

            // Apply saved state
            if (item.userSelectedAnswer != 0) {

                // Highlight the correct answer
                when (item.userSelectedAnswer) {

                    1 -> {
                        disableAllOptions(binding)
                        highlightAnswers(binding, item, context)
                        if (item.userSelectedAnswer == item.answer.toInt()) {
                            correctAnswer(option1Layout, option1Icon, option1Tv)
                        } else {
                            wrongAnswer(option1Layout, option1Icon, option1Tv)
                        }
//                        applySelectionState(option1Layout, option1Icon, option1Tv, item, context)
                    }

                    2 -> {
                        disableAllOptions(binding)
                        highlightAnswers(binding, item, context)
                        if (item.userSelectedAnswer == item.answer.toInt()) {
                            correctAnswer(option2Layout, option2Icon, option2Tv)
                        } else {
                            wrongAnswer(option2Layout, option2Icon, option2Tv)
                        }
                    }

                    3 -> {
                        disableAllOptions(binding)
                        highlightAnswers(binding, item, context)
                        if (item.userSelectedAnswer == item.answer.toInt()) {
                            correctAnswer(option3Layout, option3Icon, option3Tv)
                        } else {
                            wrongAnswer(option3Layout, option3Icon, option3Tv)
                        }
                    }

                    4 -> {
                        disableAllOptions(binding)
                        highlightAnswers(binding, item, context)
                        if (item.userSelectedAnswer == item.answer.toInt()) {
                            correctAnswer(option4Layout, option4Icon, option4Tv)
                        } else {
                            wrongAnswer(option4Layout, option4Icon, option4Tv)
                        }
                    }
                }
            }

            val onClickListener = View.OnClickListener { view ->
                showImgOrTextView(item.explanation, item.explanationImage, explainIv, explainTv)
                // Determine selected option
                var selectedOption = 0
                var img: ImageView? = null
                var option: TextView? = null
                when (view) {
                    option1Layout -> {
                        selectedOption = 1
                        img = option1Icon
                        option = option1Tv
                    }

                    option2Layout -> {
                        selectedOption = 2
                        img = option2Icon
                        option = option2Tv
                    }

                    option3Layout -> {
                        selectedOption = 3
                        img = option3Icon
                        option = option3Tv
                    }

                    option4Layout -> {
                        selectedOption = 4
                        img = option4Icon
                        option = option4Tv
                    }
                }
                item.userSelectedAnswer = selectedOption

                // Highlight correct and wrong answers
                highlightAnswers(binding, item, context)

                // Highlight selected option
                if (img != null && option != null) {
                    if (selectedOption == item.answer.toInt()) {
                        correctAnswer(view, img, option)
                    } else {
                        wrongAnswer(view, img, option)
                    }
                }

                disableAllOptions(binding)
            }

            option1Layout.setOnClickListener(onClickListener)
            option2Layout.setOnClickListener(onClickListener)
            option3Layout.setOnClickListener(onClickListener)
            option4Layout.setOnClickListener(onClickListener)
        }
    }

    private fun correctAnswer(optionLayout: View, imageView: ImageView, option: TextView) {
        optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
        imageView.setImageResource(R.drawable.baseline_check_24)
        option.setTextColor(ContextCompat.getColor(option.context, R.color.light_green))
    }

    private fun wrongAnswer(optionLayout: View, imageView: ImageView, option: TextView) {
        optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
        imageView.setImageResource(R.drawable.baseline_close_24)
        option.setTextColor(ContextCompat.getColor(option.context, R.color.end_color_five))
    }

    private fun makeGrayText(optionLayout: View, textView: TextView, context: Context) {
        optionLayout.setBackgroundResource(R.drawable.gray_baground)
        textView.setTextColor(ContextCompat.getColor(context, R.color.liteGray))
    }

    private fun resetOption(
        optionLayout: View,
        optionIcon: ImageView,
        optionText: TextView,
        context: Context
    ) {
        optionIcon.setImageResource(0)
        optionLayout.setBackgroundResource(0) // Default background resource
        optionText.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.LiteBlack
            )
        ) // Default text color resource
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


    private fun highlightAnswers(binding: McqLayoutBinding, item: Question, context: Context) {
        with(binding) {
            // Reset all options to default state

//            resetOption(option1Layout, option1Icon, option1Tv, context)
//            resetOption(option2Layout, option2Icon, option2Tv, context)
//            resetOption(option3Layout, option3Icon, option3Tv, context)
//            resetOption(option4Layout, option4Icon, option4Tv, context)

            // Highlight the correct answer
            when (item.answer.toInt()) {
                1 -> correctAnswer(option1Layout, option1Icon, option1Tv)
                2 -> correctAnswer(option2Layout, option2Icon, option2Tv)
                3 -> correctAnswer(option3Layout, option3Icon, option3Tv)
                4 -> correctAnswer(option4Layout, option4Icon, option4Tv)
            }
            // Gray out the incorrect answers
            if (item.answer.toInt() != 1) makeGrayText(option1Layout, option1Tv, context)
            if (item.answer.toInt() != 2) makeGrayText(option2Layout, option2Tv, context)
            if (item.answer.toInt() != 3) makeGrayText(option3Layout, option3Tv, context)
            if (item.answer.toInt() != 4) makeGrayText(option4Layout, option4Tv, context)
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
