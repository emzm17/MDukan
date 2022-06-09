package com.example.olx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.modal.Categories
import kotlinx.android.synthetic.main.griditem.view.*

class CategoryAdapter(private val context: Context, private var categoryList:MutableList<Categories>, private val itemClickListener:ItemClickListener): RecyclerView.Adapter<CategoryAdapter.AdapterViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewModel {
        val v=LayoutInflater.from(context).inflate(R.layout.griditem,parent,false)
        return AdapterViewModel(v)
    }

    override fun onBindViewHolder(holder: AdapterViewModel, position: Int) {
        holder.bind(categoryList[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
           itemClickListener.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
         return categoryList.size
    }

    fun update(temp: MutableList<Categories>) {
       categoryList=temp
       notifyDataSetChanged()
    }


    inner class AdapterViewModel(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item:Categories)=with(itemView){
            Glide.with(context).load(item.image)
             .placeholder(R.drawable.ic_placeholder)
             .into(ivIcon)
            tvTitle.text=item.key

        }


    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }

}