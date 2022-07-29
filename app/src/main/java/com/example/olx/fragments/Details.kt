package com.example.olx.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olx.BaseFragment
import com.example.olx.ImagePreview
import com.example.olx.R
import com.example.olx.adapter.DetailsImageAdapter
import com.example.olx.modal.SItem
import com.example.olx.room.*
import com.example.olx.util.Constants

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.item_details_2.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Details : BaseFragment(),DetailsImageAdapter.itemClickListener {
     private lateinit var DB:FirebaseFirestore
     private lateinit var sitem: SItem
     private lateinit var dao:ProductDao
     private var currentlist= arrayListOf<ProductModel>()
     private lateinit var db:AppDatabase
     private lateinit var repository: Repository
     private lateinit var vm:ProductViewModel
    private val args by navArgs<DetailsArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.item_details_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sitem=args.sItem
        DB= FirebaseFirestore.getInstance()
         if(sitem.type.equals(Constants.CAR)){
             ll_km_driven.visibility=View.VISIBLE
         }
         getItem()

         db=AppDatabase.getInstance(requireContext())
         repository= Repository(db)
         vm= ViewModelProvider(requireActivity(),ProductViewModelFactory(repository))[ProductViewModel::class.java]
         vm.getcurrentItem(sitem.id.toString()).observe(viewLifecycleOwner){
              currentlist.addAll(it)
         }
         cartAction(sitem.id.toString(),sitem.brand.toString(),
             sitem.type.toString(),
             sitem.imagelist.toString(),
             sitem.price.toString(),
             sitem.user_id.toString(),sitem.user_name.toString())

         Log.i("cart", currentlist.size.toString())

    }

    private fun getItem() {
         showprogress()
         DB.collection(sitem.type.toString())
             .document(sitem.id.toString())
             .get().addOnSuccessListener {
                 hideprogress()
                 sitem=it.toObject(SItem::class.java)!!
                 data()
                 viewAdapter()
             }

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun data() {
        val sdf= SimpleDateFormat("dd/MM/yyyy")
       tvItemPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(sitem.price!!.toInt())
       tvItemTitle.text=sitem.brand+"  "+sitem.title.toString()
       tvItemTown.text=sitem.city
       tvItemState.text="Jharkhand"
       tvDescriptionList.text=sitem.description
       tvAddressList.text=sitem.address
       tvYearPurchase.text=sdf.format(sitem.createdAt!!.time)
    }

    private fun viewAdapter() {
        val list=ArrayList<String>()
        list.add(sitem.imagelist.toString())
        val viewadapter=DetailsImageAdapter(requireContext(),list,this)
       ViewPage.adapter=viewadapter
        ViewPage.offscreenPageLimit=1

    }





    override fun onItemClick(position: Int) {
        startActivity(
            Intent(activity, ImagePreview::class.java).putExtra(
                "imageurl",
                sitem.imagelist
            )
        )

    }

    @SuppressLint("SetTextI18n")
    private fun cartAction(
        pid: String,
        brand: String,
        type: String,
        image: String,
        price: String,
        userid: String,
        username: String
    ){
          if(currentlist.size != 0){
              btnADD.text = "GO TO CART"
          }
         else{
              btnADD.text = "ADD TO CART"
          }
          btnADD.setOnClickListener {
               if(currentlist.size==0){
                   addtoCart(pid,brand,type,image,price,userid,username)
                   btnADD.text="Go TO CART"
               }
               else{
                   gotoCart()
               }
          }
    }
    private fun gotoCart(){
           val action=DetailsDirections.actionDetailsToCart()
           findNavController().navigate(action)
    }
    @SuppressLint("SetTextI18n")
    private fun addtoCart( pid:String, brand: String, type: String, image: String, price: String, userid:String,username: String){
                    val data=ProductModel(pid,price,brand,type,userid,image,username)
                   vm.insert(data)
               }
    }



