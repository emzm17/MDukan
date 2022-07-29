package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.olx.R
import com.example.olx.modal.AllOrder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_inventory.view.*
import java.text.NumberFormat
import java.util.*

class AllOrderAdapter(private val context: Context, private var list:List<AllOrder>):RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        val v= LayoutInflater.from(context).inflate(R.layout.item_inventory,parent,false)
        return AllOrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
         holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class AllOrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
             @SuppressLint("SetTextI18n", "ResourceAsColor")
             fun bind(item:AllOrder)=with(itemView){
                productTitle.text="Item Name: ${item.name}"
                productPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(item.price.toInt())
                productId.text="Item OrderId: ${item.OrderId}"

                 cancelBTN.setOnClickListener {
                    ProceedBTN.isEnabled=false
                     updatestatus("Canceled",item.OrderId)
                 }
                when(item.status){
                    "Ordered"->{
                          ProceedBTN.text="Ordered"

                          ProceedBTN.setOnClickListener {
                              updatestatus("Dispatched",item.OrderId)
                          }
                    }
                    "Dispatched"->{
                        ProceedBTN.text="Dispatched"
                        ProceedBTN.setOnClickListener {
                            updatestatus("Delivered",item.OrderId)
                        }
                    }
                    "Delivered"->{
                        ProceedBTN.text="Already Delivered"
                        cancelBTN.isEnabled=false
                       cancelBTN.setBackgroundColor(R.color.dark_gray)
                    }
                }
             }
    }
    fun updatestatus(s:String,doc:String){
        val data= hashMapOf<String,Any>()
        data["status"]=s
        FirebaseFirestore.getInstance().collection("AllOrders")
            .document(doc).update(data).addOnSuccessListener {
                 Toast.makeText(context,"Status Updated",Toast.LENGTH_SHORT).show()
            }
    }


}