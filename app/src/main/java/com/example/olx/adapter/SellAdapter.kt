package com.example.olx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.modal.Categories
import kotlinx.android.synthetic.main.griditem.view.*

class SellAdapter(private var context: Context,private var categoryList:MutableList<Categories>,private val itemClickListener: ItemClickListener): RecyclerView.Adapter<SellAdapter.SellViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        val v=LayoutInflater.from(context).inflate(R.layout.sellitem,parent,false)
        return SellViewHolder(v)
    }

    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
       holder.bind(categoryList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
         return categoryList.size
    }
    inner class SellViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
           fun bind(item: Categories)=with(itemView){
               Glide.with(context).load(item.image_bw)
                   .placeholder(R.drawable.ic_placeholder)
                   .into(ivIcon)
               tvTitle.text=item.key

           }
    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }

}