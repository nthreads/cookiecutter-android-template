package com.csms.base.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.StrictMode
import android.text.TextUtils
import com.csms.base.R
import com.csms.base.domain.models.*
import com.csms.base.utils.LocaleUtil
import com.csms.base.utils.PreferenceUtility
import com.csms.chaperone.utils.extensions.isNotNullNorEmpty
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.io.IOException
import java.net.HttpURLConnection
import java.util.*


/**
 * Package Name : com.csms.chaperone.core
 * Created by Asad Ur Rehman on 6/16/21 - 12:51 PM.
 * Copyright (c) 2021 Creativity Smart Media Solutions. All rights reserved.
 */

open class BaseAppController : Application() {

    var currentUser: User? = null



    override fun onCreate() {
        super.onCreate()
        instance = this
        currentUser = getSavedUser()
        XInjectionManager.init(this)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        if (!TextUtils.isEmpty(getCurrentLanguageCode()))
            LocaleUtil.setLocale(this, getCurrentLanguageCode())
    }

    @SuppressLint("DefaultLocale")
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            model
        } else {
            "$manufacturer $model"
        }
    }

    companion object {
        lateinit var instance: BaseAppController
        {% if cookiecutter.create_fb_remote_config == "y" -%}
        var remoteConfig: RemoteConfig = RemoteConfig()
        {% endif %}
    }

    fun getCurrentLanguageCode(): String {
        return PreferenceUtility.getPreference(
            this,
            Consts.APP_PREFS, Consts.PREF_LANGUAGE_SELECTED, "en"
        )
    }

    fun setCurrentLanguage(currentLanguage: String) {
        PreferenceUtility.setPreference(
            this, Consts.APP_PREFS,
            Consts.PREF_LANGUAGE_SELECTED, currentLanguage
        )
    }

    fun setPref(key: String, value: String) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value)
    }

    fun setPref(key: String, value: ArrayList<String>) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value.toMutableSet())
    }

    fun setPref(key: String, value: Int) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value)
    }

    fun setPref(key: String, value: Long) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value)
    }

    fun setPref(key: String, value: Float) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value)
    }

    fun setPref(key: String, value: Boolean) {
        PreferenceUtility.setPreference(this, Consts.APP_PREFS, key, value)
    }

    fun getPrefs(key: String): String {
        return PreferenceUtility.getPreference(this, Consts.APP_PREFS, key)
    }

    fun getPrefs(key: String, defValue: String): String {
        return PreferenceUtility.getPreference(this, Consts.APP_PREFS, key, defValue)
    }

    fun getIntPrefs(key: String): Int {
        return PreferenceUtility.getIntPreference(this, Consts.APP_PREFS, key)
    }

    fun getLongPrefs(key: String): Long {
        return PreferenceUtility.getLongPreference(this, Consts.APP_PREFS, key)
    }

    fun getLongPrefs(key: String, defValue: Long): Long {
        return PreferenceUtility.getLongPreference(this, Consts.APP_PREFS, key, defValue)
    }

    fun getFloatPrefs(key: String, defValue: Float): Float {
        return PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, key, defValue)
    }

    fun getBoolPrefs(key: String, defValue: Boolean): Boolean {
        return PreferenceUtility.getBoolPreference(this, Consts.APP_PREFS, key, defValue)
    }

    fun getMutableSetPrefs(key: String): MutableSet<String>? {
        return PreferenceUtility.getMutableSetPreference(this, Consts.APP_PREFS, key)
    }

    private fun removePrefs(key: String) {
        PreferenceUtility.removePreferences(this, Consts.APP_PREFS, key)
    }

    private fun removeAllPrefs() {
        PreferenceUtility.removeAllPreferences(this, Consts.APP_PREFS)
    }

    fun getSavedUser(): User? {
        //TODO Implement User Strategy
        return User()
    }

    

    fun clearUserSession() {

        currentUser = null
        removeAllPrefs()
    }

    fun isSessionActive(): Boolean {

        val id = getPrefs(Consts.PREFS_USER_ID)
        val accessToken = getPrefs(Consts.PREFS_ACCESS_TOKEN)
        val isUserActive = getBoolPrefs(Consts.PREFS_USER_IS_ACTIVE, false)

        return id.isNotBlank() || accessToken.isNotBlank() || !isUserActive

        // TODO Check expiry when implemented
        val expiry = getLongPrefs(Consts.PREFS_EXPIRY)
        val isExpired = Date(expiry).after(Date(System.currentTimeMillis()))
        return isExpired
    }

    fun getSessionAccessToken(): String? {
        val token = getPrefs(Consts.PREFS_ACCESS_TOKEN)
        return if (token.isEmpty()) null
        else token
    }


    fun isForeground(context: Context = applicationContext): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
        val packageName = context.packageName
        tasks.forEach {
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == it.importance && packageName == it.processName) {
                return true
            }
        }

        return false
    }
}
