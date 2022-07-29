package com.example.olx.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductModel::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun productDao():ProductDao


    companion object{
        private var database:AppDatabase?=null
        private var DATABASE_NAME="product_database"


        fun getInstance(context: Context):AppDatabase{
             if(database==null) {
                 synchronized(this) {
                     database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                             .build()
                 }
             }
             return database!!
        }
    }
}