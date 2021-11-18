package com.nthreads.base.utils

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.nthreads.base.R

object SnackbarUtils {

    private var mSnackbar: Snackbar? = null

    fun show(view: View, @StringRes msg: Int) {
        mSnackbar = Snackbar.make(view, msg, 3000)
        mSnackbar?.apply {
            show()
        }
    }

    fun show(view: View, @StringRes msg: Int, @StringRes actionLabel: Int, mCallback: View.OnClickListener) {
        mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
        mSnackbar?.apply {
            show()
            setAction(actionLabel, mCallback)
        }
    }


    fun show(view: View, msg: String, mCallback: View.OnClickListener) {

        mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
        mSnackbar?.apply {
            show()
            setAction(R.string.lbl_retry, mCallback)
        }
    }

    fun show(view: View, msg: String, actionLabel: String, mCallback: View.OnClickListener) {
        mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
        mSnackbar?.apply {
            show()
            setAction(actionLabel, mCallback)
        }
    }
}