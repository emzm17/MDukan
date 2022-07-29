package com.example.olx.room

import androidx.lifecycle.LiveData

class Repository(private var db:AppDatabase) {

    fun getItem(id:String):LiveData<List<ProductModel>>{
         return db.productDao().isExist(id)
    }

    suspend fun insert(p:ProductModel){
         db.productDao().insertProduct(p)
    }
    suspend fun delete(p:ProductModel){
        db.productDao().deleteProduct(p)
    }

    fun getAll():LiveData<List<ProductModel>> {
       return  db.productDao().getAllProduct()
    }

}