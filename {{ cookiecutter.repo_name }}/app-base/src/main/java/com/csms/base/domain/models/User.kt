package com.csms.base.domain.models


import android.os.Parcelable
import com.csms.base.utils.ValidationUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User(
    var id: String = "",
    var name: String? = "",
    var avatar: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var isActive: Boolean = false,
    var isAccountApproved: Boolean = false,
    var isPhoneVerified: Boolean = false,
    var isEmailVerified: Boolean = false,
) : Parcelable {
    fun isValidEmail(): Boolean = ValidationUtils.isValidEmail(email ?: "")
}


