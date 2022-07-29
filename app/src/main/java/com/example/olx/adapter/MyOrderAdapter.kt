package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.modal.AllOrder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_inventory.view.*
import kotlinx.android.synthetic.main.order_item.view.*
import java.text.NumberFormat
import java.util.*

class MyOrderAdapter(private val context: Context, private var list:List<AllOrder>): RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyOrderViewHolder {
        val v= LayoutInflater.from(context).inflate(R.layout.order_item,parent,false)
        return MyOrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class MyOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(item: AllOrder)=with(itemView){
            MyproductTitle.text=item.name
            MyproductPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(item.price.toInt())
            MyproductId.text="OrderId: ${item.OrderId}"
            MyproductStatus.text="Status: ${item.status}"
            Glide.with(context).load(item.image).placeholder(R.drawable.ic_placeholder).into(myorderImage)
            MyBtnCancel.setOnClickListener {
                updatestatus("Canceled",item.OrderId)
            }
            when(item.status){
                "Ordered"->{
                    MyproductStatus.text="Status: Ordered"
                    }
                "Dispatched"->{
                    MyproductStatus.text="Status: Dispatched"
                }
                "Delivered"->{
                    MyproductStatus.text="Status: Delivered"
                    MyBtnCancel.isEnabled=false
                }
            }
        }
    }
    fun updatestatus(s:String,doc:String){
        val data= hashMapOf<String,Any>()
        data["status"]=s
        FirebaseFirestore.getInstance().collection("AllOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context,"Status Updated", Toast.LENGTH_SHORT).show()
            }
    }


}