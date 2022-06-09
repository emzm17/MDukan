package com.example.olx.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.adapter.BrowseAdapter
import com.example.olx.modal.Item
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_browse_item.*
import kotlinx.android.synthetic.main.fragment_home.*

class BrowseItem :  BaseFragment(),BrowseAdapter.ItemClickListener {
    private lateinit var DB:FirebaseFirestore
    private val args by navArgs<BrowseItemArgs>()
    private var list:MutableList<SItem> = ArrayList()
    private var clist:MutableList<SItem> = ArrayList()
    private lateinit var adapter: BrowseAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DB= FirebaseFirestore.getInstance()
        rv_browse.layoutManager=LinearLayoutManager(requireContext())
        retrivelist()
    }

    private fun retrivelist() {
        list.clear()
        showprogress()
        DB.collection(args.key)
            .whereNotEqualTo("user_id",SharedPref(requireContext()).getString(Constants.USER_ID))
            .get().addOnSuccessListener {
                hideprogress()
                val s=it.toObjects(SItem::class.java)
                list.addAll(s)
               setAdapter()
        }

    }

    private fun setAdapter() {
        clist.clear()
        clist.addAll(list)
        adapter= BrowseAdapter(requireContext(),clist,this)
        rv_browse.adapter=adapter
    }

    override fun onItemClick(position: Int) {
        val item=list[position]

        val action=BrowseItemDirections.actionBrowseItemToDetails(item)
        findNavController().navigate(action)
    }


}