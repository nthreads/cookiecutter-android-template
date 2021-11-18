package com.nthreads.chaperone.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nthreads.base.core.Consts.KEY_EXTRAS
import kotlin.reflect.KClass

fun <T : Activity> KClass<T>.start(sourceActivity: AppCompatActivity, finish: Boolean = false) {
    Intent(sourceActivity, this.java).apply {
        sourceActivity.startActivity(this)
    }
    if (finish) {
        sourceActivity.finish()
    }
}

fun <T : Activity> KClass<T>.start(context: Context?) {
    context?.let {
        Intent(it, this.java).apply {
            it.startActivity(this)
        }
    }

}

fun <T : Activity> KClass<T>.start(context: Context?, extras: Bundle) {
    context?.let { ctx ->
        val intent = Intent(ctx, this.java)
        intent.putExtras(extras)
        ctx.startActivity(intent)
    }
}

fun <T : Activity> KClass<T>.startForResult(
    sourceActivity: AppCompatActivity,
    requestCode: Int,
    extras: Bundle? = null
) {
    Intent(sourceActivity, this.java).apply {
        extras?.let { this.putExtras(it) }
        sourceActivity.startActivityForResult(this, requestCode)
    }
}

fun <T : Activity> KClass<T>.startAsNewTask(sourceActivity: Activity, extras: Bundle? = null) {
    Intent(sourceActivity, this.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        extras?.let {
            putExtras(it)
        }

        sourceActivity.overridePendingTransition(0, 0)
        sourceActivity.finish()
        sourceActivity.startActivity(this)
        sourceActivity.overridePendingTransition(0, 0)
    }
}

fun <T : Activity> KClass<T>.startForLocale(sourceActivity: Activity) {

    Intent(sourceActivity, this.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        sourceActivity.overridePendingTransition(0, 0)
        sourceActivity.startActivity(this)
        sourceActivity.finish()
        sourceActivity.overridePendingTransition(0, 0)
    }
}

fun <T : Activity> KClass<T>.startAsNewTask(context: Context?, parent: FragmentActivity?) {
    Intent(context, this.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        parent?.overridePendingTransition(0, 0)
        context?.startActivity(this)
        parent?.overridePendingTransition(0, 0)
        parent?.finish()
    }
}

// navigating user to app settings
fun openSettings(packageName: String, activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    activity.startActivityForResult(intent, 101)
}

// navigating user to app settings
fun openSettings(packageName: String, fragment: Fragment) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    fragment.startActivityForResult(intent, 101)
}
