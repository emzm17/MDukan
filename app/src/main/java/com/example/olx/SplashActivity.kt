package com.example.olx


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.view.Gravity.apply
import androidx.core.app.ActivityCompat
import com.example.olx.login.LoginActivity
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit


@SuppressLint("CustomSplashScreen")
class SplashActivity:BaseActivity() {
  private lateinit var handler:Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(SharedPref(this).getString(Constants.USER_ID)!!.isEmpty()){
            handler= Handler()
            handler.postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },2000)
        }
        else{
            handler= Handler()
            handler.postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },2000)
        }


    }

}