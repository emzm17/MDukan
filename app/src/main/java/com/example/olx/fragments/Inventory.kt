package com.example.olx.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.adapter.AllOrderAdapter
import com.example.olx.modal.AllOrder
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.android.synthetic.main.fragment_my_ads.*


class Inventory : BaseFragment() {
    private lateinit var list:ArrayList<AllOrder>
    private lateinit var adapter:AllOrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list= ArrayList()
        FirebaseFirestore.getInstance().collection("AllOrders")
            .whereEqualTo("sellerId",SharedPref(requireContext()).getString(Constants.USER_ID))
            .get().addOnSuccessListener {
                for(id in it) {
                    val s = id.toObject(AllOrder::class.java)
                    list.add(s)
                }
                if(list.size>0){
                    ll_inventory.visibility=View.GONE
                }
                else{
                    ll_inventory.visibility=View.VISIBLE
                }

                set()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_LONG).show()
            }

    }

    private fun set(){
        adapter= AllOrderAdapter(requireContext(),list)
        inventoryRcview.layoutManager=LinearLayoutManager(requireContext())
        inventoryRcview.adapter=adapter

    }


}