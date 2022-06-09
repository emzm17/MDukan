package com.example.olx

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.olx.util.Constants
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreview : BaseActivity() {
    private var mScaleGestureDetector:ScaleGestureDetector?=null
    private var mScaleFactor=1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_image_preview)

        Log.i("IMAGEPREVIEW","error")

        val extras = getIntent().getExtras()
        if (extras?.containsKey("imageuri")!!) {
            val imageUri = extras.getString("imageuri")
            val myBitmap = BitmapFactory.decodeFile(imageUri)
          ImageView!!.setImageBitmap(myBitmap)
        } else if (extras.containsKey("imageurl")) {
            val imageUrl = extras.getString("imageurl")
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.big_placeholder)
                .into(ImageView)

        }

        ImageView.scaleX=mScaleFactor
        ImageView.scaleY=mScaleFactor
        
        mScaleGestureDetector= ScaleGestureDetector(this,ScaleListener())
    }



    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            mScaleFactor=p0?.scaleFactor!!
            mScaleFactor=Math.max(0.1f,Math.min(mScaleFactor,10.0f))
            ImageView.scaleX=mScaleFactor
            ImageView.scaleY=mScaleFactor

            return true
        }

    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        mScaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }





}