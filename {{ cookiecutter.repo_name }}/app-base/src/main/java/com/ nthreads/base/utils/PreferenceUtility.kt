package com.nthreads.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

class PreferenceUtility {
    companion object {

        /*
        * Function for getting a value from shared preferences
        */

        fun getPreference(
            context: Context,
            prefName: String, key: String
        ): String {

            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getString(key, "") ?: ""
        }

        fun getPreference(
            context: Context,
            prefName: String,
            key: String,
            defValue: String
        ): String {
            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getString(key, defValue) ?: defValue
        }

        fun getMutableSetPreference(
            context: Context,
            prefName: String, key: String
        ): MutableSet<String>? {

            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getStringSet(key, emptySet())
        }

        /*
         * Function for setting values for shared preferences
         */

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: String
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: Float
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: MutableSet<String>
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putStringSet(key, value)
            editor.apply()
        }

        fun setPreference(
            context: Context, prefName: String,
            key: String, value: Long
        ) {

            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun setPreference(
            base: Context, prefName: String,
            key: String, value: Boolean
        ) {

            val userPref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun setPreference(context: Context, prefName: String, list: java.util.ArrayList<String>) {
            val userPref = context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = userPref.edit()
            val gson = Gson()
            val json: String = gson.toJson(list)
            editor.putString(prefName, json)
            editor.apply()
        }

        fun getArrayListPreference(context: Context, prefName: String, key: String): ArrayList<String> {
            val usePref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val gson = Gson()
            val json: String = usePref.getString(key, "") ?: gson.toJson(arrayListOf<String>())
            val type: Type = object : TypeToken<ArrayList<String>>() {}.getType()
            return gson.fromJson(json, type)
        }

        fun getBoolPreference(
            base: Context,
            prefName: String, key: String
        ): Boolean {

            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getBoolean(key, false)
        }

        fun getBoolPreference(
            base: Context,
            prefName: String, key: String, defVal: Boolean
        ): Boolean {

            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getBoolean(key, defVal)
        }

        fun setPreference(
            base: Context, prefName: String,
            key: String, value: Int
        ) {

            val userPref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            val editor = userPref.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getIntPreference(
            base: Context, prefName: String,
            key: String
        ): Int {

            val usePref = base.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
            )
            return usePref.getInt(key, 0)
        }

        fun getLongPreference(base: Context, prefName: String, key: String): Long {
            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getLong(key, 0)
        }

        fun getLongPreference(base: Context, prefName: String, key: String, defValue: Long): Long {
            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getLong(key, defValue)
        }

        fun getFloatPreference(
            base: Context,
            prefName: String,
            key: String,
            defValue: Float
        ): Float {
            val usePref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return usePref.getFloat(key, defValue)
        }

        fun removePreferences(base: Context, prefName: String, key: String) {

            val userPref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = userPref.edit()
            editor.remove(key).apply()
        }

        fun removeAllPreferences(base: Context, prefName: String) {

            val userPref = base.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = userPref.edit()
            editor.clear()
            editor.apply()
        }
    }
}
