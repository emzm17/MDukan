package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.room.AppDatabase
import com.example.olx.room.ProductModel
import kotlinx.android.synthetic.main.cart_item.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class CartAdapter(private val context: Context,private var cartlist:List<ProductModel>):
    RecyclerView.Adapter<CartAdapter.CartAdapterViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapterViewModel {
        val v= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false)
        return CartAdapterViewModel(v)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: CartAdapterViewModel, position: Int) {
        holder.bind(cartlist[position])
        val dao=AppDatabase.getInstance(context).productDao()
        holder.itemView.imgDelete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(cartlist[position])
            }
        }
    }
    fun update(list:List<ProductModel>){
         cartlist=list
         notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return cartlist.size
    }

   inner class CartAdapterViewModel(itemView: View):RecyclerView.ViewHolder(itemView) {
           @SuppressLint("SetTextI18n")
           fun bind(item:ProductModel)= with(itemView){
                itemView.cartItemPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(item.price.toInt())
                itemView.cartItemBrand.text=item.brand
                itemView.cartItemSeller.text=item.seller_name
                itemView.cartItemCity.text="Ranchi Jharkhand"
               Glide.with(context).load(item.imagelist).into(itemView.cartItemImage)
           }
    }




}