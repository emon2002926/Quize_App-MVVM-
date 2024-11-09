package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.databinding.ItemSubjectBinding
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_BANNER_AD_ID
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// Define the adapter
class SubjectWithAdsAdapter(
    private val listener: HandleClickListener
) : ListAdapter<Any, RecyclerView.ViewHolder>(DiffCallback()) {
    
    companion object {
        const val VIEW_TYPE_SUBJECT = 0
        const val VIEW_TYPE_AD = 1
    }
    
    // ViewHolder for SubjectName items
    inner class SubjectViewHolder(private val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubjectName) {
            binding.tvSubjectName.text = item.subject_name
            binding.parentLayout.setOnClickListener { listener.onClick(item) }
        }
    }
    
    // ViewHolder for AdView
    inner class AdViewHolder(val adView: AdView) : RecyclerView.ViewHolder(adView)
    
    // Determine view type for each position
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is AdViewPlaceholder) VIEW_TYPE_AD else VIEW_TYPE_SUBJECT
    }
    
    // Create ViewHolder based on view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SUBJECT -> {
                val binding =
                    ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SubjectViewHolder(binding)
            }
            
            VIEW_TYPE_AD -> {
                val adView = AdView(parent.context).apply {
                    adUnitId = ADMOB_BANNER_AD_ID // Replace with your actual Ad Unit ID
                    setAdSize(AdSize.BANNER)
                    loadAd(AdRequest.Builder().build())
                }
                AdViewHolder(adView)
            }
            
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    
    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SubjectViewHolder -> {
                val subject = getItem(position) as SubjectName
                holder.bind(subject)
            }
            
            is AdViewHolder -> {
                // Ensure ads are not recycled
                val adView = holder.adView
                if (adView.parent != null) {
                    (adView.parent as ViewGroup).removeView(adView) // Avoid view duplication
                }
            }
        }
    }
    
    // Define the DiffUtil callback
    class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is SubjectName && newItem is SubjectName -> oldItem.id == newItem.id
                oldItem is AdViewPlaceholder && newItem is AdViewPlaceholder -> true
                else -> false
            }
        }
        
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem != newItem // Correct this to ensure content comparison
        }
    }
    
    // Method to set subjects with ads
    fun setSubjectsWithAds(subjectList: List<SubjectName>) {
        val itemsWithAds = mutableListOf<Any>()
        for (i in subjectList.indices) {
            if (i != 0 && i % 5 == 0) {
                // Insert an AdViewPlaceholder after every 5 items
                itemsWithAds.add(AdViewPlaceholder())
            }
            itemsWithAds.add(subjectList[i])
        }
        submitList(itemsWithAds)
    }
    
    // Click listener interface
    interface HandleClickListener {
        fun onClick(subjectName: SubjectName)
    }
}

// Placeholder class for ads
class AdViewPlaceholder
