package com.example.olx

import android.R.attr
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.olx.util.Constants
import com.example.olx.util.OnActivityResultData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : BaseActivity() {
    lateinit var onActivityResultData: OnActivityResultData
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.container_fragment)
        val appBarConfiguration= AppBarConfiguration(
            setOf(R.id.Home,R.id.Sell,R.id.MyAds,R.id.Profile))
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
         return navController.navigateUp() || super.onSupportNavigateUp()
    }
     fun getOnActivityResult(onActivityResultData: OnActivityResultData){
         this.onActivityResultData=onActivityResultData
     }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
                    if(resultCode==RESULT_OK ) {
                              if(requestCode == Constants.REQUEST_CODE) {
                                  val bundle = Bundle()
                                  val mPath:ArrayList<String> = ArrayList()
                                  mPath.add(data!!.data.toString())
                                  mPath.add(data!!.data!!.path.toString())
                                  bundle.putStringArrayList(Constants.IMAGE_LIST, mPath)
                                  onActivityResultData.resultData(bundle)

                              }
                        else
                              {
                                  Log.i("MAIN","error")
                              }
                    }
                 else{
                     Log.i("MAIN ACTIVITY","eRROR")
                    }

              }

}