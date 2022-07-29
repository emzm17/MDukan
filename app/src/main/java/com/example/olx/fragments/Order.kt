package com.example.olx.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.R
import com.example.olx.adapter.AllOrderAdapter
import com.example.olx.adapter.MyOrderAdapter
import com.example.olx.modal.AllOrder
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.android.synthetic.main.fragment_order.*


class Order : Fragment() {
    private lateinit var list:ArrayList<AllOrder>
    private lateinit var adapter: MyOrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list= ArrayList()
        FirebaseFirestore.getInstance().collection("AllOrders")
            .whereEqualTo("userId", SharedPref(requireContext()).getString(Constants.USER_ID))
            .get().addOnSuccessListener {
                for(id in it) {
                    val s = id.toObject(AllOrder::class.java)
                    list.add(s)

                }
                if(list.size>0){
                    ll_order.visibility=View.GONE
                }
                else{
                    ll_order.visibility=View.VISIBLE
                }
                Log.i("SIZE",list.size.toString())
                set()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"${it.message}", Toast.LENGTH_LONG).show()
            }

    }

    private fun set(){
        adapter= MyOrderAdapter(requireContext(),list)
        orderRcview.layoutManager= LinearLayoutManager(requireContext())
        orderRcview.adapter=adapter

    }




}