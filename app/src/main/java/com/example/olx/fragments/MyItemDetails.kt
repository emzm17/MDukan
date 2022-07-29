package com.example.olx.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olx.BaseFragment
import com.example.olx.ImagePreview
import com.example.olx.R
import com.example.olx.adapter.DetailsImageAdapter
import com.example.olx.login.LoginActivity
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.ll_km_driven
import kotlinx.android.synthetic.main.fragment_my_item_details.*
import kotlinx.android.synthetic.main.item_details_2.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyItemDetails : BaseFragment(),DetailsImageAdapter.itemClickListener {
    private lateinit var DB: FirebaseFirestore
    private lateinit var Sitem: SItem

    private val args by navArgs<MyItemDetailsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DB= FirebaseFirestore.getInstance()
        if(args.sItem.type.equals(Constants.CAR)){
            ll_km_driven.visibility=View.VISIBLE
        }
        getItem()
        clickListener()

    }
        private fun clickListener() {
            Mbtndelete.setOnClickListener {
                 alert()
            }
        }

        private fun alert() {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Remove")
            builder.setMessage("Are are sure you want to delete your Ad?")
            builder.setIcon(R.drawable.ic_warning)
            builder.setPositiveButton("Yes" ){ dialog: DialogInterface, i: Int ->
                DB.collection(args.sItem.type.toString())
                    .document(args.sItem.id.toString())
                    .delete().addOnSuccessListener {
                        Toast.makeText(requireContext(),"Successfully Remove your Ad",Toast.LENGTH_LONG).show()
                    }
                val action=MyItemDetailsDirections.actionMyItemDetails2ToMyAds()
                findNavController().navigate(action)
                dialog.dismiss()
            }
            builder.setNegativeButton("No"){ dialog:DialogInterface,i:Int->
                dialog.dismiss()
            }
            val alertDialog=builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

    private fun getItem() {
        showprogress()
        DB.collection(args.sItem.type.toString())
            .document(args.sItem.id.toString())
            .get().addOnSuccessListener {
                hideprogress()
               Sitem=it.toObject(SItem::class.java)!!
                data()
                viewAdapter()
            }
    }

    private fun viewAdapter() {
        val list=ArrayList<String>()
        list.add(args.sItem.imagelist.toString())
        val viewadapter= DetailsImageAdapter(requireContext(),list,this)
        MViewPage.adapter=viewadapter
        MViewPage.offscreenPageLimit=1
    }

    private fun data() {
        val sdf= SimpleDateFormat("dd/MM/yyyy")
        MtvItemPrice.text= NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(args.sItem.price!!.toInt())
        MtvItemTitle.text=args.sItem.brand+"  "+args.sItem.title.toString()
        MtvItemTown.text=args.sItem.city
        MtvItemState.text="Jharkhand"
        MtvDescriptionList.text=args.sItem.description
        MtvAddressList.text=args.sItem.address
        MtvYearPurchase.text=sdf.format(args.sItem.createdAt!!.time)
    }

    override fun onItemClick(position: Int) {
        startActivity(
            Intent(activity, ImagePreview::class.java).putExtra(
                "imageurl",
                Sitem.imagelist
            )
        )
    }

}