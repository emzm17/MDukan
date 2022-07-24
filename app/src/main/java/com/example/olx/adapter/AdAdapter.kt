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
import java.text.SimpleDateFormat

class AdAdapter(private val context: Context, private var myList:MutableList<SItem>, private var listener:ItemClickListener):
    RecyclerView.Adapter<AdAdapter.AdAdapterViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdAdapter.AdAdapterViewModel {
        val v= LayoutInflater.from(context).inflate(R.layout.ads_item,parent,false)
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
            itemView.spriceTv.text="₹ ${item.price}"
            itemView.sbrandTv.text=item.brand
            itemView.syearTv.text=item.year
            itemView.scityTv.text=item.city
            Glide.with(context).load(item.imagelist).placeholder(R.drawable.ic_placeholder).into(itemView.sell_imageview)

        }
    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }

}