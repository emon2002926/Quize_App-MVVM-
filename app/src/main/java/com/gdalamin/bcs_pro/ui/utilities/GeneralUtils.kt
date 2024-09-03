package com.gdalamin.bcs_pro.ui.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Base64
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import java.nio.charset.StandardCharsets
import java.text.NumberFormat
import java.util.Locale


object GeneralUtils {
    
    
    fun convertEnglishToBangla(numberStr: String): String {
        try {
            val number = numberStr.toDouble()
            val bengaliLocale = Locale("bn", "BD")
            val bengaliNumberFormat = NumberFormat.getNumberInstance(bengaliLocale)
            return bengaliNumberFormat.format(number)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return numberStr
        }
        
    }
    
    fun convertBase64ToBitmap(base64Image: String?): Bitmap {
        val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
    
    
    fun hideShimmerLayout(shimmerLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
    
    fun showShimmerLayout(shimmerLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
    
    
    fun EditText.isEmpty(errorMessage: String): Boolean {
        return if (this.text.toString().isEmpty()) {
            this.error = errorMessage
            true
        } else {
            false
        }
    }
    
    
    fun convertToUTF8(inputString: String): String {
        return String(inputString.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    }
    
    
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}