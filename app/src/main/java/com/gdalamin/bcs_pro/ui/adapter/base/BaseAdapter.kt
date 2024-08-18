package com.gdalamin.bcs_pro.ui.adapter.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding>(
    private val areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    private val areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) : RecyclerView.Adapter<BaseAdapter<T, VB>.BaseViewHolder<VB>>() {

    inner class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return this@BaseAdapter.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return this@BaseAdapter.areContentsTheSame(oldItem, newItem)
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val item = differ.currentList[position]
        bind(holder.binding, item, position)
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): VB

    abstract fun bind(binding: VB, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolder(binding)
    }

    fun submitList(list: List<T>) {
        differ.submitList(list)
    }


    // New method to append items
    fun appendList(newItems: List<T>) {
        val updatedList = differ.currentList.toMutableList().apply {
            addAll(newItems)
        }
        differ.submitList(updatedList)
    }
}
