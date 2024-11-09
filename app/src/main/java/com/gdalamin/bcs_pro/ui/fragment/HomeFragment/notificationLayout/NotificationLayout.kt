package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.UserNotification
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.databinding.NotificationItemDitealsBinding
import com.gdalamin.bcs_pro.databinding.NotificationLayoutBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.NotificationAdapter
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertBase64ToBitmap
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout

class NotificationLayout(
    private val fragment: Fragment,
    private val notificationViewModel: NotificationViewModel,
    private val binding: FragmentHomeBinding
) : NotificationAdapter.handleClickListner {
    
    private val notificationAdapter by lazy { NotificationAdapter(this) }
    
    init {
        listener()
    }
    
    private fun listener() {
        
        binding.btnNotification.setOnClickListener {
            notificationViewModel.getNotification()
            show()
        }
        
    }
    
    private fun showNotificationDiteals(item: UserNotification) {
        val binding =
            NotificationItemDitealsBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val dialog = Dialog(fragment.requireContext())
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.white_bg_5dp)
        
        binding.apply {
            
            tvTitle.text = item.title
            ivNotificationImage.setImageBitmap(convertBase64ToBitmap(item.image))
            tvDescription.text = item.description
            btnClose.setOnClickListener {
                dialog.dismiss()
                
            }
            btnSubmit.setOnClickListener { dialog.dismiss() }
        }
        
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );
        }

//         Set margins programmatically
        
        
        dialog.show()
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
                        noNotificationLayout.visibility = View.GONE
                        showShimmerLayout(shimmerLayout, notificationRv)
                    }
                    
                    is DataState.Success -> {
                        response.data?.let { notificationList ->
                            shimmerLayout.visibility = View.GONE
                            shimmerLayout.stopShimmer()
                            
                            if (notificationList.isEmpty()) {
                                // If there are no notifications, show the "No Notification" layout
                                noNotificationLayout.visibility = View.VISIBLE
                                notificationRv.visibility = View.GONE
                                
                            } else {
                                // Otherwise, show the notifications in the RecyclerView
                                noNotificationLayout.visibility = View.GONE
                                notificationRv.visibility = View.VISIBLE
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
                        noNotificationLayout.visibility = View.GONE
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
    
    private fun setupRecyclerView(binding: NotificationLayoutBinding) = binding.apply {
        notificationRv.adapter = notificationAdapter
        notificationRv.layoutManager =
            LinearLayoutManager(fragment.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    
    override fun onClick(item: UserNotification) {
        showNotificationDiteals(item)
    }
    
}