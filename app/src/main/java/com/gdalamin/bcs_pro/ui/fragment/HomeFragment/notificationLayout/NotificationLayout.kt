package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.databinding.NotificationLayoutBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.NotificationAdapter
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout

class NotificationLayout(
    private val fragment: Fragment,
    private val notificationViewModel: NotificationViewModel,
    private val binding: FragmentHomeBinding
) {
    
    private val notificationAdapter by lazy { NotificationAdapter() }
    
    init {
        listener()
    }
    
    private fun listener() {
        
        binding.btnNotification.setOnClickListener {
            notificationViewModel.getNotification()
            show()
        }
        
    }
    
    fun show() {
        
        val binding =
            NotificationLayoutBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        
        val dialog = Dialog(fragment.requireContext())
        dialog.setContentView(binding.root);
        
        setupRecyclerView(binding)
        
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        
        observeNotification(binding)
        
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
        dialog.show()
    }
    
    
    private fun observeNotification(binding: NotificationLayoutBinding) {
        
        binding.apply {
            notificationViewModel.notification.observe(fragment.viewLifecycleOwner) { response ->
                when (response) {
                    is DataState.Loading -> {
                        // Show loading indicator if necessary
                        binding.noNotificationLayout.visibility = View.GONE
                        showShimmerLayout(shimmerLayout, notificationRv)
                    }
                    
                    is DataState.Success -> {
                        response.data?.let { notificationList ->
                            binding.shimmerLayout.visibility = View.GONE
                            binding.shimmerLayout.stopShimmer()
                            
                            if (notificationList.isEmpty()) {
                                // If there are no notifications, show the "No Notification" layout
                                binding.noNotificationLayout.visibility = View.VISIBLE
                                binding.notificationRv.visibility = View.GONE
                            } else {
                                // Otherwise, show the notifications in the RecyclerView
                                binding.noNotificationLayout.visibility = View.GONE
                                binding.notificationRv.visibility = View.VISIBLE
                                notificationAdapter.submitList(notificationList)
                                
                                // Log the notification details
                                for (notification in notificationList) {
                                    Log.d(
                                        "NotificationDetails",
                                        "Title: ${notification.title}, Message: ${notification.description}"
                                    )
                                }
                            }
                        }
                    }
                    
                    is DataState.Error -> {
                        // Log or show the error message
//                        Log.e("NotificationError", response.message ?: "Unknown Error")
//                        binding.shimmerLayout.visibility = View.GONE
//                        binding.shimmerLayout.stopShimmer()
                        binding.noNotificationLayout.visibility = View.GONE
                        showShimmerLayout(shimmerLayout, notificationRv)
                        Toast.makeText(
                            fragment.requireContext(),
                            CHECK_INTERNET_CONNECTION_MESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            
        }
        
    }
    
    private fun setupRecyclerView(binding: NotificationLayoutBinding) {
        binding.notificationRv.adapter = notificationAdapter
        binding.notificationRv.layoutManager =
            LinearLayoutManager(fragment.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    
}