package com.example.olx.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olx.R
import com.example.olx.adapter.CartAdapter
import com.example.olx.adapter.SellAdapter
import com.example.olx.room.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_sell.*
import java.text.NumberFormat
import java.util.*


class Cart : Fragment() {
    private lateinit var db:AppDatabase
    private lateinit var repository: Repository
    private lateinit var vm: ProductViewModel
    private lateinit var  adapter:CartAdapter
    private var list= arrayListOf<ProductModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        db=AppDatabase.getInstance(requireContext())
        repository= Repository(db)
        vm= ViewModelProvider(requireActivity(), ProductViewModelFactory(repository))[ProductViewModel::class.java]
        vm.get().observe(viewLifecycleOwner){
              list.clear()
              list.addAll(it)
              adapter.update(list)
              cost(it)
        }

        BTNcheckout.setOnClickListener {
             val action=CartDirections.actionCartToCheckout(list[0])
             findNavController().navigate(action)
        }



    }
    @SuppressLint("SetTextI18n")
    private fun cost(list:List<ProductModel>){
        if(list.isEmpty()){
            TvTotalItem.text = "Total item in Cart is: 0"
            TvTotalCost.text="Total cost of is :₹ 0"
        }
        else {
            var sum: Long = 0
            for (i in list) {
                sum += i.price.toLong()
            }
            TvTotalItem.text = "Total item in Cart is: ${list.size}"
            TvTotalCost.text = "Total cost of is :${NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                .format(sum.toInt())}"
        }
    }
    private fun setAdapter(){
        rcviewCart.layoutManager= LinearLayoutManager(requireContext())
        adapter= CartAdapter(requireContext(),list)
        rcviewCart.adapter=adapter
    }
}