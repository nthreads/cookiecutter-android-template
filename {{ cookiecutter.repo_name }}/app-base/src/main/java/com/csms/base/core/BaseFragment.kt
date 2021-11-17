package com.csms.base.core

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.csms.base.BuildConfig
import com.csms.base.R
import com.csms.base.ui.dialogs.WarningDialogFragment
{% if cookiecutter.create_location_utils == "y" -%}
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polygon
{% endif %}
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*
import kotlin.math.max

open class BaseFragment : Fragment() {

    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        layout: Int
    ): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater?.inflate(layout, container, false)
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.getParent() as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    @JvmOverloads
    fun showToast(message: String?, duration: Int = Toast.LENGTH_LONG) {
        var msg = message
        if (msg == null || TextUtils.isEmpty(msg)) msg = getString(R.string.err_went_wrong)
        if (BuildConfig.DEBUG) Log.d("TOAST", msg)
        try {
            Toast.makeText(context, msg, duration).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun datePickerDialog(tag: String?, mCallback: DatePickerDialog.OnDateSetListener?) {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            mCallback,
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        )
        dpd.setOnCancelListener { Log.d("TimePicker", "Dialog was cancelled") }
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(childFragmentManager, tag)
    }

    {% if cookiecutter.create_location_utils == "y" -%}
    fun setStyle(mMap: GoogleMap, id: Int) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(context, id)
            )
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }
    }

    {% endif %}


    companion object {
        const val TAG_SDATE_PICKER = "StartDatePickerDialog"
        const val TAG_EDATE_PICKER = "EndDatePickerDialog"

        fun getDeviceWidth(context: Context): Float {
            val displayMetrics = context.resources.displayMetrics
            return displayMetrics.widthPixels / displayMetrics.density
        }

        @JvmOverloads
        fun calculateNoOfColumns(
            context: Context,
            itemWidth: Float = 160f,
            containerWidth: Int = -1
        ): Int {
            val displayMetrics = context.resources.displayMetrics
            var fullWidth = containerWidth
            if (fullWidth == -1) {
                fullWidth = displayMetrics.widthPixels
            }
            val dpWidth = fullWidth / displayMetrics.density
            return max(1, (dpWidth / itemWidth).toInt())
        }


        {% if cookiecutter.create_location_utils == "y" -%}
        fun getGeoPointsString(polygon: Polygon): String {
            val stringBuilder = StringBuilder("'points' : [")
            for (i in polygon.points.indices) {
                val lat = polygon.points[i].latitude
                val lon = polygon.points[i].longitude
                stringBuilder.append("{'lat' : ").append(lat).append(", 'lon' : ").append(lon)
                    .append("},")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1) // Remove last comma
            stringBuilder.append("]")
            return stringBuilder.toString()
        }

        {% endif %}
    }

    protected fun getMaxEntriesForEvenRows(
        noOfColumns: Int,
        noOfRows: Int = 4,
        totalEntries: Int
    ): Int {
        val orientation = resources.configuration.orientation
        // Multiply noOfColumns with rows depending upon orientation
        // 3 rows in portrait mode and 6 rows in landscape
        val maxEntries = if (orientation == 2) noOfColumns * noOfRows else noOfColumns * noOfRows
        // If totalEntries are less than calculated maxEntries then reduce one complete row
        return if (totalEntries < maxEntries) totalEntries else maxEntries
    }



    fun showErrorDialog(
        title: String = getString(R.string.lbl_error),
        message: String = "",
        nBtnLabel: String? = null
    ) {
        val dialog = WarningDialogFragment.newInstance(
            title,
            message,
            nBtnLabel = nBtnLabel
        )

        dialog.setListener(object : WarningDialogFragment.WarningDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {}

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                dialog.dismiss()
            }

            override fun onDismiss(dialog: DialogFragment) {}

        })
        dialog.show(childFragmentManager, "Error")
    }

    fun showLocationDialog(
        isRationale: Boolean = false,
        mCallback: ((DialogFragment, Boolean) -> Unit)? = null
    ) {

        val message =
            if (isRationale) getString(R.string.permission_location_rationale) else getString(R.string.permission_user_location)
        val dialog = WarningDialogFragment.newInstance(
            getString(R.string.lbl_location),
            message,
            getString(R.string.lbl_settings),
            getString(R.string.lbl_later),

            )
        dialog.isCancelable = false
        dialog.setListener(object : WarningDialogFragment.WarningDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                mCallback?.invoke(dialog, true)
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                mCallback?.invoke(dialog, false)
            }

            override fun onDismiss(dialog: DialogFragment) {

            }

        })

        dialog.show(childFragmentManager, "Tag")
    }
}