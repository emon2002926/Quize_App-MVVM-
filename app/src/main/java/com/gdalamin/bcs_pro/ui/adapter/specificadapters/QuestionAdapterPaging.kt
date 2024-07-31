package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.databinding.McqLayoutBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapterPaging
import com.gdalamin.bcs_pro.ui.utilities.Animations
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.convertEnglishToBangla

class QuestionAdapterPaging : BaseAdapterPaging<Question, McqLayoutBinding>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }
    }


    private var showAnswers: Boolean = false

    fun showAnswer(show: Boolean) {
        showAnswers = show
        notifyDataSetChanged()
    }

    private var showExamUi = ""

    fun changeUiForExam(show: String) {
        showExamUi = show
        notifyDataSetChanged()
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): McqLayoutBinding {
        return McqLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: McqLayoutBinding, item: Question, position: Int) {
        with(binding) {
            explainIv.visibility = View.GONE
            Animations.setAnimationFadeIn(binding.root.context, binding.root, position)

            tvQuestionPosition.text = convertEnglishToBangla("${position + 1})")
            // Set question and options
            bindQuestionAndOptions(item, this)
            // Reset options to default state
            resetOptions(this)

            showExplanation(showAnswers, item, this)

            if (item.userSelectedAnswer != 0) {
                disableOptions(this)
                highlightUserSelection(this, item)
            }

            val onClickListener = View.OnClickListener { view ->
                showImgOrTextView(
                    item.explanation,
                    item.explanationImage,
                    explainIv,
                    explainTv
                )
                item.userSelectedAnswer = getOptionIndex(view, this)
                highlightUserSelection(this, item)
                disableOptions(this)
            }
            // Set click listeners for all option layouts
            setOptionClickListeners(this, onClickListener)
        }
    }

    interface OnItemSelectedListenerPaging {
        fun onItemSelected2(item: Question)
    }

    private fun showExplanation(show: Boolean, item: Question, binding: McqLayoutBinding) {
        if (show) {
            disableOptions(binding)
            showImgOrTextView(
                item.explanation,
                item.explanationImage,
                binding.explainIv,
                binding.explainTv
            )
            highlightCorrectAnswer(binding, item)
        } else {
            binding.explainIv.visibility = View.GONE
            binding.explainTv.visibility = View.GONE
        }
    }

    private fun bindQuestionAndOptions(item: Question, binding: McqLayoutBinding) {
        with(binding) {
            showImgOrTextView(item.question, item.image, questionIv, questionTv)
            showImgOrTextView(item.option1, item.option1Image, option1Iv, option1Tv)
            showImgOrTextView(item.option2, item.option2Image, option2Iv, option2Tv)
            showImgOrTextView(item.option3, item.option3Image, option3Iv, option3Tv)
            showImgOrTextView(item.option4, item.option4Image, option4Iv, option4Tv)
        }
    }

    private fun resetOptions(binding: McqLayoutBinding) {
        with(binding) {
            resetOption(option1Layout, option1Icon, option1Tv, option1Layout.context)
            resetOption(option2Layout, option2Icon, option2Tv, option2Layout.context)
            resetOption(option3Layout, option3Icon, option3Tv, option3Layout.context)
            resetOption(option4Layout, option4Icon, option4Tv, option4Layout.context)
        }
    }

    private fun highlightCorrectAnswer(binding: McqLayoutBinding, item: Question) {
        with(binding) {
            val correctAnswer = item.answer.toInt()
            highlightAnswer(option1Layout, option1Icon, option1Tv, 1, correctAnswer)
            highlightAnswer(option2Layout, option2Icon, option2Tv, 2, correctAnswer)
            highlightAnswer(option3Layout, option3Icon, option3Tv, 3, correctAnswer)
            highlightAnswer(option4Layout, option4Icon, option4Tv, 4, correctAnswer)
        }
    }

    private fun highlightUserSelection(binding: McqLayoutBinding, item: Question) {
        val userAnswer = item.userSelectedAnswer
        val correctAnswer = item.answer.toInt()
        with(binding) {
            highlightAnswer(option1Layout, option1Icon, option1Tv, 1, correctAnswer, userAnswer)
            highlightAnswer(option2Layout, option2Icon, option2Tv, 2, correctAnswer, userAnswer)
            highlightAnswer(option3Layout, option3Icon, option3Tv, 3, correctAnswer, userAnswer)
            highlightAnswer(option4Layout, option4Icon, option4Tv, 4, correctAnswer, userAnswer)
        }
    }

    private fun highlightAnswer(
        layout: View,
        icon: ImageView,
        text: TextView,
        optionIndex: Int,
        correctAnswer: Int,
        userAnswer: Int = 0
    ) {
        if (optionIndex == correctAnswer) {
            correctAnswer(layout, icon, text)
        } else if (userAnswer != 0 && optionIndex == userAnswer) {
            wrongAnswer(layout, icon, text)
        } else {
            makeGrayText(layout, text, layout.context)
        }
    }

    private fun setOptionClickListeners(
        binding: McqLayoutBinding,
        onClickListener: View.OnClickListener
    ) {
        with(binding) {
            option1Layout.setOnClickListener(onClickListener)
            option2Layout.setOnClickListener(onClickListener)
            option3Layout.setOnClickListener(onClickListener)
            option4Layout.setOnClickListener(onClickListener)
        }
    }

    private fun getOptionIndex(view: View, binding: McqLayoutBinding): Int {
        return when (view) {
            binding.option1Layout -> 1
            binding.option2Layout -> 2
            binding.option3Layout -> 3
            binding.option4Layout -> 4
            else -> 0
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
        optionLayout.setBackgroundResource(0)
        optionText.setTextColor(ContextCompat.getColor(context, R.color.LiteBlack))
        optionLayout.isEnabled = true
    }

    private fun disableOptions(binding: McqLayoutBinding) {
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
        } else if (base64ImageString.isNotEmpty()) {
            imageView.visibility = View.VISIBLE
            textView.visibility = View.GONE
            imageView.setImageBitmap(GeneralUtils.convertBase64ToBitmap(base64ImageString))
        }
    }

    private fun highLightClickedOptionForExam(
        optionLayout: View,
        optionIcon: ImageView
    ) {
        optionIcon.setImageResource(R.drawable.black_dot)
        optionLayout.setBackgroundResource(R.drawable.round_back_selected_option)
        optionLayout.isEnabled = false
    }
}
