package com.example.olx.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.olx.BaseFragment
import com.example.olx.ImagePreview
import com.example.olx.R
import com.example.olx.adapter.DetailsImageAdapter
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.SimpleDateFormat


class Details : BaseFragment(),DetailsImageAdapter.itemClickListener {
     private lateinit var DB:FirebaseFirestore
     private lateinit var sitem: SItem

    private val args by navArgs<DetailsArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sitem=args.sItem
        DB= FirebaseFirestore.getInstance()
         if(sitem.type.equals(Constants.CAR)){
             ll_km_driven.visibility=View.VISIBLE
         }
         getItem()
         clickListener()
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
        tvPrice.text = sitem.price
        tvTitle.text = sitem.title
        tvBrand.text = sitem.brand
        tvPhone.text = sitem.phone
        tvAddress.text = sitem.address
        tvDescription.text=sitem.description
         tvKmDriven.text=sitem.driven

         tvYear.text=sitem.year
         tvDate.text=sdf.format(sitem.createdAt!!.time)
    }

    private fun viewAdapter() {
        val list=ArrayList<String>()
        list.add(sitem.imagelist.toString())
        val viewadapter=DetailsImageAdapter(requireContext(),list,this)
        idViewPager.adapter=viewadapter
        idViewPager.offscreenPageLimit=1


    }

    private fun clickListener() {
        buttonCall.setOnClickListener{
            val num=sitem.phone.toString().trim()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(num)))
            startActivity(intent)
        }
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