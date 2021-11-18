package com.nthreads.base.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.nthreads.base.R

class CustomLanguagePicker private constructor(val needAllLangs: Boolean) : DialogFragment() {
    private var hostFragment: Fragment? = null
    private var mListener: LanguageSelectionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mListener = hostFragment?.let {
                hostFragment as LanguageSelectionListener
            } ?: context as LanguageSelectionListener


        } catch (e: ClassCastException) {
            throw ClassCastException("You must implement LanguageSelectionListener")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val rootView = inflater.inflate(R.layout.fragment_language_picker, null)

            val langEnglish = rootView.findViewById<RelativeLayout>(R.id.layoutEnglish)
            val langArabic = rootView.findViewById<RelativeLayout>(R.id.layoutArabic)

            val layoutFrench = rootView.findViewById<RelativeLayout>(R.id.layoutFrench)
            val layoutUrdu = rootView.findViewById<RelativeLayout>(R.id.layoutUrdu)
            val layoutHindi = rootView.findViewById<RelativeLayout>(R.id.layoutHindi)
            val layoutTamil = rootView.findViewById<RelativeLayout>(R.id.layoutTamil)
            val layoutMalayalam = rootView.findViewById<RelativeLayout>(R.id.layoutMalayalam)

            val layoutBengali = rootView.findViewById<RelativeLayout>(R.id.layoutBengali)
            val layoutChineseSimplified =
                rootView.findViewById<RelativeLayout>(R.id.layoutChineseSimplified)

            setClick(langEnglish, "en")
            setClick(langArabic, "ar")

            setClick(layoutFrench, "fr")
            setClick(layoutUrdu, "ur")
            setClick(layoutHindi, "hi")
            setClick(layoutTamil, "ta")
            setClick(layoutMalayalam, "ml")
            setClick(layoutBengali, "bn")

            setClick(layoutChineseSimplified, "zh-rCN")

            if (!needAllLangs) {
                layoutFrench.visibility = View.GONE
                layoutUrdu.visibility = View.GONE
                layoutHindi.visibility = View.GONE
                layoutTamil.visibility = View.GONE
                layoutMalayalam.visibility = View.GONE
                layoutBengali.visibility = View.GONE
                layoutChineseSimplified.visibility = View.GONE
            }



            builder.setView(rootView)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setClick(layout: RelativeLayout, locale: String) {
        layout.setOnClickListener {
            mListener?.onLanguageSelected(locale)
                ?: Log.e("TAG", "mListener is null, see attachment")
            dismiss()
        }
    }

    interface LanguageSelectionListener {
        fun onLanguageSelected(selectedLocale: String)
    }

    companion object {

        fun newInstance(needAllLangs: Boolean): CustomLanguagePicker {
            return CustomLanguagePicker(needAllLangs)
        }

        fun newInstance(hostFragment: Fragment, needAllLangs: Boolean): CustomLanguagePicker {
            val fragment = newInstance(needAllLangs)
            fragment.hostFragment = hostFragment
            return fragment
        }
    }
}