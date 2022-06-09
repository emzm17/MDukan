package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.modal.Item
import com.example.olx.modal.SItem
import kotlinx.android.synthetic.main.ads_item.view.*
import java.text.SimpleDateFormat


class BrowseAdapter(private val context: Context, private var myList: MutableList<SItem>, private var listener:ItemClickListener):
    RecyclerView.Adapter<BrowseAdapter.BrowseAdapterViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseAdapterViewModel {
        val v= LayoutInflater.from(context).inflate(R.layout.ads_item,parent,false)
        return BrowseAdapterViewModel(v)
    }

    override fun onBindViewHolder(holder: BrowseAdapterViewModel, position: Int) {
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


    inner class BrowseAdapterViewModel(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        val sdf= SimpleDateFormat("dd/MM/yyyy")

        @SuppressLint("SetTextI18n")
        fun bind(item: SItem)=with(itemView){
            itemView.spriceTv.text="Rs${item.price}"
            itemView.sAddressTv.text=item.address
            itemView.sbrandTv.text=item.brand
            itemView.sdateTv.text= sdf.format(item.createdAt!!.time)
            Glide.with(context).load(item.imagelist).placeholder(R.drawable.ic_placeholder).into(itemView.sell_imageview)

        }
    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }



}