package com.example.olx.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.adapter.AdAdapter
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my_ads.*

class MyAds : BaseFragment(),AdAdapter.ItemClickListener, View.OnClickListener {
    private var list:MutableList<SItem> = ArrayList()
    private var clist:MutableList<SItem> = ArrayList()
    private val DB by lazy{
    FirebaseFirestore.getInstance()
   }
private var myAdapter:AdAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        getAd()
        rcview_myad.layoutManager=LinearLayoutManager(requireContext())
    }


    private fun listener() {
       buttonPost.setOnClickListener(
           this
       )
    }

    private fun getAd() {
      showprogress()
      DB.collection(Constants.CATEGORIES)
          .get().addOnSuccessListener {
              for( i in it.documents){
                    getkey(i.getString(Constants.KEY))
              }
          }
    }

    private fun getkey(s: String?) {
            list.clear()
            DB.collection(s!!)
                .whereEqualTo("user_id",SharedPref(requireActivity()).getString(Constants.USER_ID))
                .get().addOnSuccessListener {
                    hideprogress()
                      val s =it.toObjects(SItem::class.java)
                      list.addAll(s)
                      if(list.size>0){
                          ll_no_data.visibility=View.GONE
                          setAdapter()
                      }
                      else{
                          ll_no_data.visibility=View.VISIBLE
                      }

                }
    }

    private fun setAdapter() {
         Log.i("MYADS","${list.size}")
         clist.clear()
         clist.addAll(list)
         myAdapter= AdAdapter(requireContext(),clist,this)
         rcview_myad.adapter=myAdapter
    }


    override fun onItemClick(position: Int) {
        val item=list[position]
        val action=MyAdsDirections.actionMyAdsToMyItemDetails2(item)
        findNavController().navigate(action)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonPost->{
                val action=MyAdsDirections.actionMyAdsToSell()
                findNavController().navigate(action)
            }
        }
    }

}