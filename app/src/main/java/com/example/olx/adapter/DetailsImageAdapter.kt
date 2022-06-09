package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.LayoutInflater.*
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.olx.R
import java.util.zip.Inflater

@SuppressLint("ViewConstructor")
class DetailsImageAdapter(private val context: Context,private var list:ArrayList<String>,private var listener: itemClickListener):
    PagerAdapter(){

    private var flag=true



    override fun getCount(): Int {
             if(flag){
                 flag=false
                 notifyDataSetChanged()
             }
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
           return view==`object`
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=inflater.inflate(R.layout.adapter_image_details,container,false)
        val imageview=view.findViewById<ImageView>(R.id.imgview)
        imageview.setOnClickListener {
             listener.onItemClick(position)
        }
        Glide.with(context)
            .load(list.get(position))
            .placeholder(R.drawable.big_placeholder)
            .into(imageview)
        container.addView(view, 0)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)

      }
    interface itemClickListener{
        fun onItemClick(position: Int)
    }


    }
