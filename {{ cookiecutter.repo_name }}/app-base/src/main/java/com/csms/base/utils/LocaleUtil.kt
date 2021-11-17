package com.csms.base.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.util.*

class LocaleUtil {
    companion object {
        fun setLocale(context: Context, language: String) {

            val config = context.resources.configuration

            val locale = Locale(language)
            Locale.setDefault(locale)

            if (isAtLeast24Api()) {
                setSystemLocale(config, locale)
            } else {
                setSystemLocaleLegacy(config, locale)
            }

            /*if (BuildUtil.isAtLeast17Api()) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }*/

            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun getSystemLocaleLegacy(config: Configuration): Locale {
            return config.locales[0]
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale {
            return config.locales.get(0)
        }

        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }

        fun isAtLeast24Api(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        fun isRtl() : Boolean {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
        }
    }
}