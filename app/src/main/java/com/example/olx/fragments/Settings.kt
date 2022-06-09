package com.example.olx.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.olx.R
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import kotlinx.android.synthetic.main.fragment_settings.*


class Settings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sFullname.setText(SharedPref(requireContext()).getString(Constants.USER_NAME))
        sEmail.setText(SharedPref(requireContext()).getString(Constants.USER_EMAIL))
        sPhone.setText(SharedPref(requireContext()).getString(Constants.USER_PHONE))
        sAddress.setText(SharedPref(requireContext()).getString(Constants.USER_ADDRESS))

         clickListener()
    }

    private fun clickListener() {
         ssaveBtn.setOnClickListener {
              saveDetails()
         }
    }

    private fun saveDetails() {
        if(sFullname.text.toString().isEmpty()){
             sFullname.setError("Name Can't be Empty")
        }
        else if(sEmail.text.toString().isEmpty()){
             sEmail.setError("Email Can't be Empty")
        }
        else if(sPhone.text.toString().isEmpty()){
            sPhone.setError("Phone Number can't be Empty")
        }
        else if(sAddress.text.toString().isEmpty()){
            sAddress.setError("Address can't be Empty")
        }
        else{
             SharedPref(requireContext()).setString(Constants.USER_PHONE,sPhone.text.toString())
             SharedPref(requireContext()).setString(Constants.USER_NAME,sFullname.text.toString())
             SharedPref(requireContext()).setString(Constants.USER_EMAIL,sEmail.text.toString())
             SharedPref(requireContext()).setString(Constants.USER_ADDRESS,sAddress.text.toString())
             Toast.makeText(requireContext(),"Successfully Saved",Toast.LENGTH_LONG).show()
             val action=SettingsDirections.actionSettingsToProfile()
             findNavController().navigate(action)
        }
    }

}