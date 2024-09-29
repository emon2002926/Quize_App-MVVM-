package com.gdalamin.bcs_pro.ui.adapter.specificadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gdalamin.bcs_pro.data.model.UserNotification
import com.gdalamin.bcs_pro.databinding.NotificationItemBinding
import com.gdalamin.bcs_pro.ui.adapter.base.BaseAdapter
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertBase64ToBitmap

class NotificationAdapter() :
    BaseAdapter<UserNotification, NotificationItemBinding>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
    ) {
    override fun createBinding(parent: ViewGroup, viewType: Int): NotificationItemBinding {
        return NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
    
    override fun bind(binding: NotificationItemBinding, item: UserNotification, position: Int) {
        
        with(binding) {
            tvTitle.text = item.title
            tvDescription.text = item.description
            if (item.image.isNotEmpty()) {
                ivNotificationImage.setImageBitmap(convertBase64ToBitmap(item.image))
            }
        }
    }
    
    
}