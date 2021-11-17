package com.csms.base.utils

import android.text.TextUtils
import android.widget.EditText
import com.csms.chaperone.utils.extensions.textContent
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun isValidEmiratesId(id: String): Boolean {
        val regexString = "784-[0-9]{4}-[0-9]{7}-[0-9]"
        val pattern = Pattern.compile(regexString)

        return pattern.matcher(id).matches()
    }

    fun isValidEmail(etInput: EditText, errMsg: String): Boolean {
        val input = etInput.text.toString().trim()
        if (input.isEmpty() || !isValidEmail(input)) {
            setError(etInput, errMsg)
            return false
        }

        return true
    }

    fun isValidEmailOptional(etInput: EditText, errMsg: String): Boolean {
        val input = etInput.text.toString().trim()
        if (!input.isEmpty() && !isValidEmail(input)) {
            setError(etInput, errMsg)
            return false
        }

        return true
    }

    fun isValidInput(etInput: EditText, errMsg: String): Boolean {
        if (etInput.text.toString().trim().isEmpty()) {
            setError(etInput, errMsg)
            return false
        } else {
            etInput.error = null
        }

        return true
    }

    fun isValidInput(etInput: EditText, errMsg: String, minChar: Int): Boolean {
        val value = etInput.text.toString().trim()
        if (value.isEmpty() || value.length < minChar) {
            setError(etInput, errMsg)
            return false
        } else {
            etInput.error = null
        }

        return true
    }

    fun isValidPhoneNumber(
        etPhone: EditText,
        errMsg: String,
        errMsgLeadingZero: String,
        minChar: Int
    ): Boolean {
        if (isValidInput(etPhone, errMsg, minChar)) {
            if (etPhone.textContent.startsWith("0")) {
                setError(etPhone, errMsgLeadingZero)
                return false
            } else {
                etPhone.error = null
            }
        }

        return true
    }

    fun setError(etInput: EditText, errMsg: String) {
        etInput.error = errMsg
        etInput.requestFocus()
    }

    fun isValidInput(etInput: EditText, til: TextInputLayout, errMsg: String): Boolean {
        if (etInput.text.toString().trim().isEmpty()) {
            setError(etInput, errMsg)
            return false
        } else {
            til.isErrorEnabled = false
        }

        return true
    }

    fun isValidInput(etInput: TextInputEditText, til: TextInputLayout, errMsg: String): Boolean {
        if (etInput.text.toString().trim().isEmpty()) {
            etInput.error = errMsg
            etInput.requestFocus()
            return false
        } else {
            til.isErrorEnabled = false
        }

        return true
    }

    fun isValidInput(
        etInput: EditText,
        til: TextInputLayout,
        minChar: Int,
        errMsg: String
    ): Boolean {
        val value = etInput.text.toString().trim()
        if (value.isEmpty() || value.length < minChar) {
            setError(etInput, errMsg)
            return false
        } else {
            til.isErrorEnabled = false
        }

        return true
    }

    fun isValidInput(
        etInput: TextInputEditText,
        til: TextInputLayout,
        minChar: Int,
        errMsg: String
    ): Boolean {
        val value = etInput.text.toString().trim()
        if (value.isEmpty() || value.length < minChar) {
            etInput.error = errMsg
            etInput.requestFocus()
            return false
        } else {
            til.isErrorEnabled = false
        }

        return true
    }

    fun isValidInput(
        etInput: EditText,
        til: TextInputLayout,
        minChar: Int,
        maxChar: Int,
        errMsg: String
    ): Boolean {
        val value = etInput.text.toString().trim()
        if (value.isEmpty() || value.length < minChar || value.length > maxChar) {
            setError(etInput, errMsg)
            return false
        } else {
            til.isErrorEnabled = false
        }

        return true
    }

    fun sameString(etInputOne: EditText, etInputTwo: EditText, errMsg: String): Boolean {
        if (etInputOne.textContent.equals(etInputTwo.textContent)) {
            return true
        } else {
            setError(etInputTwo, errMsg)
            return false
        }
    }


}