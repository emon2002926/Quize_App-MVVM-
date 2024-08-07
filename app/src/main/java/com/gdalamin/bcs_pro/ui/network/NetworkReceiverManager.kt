package com.gdalamin.bcs_pro.ui.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.annotation.Keep

@Keep
class NetworkReceiverManager(
    private val context: Context,
    private val connectivityChangeListener: ConnectivityChangeListener
) {
    private lateinit var networkReceiver: BroadcastReceiver
    private var isConnected: Boolean = false

    interface ConnectivityChangeListener {
        fun onConnected()
        fun onDisconnected()
    }

    init {
        register()
    }

    fun register() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val newIsConnected: Boolean = isConnected(context)
                if (!isConnected && newIsConnected) {
                    connectivityChangeListener.onConnected()
                } else if (isConnected && !newIsConnected) {
                    connectivityChangeListener.onDisconnected()
                }
                isConnected = newIsConnected
            }
        }
        context.registerReceiver(networkReceiver, intentFilter)
        isConnected = isConnected(context)
    }

    fun unregister() {
        context.unregisterReceiver(networkReceiver)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
