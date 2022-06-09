package com.example.olx.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.adapter.CategoryAdapter
import com.example.olx.adapter.SellAdapter
import com.example.olx.modal.Categories
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*


class Sell : BaseFragment() ,SellAdapter.ItemClickListener{
    private lateinit var db: FirebaseFirestore
    private lateinit var categories: MutableList<Categories>
    private lateinit var sAdapter:SellAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db= FirebaseFirestore.getInstance()
        getCategoryList()
    }

    private fun getCategoryList() {
            showprogress()
        db.collection("Categories").get().addOnSuccessListener {
            hideprogress()
            categories=it.toObjects(Categories::class.java)
            setAdapter()
        }
    }
    private fun setAdapter(){
        rcviewSell.layoutManager= GridLayoutManager(requireContext(),3)
        sAdapter= SellAdapter(requireContext(),categories,this)
        rcviewSell.adapter=sAdapter
    }



    override fun onItemClick(position: Int) {
              val key= categories[position].key
              val action=SellDirections.actionSellToIncludeDetails(key)
              findNavController().navigate(action)
    }


}