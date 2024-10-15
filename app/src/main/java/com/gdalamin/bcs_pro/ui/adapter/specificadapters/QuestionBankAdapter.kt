package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.BcsYearName
import com.gdalamin.bcs_pro.databinding.ItemQuestionBankBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.utilities.Animations.setAnimationFadeIn
import com.gdalamin.bcs_pro.utilities.GeneralUtils

class QuestionBankAdapter(private val listener: HandleClickListener) :
    BaseAdapter<BcsYearName, ItemQuestionBankBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
    ) {

    var isAnyItemDownloading = false

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemQuestionBankBinding {
        return ItemQuestionBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemQuestionBankBinding, item: BcsYearName, position: Int) {
        with(binding) {

            if (!item.isAnimated) {
                setAnimationFadeIn(binding.root.context, binding.root, position)
                item.isAnimated = true
            }

            // Update UI based on download and saved states
            when {
                item.isQuestionSaved -> {
                    savedImage.setImageResource(R.drawable.saved)
                    savedImage.visibility = View.VISIBLE
                    animationView.visibility = View.GONE
                    downloadNowImage.visibility = View.GONE
                }

                item.isDownloading -> {
                    // Handle downloading state
                    animationView.apply {
                        visibility = View.VISIBLE
                        isEnabled = false
                        if (!isAnimating) playAnimation() // Ensure animation resumes
                    }
                    savedImage.visibility = View.GONE
                    downloadNowImage.visibility = View.GONE


                }

                else -> {
                    // Handle ready-to-download state
                    animationView.apply {
                        visibility = View.GONE
                        cancelAnimation() // Stop animation
                    }
                    savedImage.visibility = View.GONE
                    downloadNowImage.visibility = View.VISIBLE

                    downloadNowImage.setOnClickListener {
                        if (isAnyItemDownloading) {
                            Toast.makeText(
                                binding.root.context,
                                "Only one download is allowed at a time. Please wait for the current download to finish.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            animationView.playAnimation()
                            animationView.visibility = View.VISIBLE
                            downloadNowImage.visibility = View.GONE
                            item.isDownloading = true
                            isAnyItemDownloading = true // Lock to prevent other downloads
                            listener.onClickDownload(item)
                        }
                    }
                }
            }

            // Set text and other UI elements
            questionBatch.text = item.bcsYearName
            numOfQuestion.text =
                "প্রশ্নের পরিমাণ : ${GeneralUtils.convertEnglishToBangla(item.totalQuestion)}"

            fullLayout.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    // Method to get an item by its ID
    fun getItemById(id: Int): BcsYearName? {
        return differ.currentList.find { it.id == id }
    }

    // Handle download completion
    fun onDownloadComplete(item: BcsYearName) {
        val position = differ.currentList.indexOf(item)
        if (position != -1) {
            item.isDownloading = false
            isAnyItemDownloading = false
            notifyItemChanged(position)
        }
    }

    // Handle download failure
    fun onDownloadFailed(item: BcsYearName) {
        val position = differ.currentList.indexOf(item)
        if (position != -1) {
            item.isDownloading = false
            isAnyItemDownloading = false
            notifyItemChanged(position)
        }
    }

    // Interface for click handling
    interface HandleClickListener {
        fun onClick(bcsYearName: BcsYearName)
        fun onClickDownload(bcsYearName: BcsYearName)
    }
}
