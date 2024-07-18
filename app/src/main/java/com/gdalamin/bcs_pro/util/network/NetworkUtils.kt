package com.gdalamin.bcs_pro.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

class NetworkUtils(private val context: Context) {

    private var isConnected = false

    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newIsConnected: Boolean = isConnected()
            if (!isConnected && newIsConnected) {
                // Internet connection is restored
            } else if (isConnected && !newIsConnected) {
                // Internet connection is gone
                Toast.makeText(context, "Internet connection is gone", Toast.LENGTH_SHORT).show()
            }
            isConnected = newIsConnected
        }
    }

    init {
        val intentFilter = IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
        context.registerReceiver(networkReceiver, intentFilter)
        isConnected = isConnected()
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(networkReceiver)
    }

    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


}
