package com.example.olx.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.util.*
@Parcelize
@Entity(tableName="products")
data class ProductModel(
    @PrimaryKey
    @NotNull
    val productid:String,
    val price:String,
    val brand:String,
    val type:String,
    val user_id:String,
    val imagelist:String,
    val seller_name:String
):Parcelable