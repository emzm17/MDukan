package com.example.olx


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.olx.login.LoginActivity
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity:BaseActivity() {
    private lateinit var handler: Handler
    private lateinit var fusedlocationclient:FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler=Handler()
        fusedlocationclient=LocationServices.getFusedLocationProviderClient(this)
        getCurrentlocation()
    }
    override fun onResume() {
        super.onResume()


    }

    override fun onRestart() {
        super.onRestart()
        if(SharedPref(this).getString(Constants.CITY_NAME)==null){
            getCurrentlocation()
        }
    }




    @SuppressLint("MissingPermission")
    private fun getCurrentlocation() {
         if(checkPermissions()){
              if(isGpsEnable()){
                  if (ActivityCompat.checkSelfPermission(
                          this,
                          Manifest.permission.ACCESS_FINE_LOCATION
                      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                          this,
                          Manifest.permission.ACCESS_COARSE_LOCATION
                      ) != PackageManager.PERMISSION_GRANTED
                  ) {
                      // TODO: Consider calling
                      //    ActivityCompat#requestPermissions
                      // here to request the missing permissions, and then overriding
                      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                      //                                          int[] grantResults)
                      // to handle the case where the user grants the permission. See the documentation
                      // for ActivityCompat#requestPermissions for more details.
                      return
                  }
                  fusedlocationclient.lastLocation.addOnCompleteListener {
                    val location=it.result
                    if(location==null){
                         Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                    }
                    else{
                            SharedPref(this).setString(Constants.CITY_NAME,getCityName(location).toString())
                            handler.postDelayed({
                                if (!isFinishing) {
                                    if (SharedPref(this).getString(Constants.USER_ID)!!.isEmpty()) {
                                        val intent = Intent(applicationContext, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                    },
                           3000)

                    }
                }
              }
              else{

                   Toast.makeText(this,"Turn on Location and Restart your App",Toast.LENGTH_LONG).show()
                   handler.postDelayed({
                       val intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                       startActivity(intent)
                   },3000)

              }
         }
         else{
              // request permission here
              askpermission()
         }
    }

    private fun checkPermissions(): Boolean {
         if( ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED     &&
             ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED  ){
             return true
         }
        return false
    }




    private fun askpermission() {
          ActivityCompat.requestPermissions(this,
              arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),Constants.REQUEST_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constants.REQUEST_PERMISSION_CODE){
            var flag:Boolean=false
            for( i in grantResults){
                   if(i==PackageManager.PERMISSION_GRANTED){
                         flag=true
                   }
              }
            if(flag){
                 isGpsEnable()
            }
        }
    }

    private fun isGpsEnable(): Boolean {
         val locationManger:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
         return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }




    private fun getCityName(location: Location): String? {
        var cityName = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude, 1
            )
            cityName = addresses[0].locality
        }catch(e: IOException) {
            when{
                e.message == "grpc failed" -> {/* display a Toast or Snackbar instead*/ }
                else -> throw e
            }
        }

        return cityName
    }




}

