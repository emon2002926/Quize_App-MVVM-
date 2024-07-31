package com.gdalamin.bcs_pro.ui.fragment.QuestionFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdalamin.bcs_pro.R

class LoadingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_barRv)
        private val retryButton = view.findViewById<Button>(R.id.retry_button)

        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            retryButton.setOnClickListener { retry() }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.loading_state_item, parent, false)
        return LoadStateViewHolder(view)
    }
}
