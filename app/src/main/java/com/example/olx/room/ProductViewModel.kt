package com.example.olx.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private var repository: Repository):ViewModel() {

    private lateinit var mutableLiveData:MutableList<List<ProductModel>>



   fun getcurrentItem(id:String):LiveData<List<ProductModel>>{
       return repository.getItem(id)
   }

    fun insert(p:ProductModel){
        viewModelScope.launch(Dispatchers.IO){
              repository.insert(p)
        }
    }

    fun delete(p:ProductModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(p)
        }
    }

    fun get():LiveData<List<ProductModel>>{
        return repository.getAll()
    }

}