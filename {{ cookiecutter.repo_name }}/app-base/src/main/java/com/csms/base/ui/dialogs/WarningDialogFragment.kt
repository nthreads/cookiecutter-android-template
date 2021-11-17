package com.csms.base.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.csms.base.R
import com.csms.chaperone.utils.extensions.isNotNullNorEmpty

class WarningDialogFragment : DialogFragment() {

    private val TAG = "WarningDialogFragment"
    private var title: String? = null
    private var message: String? = null
    private var positiveBtnLabel: String? = null
    private var negativeBtnLabel: String? = null

    private var themeResId: Int = 0
    private var fragment: Fragment? = null

    // Use this instance of the interface to deliver action events
    private var mListener: WarningDialogListener? = null

    /*
    * The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event call-backs. Each
    * method passes the DialogFragment in case the host needs to query it.
    */
    interface WarningDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)

        fun onDismiss(dialog: DialogFragment)
    }

    companion object {

        fun newInstance(
            title: String = "",
            message: String = "",
            pBtnLabel: String? = "",
            nBtnLabel: String? = "",
            themeResId: Int = 0
        ): WarningDialogFragment {
            val fragment = WarningDialogFragment()
            fragment.title = title
            fragment.message = message
            fragment.positiveBtnLabel = pBtnLabel
            fragment.negativeBtnLabel = nBtnLabel
            fragment.themeResId = themeResId
            return fragment
        }

        fun newInstance(
            title: String = "",
            message: String = "",
            pBtnLabel: String = "",
            nBtnLabel: String = "",
            themeResId: Int = 0,
            callingFragment: Fragment
        ): WarningDialogFragment {
            val fragment = newInstance(
                title,
                message,
                pBtnLabel,
                nBtnLabel,
                themeResId
            )
            fragment.fragment = callingFragment
            return fragment
        }
    }

    fun setListener(listener: WarningDialogListener) {
        mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Build the dialog and set up the button click handlers
        activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val rootView = inflater.inflate(R.layout.fragment_dialog_warning, null)
            builder.setView(rootView)

            val tvTitle = rootView.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = rootView.findViewById<TextView>(R.id.tvMessage)

            val btnPositive = rootView.findViewById<TextView>(R.id.btnPositive)
            val btnNegative = rootView.findViewById<TextView>(R.id.btnNegative)

            if (title.isNotNullNorEmpty()) tvTitle.text = title
            else tvTitle.visibility = View.GONE

            if (message.isNotNullNorEmpty()) tvMessage.text = message
            else tvMessage.visibility = View.GONE

            if (positiveBtnLabel.isNotNullNorEmpty()) btnPositive.text = positiveBtnLabel
            else btnPositive.visibility = View.GONE

            if (negativeBtnLabel.isNotNullNorEmpty()) btnNegative.text = negativeBtnLabel
            else btnNegative.visibility = View.GONE

            btnPositive.setOnClickListener {
                mListener?.onDialogPositiveClick(this@WarningDialogFragment)
                dismiss()
            }

            btnNegative.setOnClickListener {
                mListener?.onDialogNegativeClick(this@WarningDialogFragment)
                dismiss()
            }

            val dialog = builder.create()
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        if (mListener != null) return

        try {
            mListener = fragment?.let {
                fragment as WarningDialogListener
            } ?: context as WarningDialogListener


        } catch (e: ClassCastException) {
            throw ClassCastException("You must implement WarningDialogListener")
        }

    }

    override fun show(manager: FragmentManager, tag: String?) {
        val mDismissedField = DialogFragment::class.java.getDeclaredField("mDismissed")
        mDismissedField.isAccessible = true
        mDismissedField.setBoolean(this, false)

        val mShownByMeField = DialogFragment::class.java.getDeclaredField("mShownByMe")
        mShownByMeField.isAccessible = true
        mShownByMeField.setBoolean(this, true)

        try {
            manager.beginTransaction()
                .add(this, tag)
                .commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {
            Log.d(TAG, "show: Warning dialog failed to display.")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mListener?.onDismiss(this@WarningDialogFragment)
    }
}