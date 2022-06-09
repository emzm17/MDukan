package com.example.olx.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx.BaseFragment
import com.example.olx.R
import com.example.olx.adapter.CategoryAdapter
import com.example.olx.modal.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*


class Home : BaseFragment(),CategoryAdapter.ItemClickListener {
     private lateinit var db:FirebaseFirestore
     private lateinit var categories: MutableList<Categories>
     private lateinit var cAdapter:CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val root= inflater.inflate(R.layout.fragment_home, container, false)
       return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db= FirebaseFirestore.getInstance()
        getCategoryList()
        textListener()
    }

    private fun textListener() {
       etsearchview.addTextChangedListener(object :TextWatcher{
           override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              return
           }

           override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               return
           }

           override fun afterTextChanged(p0: Editable?) {
               filterList(p0.toString())
           }

       })
    }



    private fun getCategoryList() {
        showprogress()
        db.collection("Categories").get().addOnSuccessListener {
            hideprogress()
           categories=it.toObjects(Categories::class.java)
           setAdapter()
        }
    }

    private fun setAdapter() {
        rv_categories.layoutManager=GridLayoutManager(requireContext(),3)
        cAdapter= CategoryAdapter(requireContext(),categories,this)
        rv_categories.adapter=cAdapter
    }

    override fun onItemClick(position: Int) {
         val action=HomeDirections.actionHomeToBrowseItem(categories.get(position).key)
         findNavController().navigate(action)
    }
    private fun filterList(s: String) {
       var list:MutableList<Categories> = ArrayList()
       for(data in categories ){
              if(data.key.contains(s.capitalize()) || data.key.contains(s) ){
                  list.add(data)
              }
       }
        cAdapter.update(list)
    }
}
