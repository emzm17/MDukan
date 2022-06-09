package com.example.olx.util

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    var sharedPref:SharedPreferences = context.getSharedPreferences(Constants.SharedPreferences,0)
    fun setString(key:String,value:String){
        sharedPref.edit().putString(key,value).commit()
    }

    fun getString(key: String):String?{
        return sharedPref.getString(key,"")
    }

}