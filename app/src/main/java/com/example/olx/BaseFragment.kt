package com.example.olx

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(){
    lateinit var mDailog: Dialog

    open fun showprogress(){
        mDailog= Dialog(requireActivity())
        mDailog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDailog.setContentView(R.layout.dialog_progress)
        mDailog.setCancelable(true)
    }
    open fun hideprogress(){
        mDailog.dismiss()
    }
}