package com.csms.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtil {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val nc: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(network)
                nc?.let {
                    return it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )
                } ?: return false
            }
        } else {
            val networkInfos = connectivityManager.allNetworkInfo
            for (tempNetworkInfo in networkInfos) {
                if (tempNetworkInfo.isConnected) {
                    return true
                }
            }
        }
        return false
    }
}