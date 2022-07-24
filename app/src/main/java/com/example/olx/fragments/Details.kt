package com.example.olx.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.olx.BaseFragment
import com.example.olx.ImagePreview
import com.example.olx.R
import com.example.olx.adapter.DetailsImageAdapter
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.item_details_2.*
import java.text.SimpleDateFormat


class Details : BaseFragment(),DetailsImageAdapter.itemClickListener {
     private lateinit var DB:FirebaseFirestore
     private lateinit var sitem: SItem
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

    @SuppressLint("SetTextI18n")
    private fun data() {
        val sdf= SimpleDateFormat("dd/MM/yyyy")
       tvItemPrice.text="₹ ${sitem.year}"
       tvItemTitle.text=sitem.brand
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


}