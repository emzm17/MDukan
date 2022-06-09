package com.example.olx.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class SItem(
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
    val user_id:String?=null,
    val createdAt: Date?=null,
    val imagelist:String?=null
): Parcelable
