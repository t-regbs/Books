package com.example.books.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isOnline(): Boolean {
    val connectivityManager = this
            .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.apply {
        val netInfo = activeNetworkInfo
        netInfo?.let {
            if (it.isConnected) return true
        }
    }
    return false
}