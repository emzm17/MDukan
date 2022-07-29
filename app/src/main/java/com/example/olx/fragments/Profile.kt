package com.example.olx.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ShareActionProvider
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.login.LoginActivity
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*


class Profile : BaseFragment() ,View.OnClickListener{
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()
        setData()
        clicklistener()
    }

    private fun clicklistener() {
        llsettings.setOnClickListener(this)
        lllogout.setOnClickListener(this)
        llMyInventory.setOnClickListener(this)
        llMyOrder.setOnClickListener(this)
    }

    private fun setData() {
        tvName.text=SharedPref(requireContext()).getString(Constants.USER_NAME).toString()
        tvEmail.text=SharedPref(requireContext()).getString(Constants.USER_EMAIL).toString()
        Glide.with(requireActivity()).load(SharedPref(requireContext()).getString(Constants.USER_PHOTO)).placeholder(R.drawable.avatar).into(profile_pic)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
             R.id.lllogout->{
                 alertdialog()
             }
            R.id.llsettings->{
                  val action=ProfileDirections.actionProfileToSettings()
                  findNavController().navigate(action)
            }
            R.id.llMyInventory->{
                val action=ProfileDirections.actionProfileToInventory()
                findNavController().navigate(action)
            }
            R.id.llMyOrder->{
                val action=ProfileDirections.actionProfileToOrder()
                findNavController().navigate(action)
            }
        }
    }
    private fun alertdialog(){
        val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are are sure you want to logout?")
            builder.setIcon(R.drawable.ic_warning)
            builder.setPositiveButton("Yes" ){ dialog: DialogInterface, i: Int ->
              auth.signOut()
              clearSession()
              startActivity(Intent(requireContext(),LoginActivity::class.java))
              requireActivity().finish()
               dialog.dismiss()
            }
          builder.setNegativeButton("No"){ dialog:DialogInterface,i:Int->
              dialog.dismiss()
          }
          val alertDialog=builder.create()
          alertDialog.setCancelable(false)
          alertDialog.show()
    }

    private fun clearSession() {
         SharedPref(requireActivity()).setString(Constants.USER_PHOTO,"")
         SharedPref(requireActivity()).setString(Constants.USER_NAME,"")
         SharedPref(requireActivity()).setString(Constants.USER_ID,"")
         SharedPref(requireActivity()).setString(Constants.USER_EMAIL,"")
         SharedPref(requireContext()).setString(Constants.USER_PHOTO,"")


    }
}