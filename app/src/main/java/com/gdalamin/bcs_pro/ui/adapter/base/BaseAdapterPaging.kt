package com.gdalamin.bcs_pro.ui.adapter.base

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapterPaging<T : Any, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, BaseAdapterPaging<T, VB>.BaseViewHolderP<VB>>(diffCallback) {

    inner class BaseViewHolderP<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = snapshot().size

    override fun onBindViewHolder(holder: BaseViewHolderP<VB>, position: Int) {
        val item = getItem(position)
        if (item != null) {
            bind(holder.binding, item, position)
        }
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): VB

    abstract fun bind(binding: VB, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderP<VB> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolderP(binding)
    }
}