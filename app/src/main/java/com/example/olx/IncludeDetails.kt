package com.example.olx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olx.modal.Item
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import kotlinx.android.synthetic.main.fragment_include_details.*


class IncludeDetails : BaseFragment() ,View.OnClickListener {
    private val args by navArgs<IncludeDetailsArgs>()
    private var car: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_include_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextListener()
        if (args.key == (Constants.CAR)) {
            lldriven.visibility = View.VISIBLE
        }
    }

    private fun nextListener() {
        nextTv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.nextTv -> {
                sendData()
            }
        }
    }

    private fun sendData() {
        if (args.key == Constants.CAR) {
            car = drivenEt.text.toString()
        }

        if (!brandEt.text.isNullOrEmpty() && !addressEt.text.isNullOrEmpty() &&
            !yearEt.text.isNullOrEmpty() && !titleEt.text.isNullOrEmpty() &&
            !descriptionEt.text.isNullOrEmpty() && !phone_numberEt.text.isNullOrEmpty()
        ) {
            val item = Item(
                priceEt.text.toString(),
                brandEt.text.toString(),
                addressEt.text.toString(),
                yearEt.text.toString(),
                car,
                titleEt.text.toString(),
                descriptionEt.text.toString(),
                phone_numberEt.text.toString(),
                args.key
            )
            SharedPref(requireContext()).setString(Constants.USER_ADDRESS,addressEt.text.toString())
            SharedPref(requireContext()).setString(Constants.USER_PHONE,phone_numberEt.text.toString())
            val action = IncludeDetailsDirections.actionIncludeDetailsToUploadImage(item)
            findNavController().navigate(action)
        }
    }
}