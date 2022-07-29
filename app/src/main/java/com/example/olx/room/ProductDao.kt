package com.example.olx.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert
   suspend fun insertProduct(product: ProductModel)

    @Delete
    suspend fun deleteProduct(product: ProductModel)

    @Query("SELECT * FROM products " )
    fun getAllProduct():LiveData<List<ProductModel>>

    @Query("SELECT * FROM products WHERE productid=:id")
     fun isExist(id:String):LiveData<List<ProductModel>>

}