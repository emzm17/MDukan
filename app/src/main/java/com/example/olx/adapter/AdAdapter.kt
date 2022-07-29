package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.modal.Categories
import com.example.olx.modal.Item
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import kotlinx.android.synthetic.main.ads_item.view.*
import kotlinx.android.synthetic.main.griditem.view.*
import kotlinx.android.synthetic.main.item_details_3.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AdAdapter(private val context: Context, private var myList:MutableList<SItem>, private var listener:ItemClickListener):
    RecyclerView.Adapter<AdAdapter.AdAdapterViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdAdapter.AdAdapterViewModel {
        val v= LayoutInflater.from(context).inflate(R.layout.item_details_3,parent,false)
        return AdAdapterViewModel(v)
    }

    override fun onBindViewHolder(holder: AdAdapterViewModel, position: Int) {
        holder.bind(myList[position])
        holder.itemView.setOnClickListener{
             listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun update(temp: MutableList<SItem>) {
        myList=temp
        notifyDataSetChanged()
    }


    inner class AdAdapterViewModel(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        val sdf=SimpleDateFormat("dd/MM/yyyy")

        @SuppressLint("SetTextI18n")
        fun bind(item: SItem)=with(itemView){
            itemView.tvPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(item.price!!.toInt())
            itemView.tvDetails.text=item.brand!!.capitalize()
            itemView.tvSeller.text=item.user_name
            itemView.tvcity.text=item.city
            Glide.with(this).load(item.imagelist).placeholder(R.drawable.ic_placeholder).into(itemView.ImageViewItem);
      

        }
    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }

}