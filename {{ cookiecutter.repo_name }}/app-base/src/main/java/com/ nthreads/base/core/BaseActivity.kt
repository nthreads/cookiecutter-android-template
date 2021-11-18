package com.nthreads.base.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.nthreads.base.R


import com.nthreads.base.ui.dialogs.WarningDialogFragment
import com.nthreads.base.utils.LocaleUtil

{% if cookiecutter.create_fb_remote_config == "y" -%}
import com.nthreads.base.domain.models.AppConfig
import com.nthreads.base.domain.models.RemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
{% endif %}

import com.google.gson.Gson
import io.reactivex.disposables.Disposable


/**
 * Package Name : com.nthreads.base.core 
 */

open class BaseActivity: AppCompatActivity() {

    fun showNetworkConnectivityDialog(source: AppCompatActivity) {
        val dialog = WarningDialogFragment.newInstance(
            getString(R.string.lbl_network_error),
            getString(R.string.toast_network_error),
            getString(R.string.lbl_settings),
            getString(R.string.lbl_cancel)
        )

        dialog.setListener(object : WarningDialogFragment.WarningDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
                finish()
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                source.finish()
            }

            override fun onDismiss(dialog: DialogFragment) {
            }

        })

        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "NetworkError")
    }

    fun showErrorMessage(
        title: String = getString(R.string.lbl_error),
        message: String,
        pBtnLabel: String? = getString(R.string.lbl_ok)
    ) {
        val dialog = WarningDialogFragment.newInstance(
            title,
            message,
            pBtnLabel = pBtnLabel
        )

        dialog.setListener(object : WarningDialogFragment.WarningDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                dialog.dismiss()
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
            }

            override fun onDismiss(dialog: DialogFragment) {}

        })

        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "error")
    }

    {% if cookiecutter.create_fb_remote_config == "y" -%}
    fun getFirebaseRemoteConfigs(mCallback: (Boolean, RemoteConfig) -> Unit) {
        //Firebase Remote Config
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defualts)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if(task.isSuccessful) {
                val updated = task.result
                Log.d("RemoteConfig", "Updated since last : $updated")

                //Setup keys
                val gson = Gson()

                val appConfigStr = firebaseRemoteConfig.getString(Consts.CONST_APP_CONFIG)
                val appConfig = gson.fromJson(appConfigStr, AppConfig::class.java)

                val remoteConfig = RemoteConfig(
                    appConfig = appConfig
                )

                BaseAppController.remoteConfig = remoteConfig
                mCallback.invoke(task.isSuccessful, remoteConfig)

                Log.d("RemoteConfig", "Response : $appConfigStr")
            } else {
                mCallback.invoke(task.isSuccessful, RemoteConfig())
            }

            Log.d("RemoteConfig", "Task : ${task.isSuccessful}")
        }
    }
    {% endif %}

    

    fun showUpdateAppDialog(
        applicationId: String,
        activity: Activity,
        pBtnLabel: String? = getString(R.string.lbl_update),
        nBtnLabel: String? = getString(R.string.lbl_cancel)
    ) {
        val dialog = WarningDialogFragment.newInstance(
            getString(R.string.title_update),
            getString(R.string.msg_update_app),
            pBtnLabel = pBtnLabel,
            nBtnLabel = nBtnLabel
        )
        dialog.setListener(object : WarningDialogFragment.WarningDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$applicationId")
                    )
                )

                activity.finish()
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                activity.finish()
            }

            override fun onDismiss(dialog: DialogFragment) {}

        })
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "UpdateApp")
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
        dialog.show(supportFragmentManager, "Error")
    }

    companion object {
        var CURRENT_LANG = "en"
    }
}