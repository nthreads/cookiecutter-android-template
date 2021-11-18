package com.nthreads.base.utils.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.nthreads.base.R
import com.nthreads.chaperone.utils.extensions.ifNull
import com.google.android.material.snackbar.Snackbar

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)

fun AppCompatActivity.addFragment(
    tag: String, layoutId: Int, allowStateLoss: Boolean = false,
    fragmentInstance: Fragment
) {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: fragmentInstance
    val transaction = supportFragmentManager.beginTransaction().replace(layoutId, fragment, tag)
    transaction.addToBackStack(null)

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun AppCompatActivity.replaceFragment(
    tag: String, layoutId: Int, allowStateLoss: Boolean = false,
    fragmentInstance: Fragment
) {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: fragmentInstance
    val transaction = supportFragmentManager.beginTransaction()
        .replace(layoutId, fragment, tag)
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun Fragment.replaceFragment(
    tag: String, layoutId: Int, allowStateLoss: Boolean = false,
    fragmentInstance: Fragment
) {
    val fragment = childFragmentManager.findFragmentByTag(tag) ?: fragmentInstance
    val transaction = childFragmentManager.beginTransaction()
        .replace(layoutId, fragment, tag)
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun Fragment.addFragment(
    tag: String, layoutId: Int, allowStateLoss: Boolean = false,
    fragmentInstance: Fragment
) {
    val fragment = childFragmentManager.findFragmentByTag(tag) ?: fragmentInstance
    val transaction = childFragmentManager.beginTransaction().replace(layoutId, fragment, tag)
    transaction.addToBackStack(null)

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun AppCompatActivity.removeFragment(
    tag: String
) {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    fragment?.let {
        val transaction = supportFragmentManager.beginTransaction().remove(it)
        transaction.commit()
    }

}

fun AppCompatActivity.toPreviousView() {
    supportFragmentManager.popBackStack()
}

fun Activity.hideKeyboard() {
    currentFocus?.run {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}

fun View.hideKeyboard() {
    this.run {
        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}

fun Activity.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

fun Activity.showToast(@StringRes resource: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(getString(resource), duration)

@SuppressLint("LogNotTimber")
fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (message.isEmpty()) return
    Log.d("TOAST", message)

    val toast = Toast.makeText(this, message, duration)
    toast.show()
}

@SuppressLint("LogNotTimber")
fun Activity.showCenterToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    if (message.isEmpty()) return
    Log.d("TOAST", message)

    val toast = Toast.makeText(this, message, duration)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (message.isEmpty()) return

    Log.d("TOAST", message)
    return Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(@StringRes resource: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(getString(resource), duration)

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    activity?.showToast(message, duration)

@SuppressLint("RestrictedApi")
fun Context.inflate(@MenuRes menuRes: Int): Menu {
    val menu = MenuBuilder(this)
    val menuInflater = SupportMenuInflater(this)
    menuInflater.inflate(menuRes, menu)
    return menu
}

/**
 * Developed by Magora-Systems.com
 * @since 2017
 * @author Anton Vlasov - whalemare
 */
fun Menu.toList(): List<MenuItem> {
    val menuItems = ArrayList<MenuItem>(this.size())
    (0 until this.size()).mapTo(menuItems) { this.getItem(it) }
    return menuItems
}


fun <T> Activity.showErrorToast(it: com.nthreads.base.domain.models.Resource<T>) {
    it.message?.apply {
        showToast(this)
        return
    }

    it.messageRes?.apply {
        showToast(getString(this))
        return
    }

    showToast(getString(R.string.err_went_wrong))
}

fun <T> Fragment.showErrorToast(it: com.nthreads.base.domain.models.Resource<T>) {
    it.message?.apply {
        showToast(this)
        return
    }

    it.messageRes?.apply {
        showToast(getString(this))
        return
    }

    showToast(getString(R.string.err_went_wrong))
}

fun <T> Context.showErrorToast(it: com.nthreads.base.domain.models.Resource<T>) {
    it.message?.apply {
        showToast(this)
        return
    }

    it.messageRes?.apply {
        showToast(getString(this))
        return
    }

    showToast(getString(R.string.err_went_wrong))
}

/**
 * Helper functions to simplify permission checks/requests.
 */
fun Context.hasPermission(permission: String): Boolean {

    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q
    ) {
        return true
    }

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Requests permission and if the user denied a previous request, but didn't check
 * "Don't ask again", we provide additional rationale.
 *
 * Note: The Snackbar should have an action to request the permission.
 */
fun Fragment.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackbar: Snackbar
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)

    if (provideRationale) {
        snackbar.show()
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

/**
 * Requests permission and if the user denied a previous request, but didn't check
 * "Don't ask again", we provide additional rationale.
 *
 * Note: The Snackbar should have an action to request the permission.
 */
fun Activity.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackbar: Snackbar
) {
    val provideRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        shouldShowRequestPermissionRationale(permission)
    } else {
        TODO("VERSION.SDK_INT < M")
    }

    if (provideRationale) {
        snackbar.show()
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

fun Activity.showExceptionMessage(ex: Exception) {
    ex.message?.let {
        showToast(it)
    }.ifNull {
        showToast(getString(R.string.err_went_wrong))
    }
}
