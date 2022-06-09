package com.example.olx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.fragments.UploadImage
import com.example.olx.modal.Categories
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.griditem.view.*

class UploadImageAdapter(private val context: Context, private var list:ArrayList<String>,
                         private var listener:ItemClickListener
):
    RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder>() {


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageViewHolder {

        val v=LayoutInflater.from(context).inflate(R.layout.adapter_image,null,false)
        return UploadImageViewHolder(v)
    }



    override fun onBindViewHolder(holder: UploadImageViewHolder, position: Int) {
                 if(position<list.size){
                     val bitmap=BitmapFactory.decodeFile(list[position])
                     holder.imageview.setImageBitmap(bitmap)
                 }
         holder.itemView.setOnClickListener(View.OnClickListener {
                   if(position==list.size){
                       listener.setItemClick()
                   }
         })
    }

    override fun getItemCount(): Int {
        return list.size+1
    }
    inner class UploadImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imageview=itemView.findViewById<ImageView>(R.id.selectimagev)
    }
    fun update(temp:ArrayList<String>){
         list=temp
         notifyDataSetChanged()
    }

    fun customNotify(selectedimagelist: ArrayList<String>) {
              this.list=selectedimagelist
               notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun setItemClick()

    }
}
