package com.example.olx.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Item(
    val price:String?=null,
    val brand:String?=null,
    val address:String?=null,
    val year:String?=null,
    val driven:String?=null,
    val title:String?=null,
    val description:String?=null,
    val phone:String?=null,
    val type:String?=null,
    val id:String?=null,
    val date:Date?=null,
    val image:String?=null
):Parcelable
