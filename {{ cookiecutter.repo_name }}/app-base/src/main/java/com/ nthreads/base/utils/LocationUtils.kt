package com.nthreads.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.nthreads.base.R
import com.nthreads.base.ui.dialogs.WarningDialogFragment
import com.nthreads.base.utils.MathUtils.pxToDp
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

@SuppressLint("LogNotTimber")
object LocationUtils {
    private const val LN2 = 0.6931471805599453 // Natural log of 2
    private const val WORLD_PX_HEIGHT = 256
    private const val WORLD_PX_WIDTH = 256
    private const val ZOOM_MAX = 21

    private var DEFAULT_MAX_WAIT_TIME = 5 * 60 * 1000L //5 minutes

    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle

    const val LOCATION_ACCESS_REQUEST = 999


    fun getBoundsZoomLevel(
        bounds: LatLngBounds,
        mapWidthPx: Float,
        mapHeightPx: Float
    ): Double {
        val ne: LatLng = bounds.northeast
        val sw: LatLng = bounds.southwest
        val latFraction =
            (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI
        val lngDiff: Double = ne.longitude - sw.longitude
        val lngFraction = (if (lngDiff < 0) lngDiff + 360 else lngDiff) / 360
        val latZoom = zoom(
            mapHeightPx,
            pxToDp(WORLD_PX_HEIGHT.toFloat()),
            latFraction
        )
        val lngZoom = zoom(
            mapWidthPx,
            pxToDp(WORLD_PX_WIDTH.toFloat()),
            lngFraction
        )
        val padding = 0.5
        val result = Math.min(latZoom, lngZoom) - padding
        return Math.min(result, ZOOM_MAX.toDouble())
    }

    private fun latRad(lat: Double): Double {
        val sin = Math.sin(lat * Math.PI / 180)
        val radX2 = Math.log((1 + sin) / (1 - sin)) / 2
        return Math.max(
            Math.min(radX2, Math.PI),
            -Math.PI
        ) / 2
    }

    private fun zoom(
        mapPx: Float,
        worldPx: Float,
        fraction: Double
    ): Double {
        return Math.floor(Math.log(mapPx / worldPx / fraction) / LN2)
    }

    fun openDirectionsInMap(context: Context, latitude: Double, longitude: Double) {
        if (isGoogleMapsInstalled(context)) {
            //dirflg=r - Switches on "Public Transit" (Railway direction)
            //dirflg=w - Switches to walking directions
            //dirflg=d - Switches to driving directions
            val url = "http://maps.google.com/maps?f=d&daddr=$latitude,$longitude&dirflg=d"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            context.startActivity(intent)
        }
    }

    private fun isGoogleMapsInstalled(context: Context): Boolean {
        try {
            val info: ApplicationInfo? =
                context.packageManager?.getApplicationInfo("com.google.android.apps.maps", 0)
            info?.let {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun Context.checkSinglePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(28)
    fun Context.checkLocationPermissionAPI28(activity: Activity, locationRequestCode: Int) {
        if (!checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val permList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            requestPermissions(activity, permList, locationRequestCode)
        }
    }

    @TargetApi(29)
    private fun Context.checkLocationPermissionAPI29(activity: Activity, locationRequestCode: Int) {
        if (checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) return

        val permList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        requestPermissions(activity, permList, locationRequestCode)
    }

    @TargetApi(30)
    private fun Activity.checkBackgroundLocationPermissionAPI30(
        callback: (allGranted: Boolean, isRationale: Boolean) -> Unit
    ) {
        if (checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            callback.invoke(true, false)
        } else {
            callback.invoke(false, true) // Show popup
        }

        /*Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    callback.invoke(true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    callback.invoke(false)
                }

            }).check()*/
    }

    fun checkIfLocationPermissionGranted(
        activity: Activity,
        parentView: View,
        backgroundLocationNeeded: Boolean = false,
        callback: (allGranted: Boolean, isRationale: Boolean) -> Unit
    ) {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // check if all permissions are granted
                    report?.apply {
                        when {
                            areAllPermissionsGranted() -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && backgroundLocationNeeded) {
                                    activity.checkBackgroundLocationPermissionAPI30(callback)
                                } else {
                                    callback.invoke(true, false)
                                }
                            }

                            isAnyPermissionPermanentlyDenied -> // check for permanent denial of any permission
                                callback.invoke(false, true)
                            else -> {
                                callback.invoke(false, false)
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                SnackbarUtils.show(parentView, R.string.permission_user_location)
            }.check()
    }


    fun getLocationRequest(isLiveLocationNeed: Boolean = false): LocationRequest {
        return LocationRequest.create().apply {

            if (isLiveLocationNeed) {
                interval = UPDATE_INTERVAL_IN_MILLISECONDS
                fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS

                priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                maxWaitTime = DEFAULT_MAX_WAIT_TIME / 2
            } else {
                interval = UPDATE_INTERVAL_IN_MILLISECONDS * 6 * 5 // 5 minutes
                fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS * 6 * 5// // 2.5 minutes
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

                maxWaitTime = DEFAULT_MAX_WAIT_TIME
            }
        }
    }


    fun enableGPS(
        activity: Activity?,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        activity?.let { ctx ->
            val locationSettingsRequest =
                LocationSettingsRequest.Builder().addLocationRequest(getLocationRequest())
            val result = LocationServices.getSettingsClient(ctx)
                .checkLocationSettings(locationSettingsRequest.build())

            result.addOnSuccessListener {
                // The client can initialize location requests here.
                onSuccess?.invoke()
                Log.d("TAG", "All location settings are satisfied")
            }

            result.addOnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode

                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "TAG",
                            "Location settings are not satisfied. Attempting to upgrade  location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(activity, LOCATION_ACCESS_REQUEST)

                            onError?.invoke()
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i("TAG", "PendingIntent unable to execute request.")
                            sie.printStackTrace()
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e("TAG", errorMessage)
                    }
                }
            }
        }
    }
}