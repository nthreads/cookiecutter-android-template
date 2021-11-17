package com.csms.base.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*

object PermissionUtil {
    val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    @JvmField
    val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val PHONE_CALL_PERMISSION = arrayOf(Manifest.permission.CALL_PHONE)
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)

    const val REQUEST_PERMISSION_LOCATION = 1000
    const val REQUEST_PERMISSION_EXTERNAL_STORAGE = 1001

    fun hasPermission(activity: Activity, permissions: Array<String>, REQUEST_CODE: Int): Boolean {
        if (permissions.isEmpty()) {
            Toast.makeText(activity, "No permissions added to check in " + activity.packageResourcePath, Toast.LENGTH_LONG).show()
            return false
        }
        val permissionsToRequest = ArrayList<String>() //Arrays.asList(permissions)
        for (permission in permissions) {
            val hasAccess = ContextCompat.checkSelfPermission(activity.applicationContext, permission)
            if (hasAccess != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), REQUEST_CODE)
            return false
        }
        return true
    }

    fun hasPermissionRationale(activity: Activity, permission: String): Boolean {
        val hasAccess = ContextCompat.checkSelfPermission(activity.applicationContext, permission)
        if (hasAccess != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            return false
        }
        return true
    }

    @JvmStatic
    fun hasPermission(fragment: Fragment, permissions: Array<String>, REQUEST_CODE: Int): Boolean {
        if (permissions.isEmpty()) {
            Toast.makeText(fragment.context, "No permissions added to check in " +
                    fragment.activity?.packageResourcePath, Toast.LENGTH_LONG).show()
            return false
        }

        fragment.context?.let {
            val permissionsToRequest = ArrayList<String>() //Arrays.asList(permissions)
            for (permission in permissions) {
                val hasAccess = ContextCompat.checkSelfPermission(it, permission)
                if (hasAccess != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission)
                }
            }
            if (permissionsToRequest.size > 0) {
                fragment.requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_CODE)
                return false
            }
        }

        return true
    }
}