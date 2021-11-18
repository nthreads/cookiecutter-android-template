package com.nthreads.base.core


object Consts {

    const val APP_PREFS = "{{ cookiecutter.repo_name }}.prefs"
    const val PREF_LANGUAGE_SELECTED = "LanguageSelected"
    const val PREF_IS_LANGUAGE_SELECTED = "IsLanguageSelected"

    const val IMAGE_DIRECTORY_NAME = "{{ cookiecutter.repo_name}}"

    const val CONST_APP_CONFIG = "android_config" //Replace this key in remote_config_defaults.xml too.

    const val PREFS_USER_EMAIL = "UserEmail"
    const val PREFS_USER_PASS: String = "UserPass"
    const val PREFS_USER_NAME = "UserName"
    const val PREFS_USER_ID = "UserId"
    const val PREFS_USER_PHONE = "UserPhone"
    const val PREFS_USER_IS_ACTIVE = "UserIsActive"

    const val PREFS_USER_BIOMETRIC_AUTHENTICATION_NEEDED = "BiometricAuthentication"

    const val PREFS_REGISTRATION_TOKEN = "DeviceRegistrationToken"
    const val PREFS_UNIQUE_DEVICE_ID = "UniqueDeviceId"
    const val PREFS_IS_DEVICE_REGISTERED = "IsDeviceRegistered"
    const val PREFS_IS_DEVICE_REGISTERED_ON_RC = "IsDeviceRegisteredOnRocketChat"

    const val PREFS_REFRESH_TOKEN = "RefreshToken"
    const val PREFS_ACCESS_TOKEN = "AccessToken"
    const val PREFS_EXPIRY = "TokenExpiry"

    const val KEY_EXTRAS: String = "Extras"
    const val EXTRAS_EMAIL: String = "Email"

    const val REQ_CODE_PERM_LOCATION = 212

    const val REQ_CODE_CAMERA = 301
    const val REQ_CODE_GALLERY = 302
    const val REQ_CODE_VIDEO = 303
    const val REQ_CODE_AUDIO = 304
    const val REQ_CODE_GET_FILE = 305

}
