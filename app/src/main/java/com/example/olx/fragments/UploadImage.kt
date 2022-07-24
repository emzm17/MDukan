package com.example.olx.fragments


import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Gallery
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager

import com.example.olx.*
import com.example.olx.adapter.UploadImageAdapter
import com.example.olx.modal.SItem
import com.example.olx.util.Constants
import com.example.olx.util.OnActivityResultData
import com.example.olx.util.SharedPref
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_sell.*
import kotlinx.android.synthetic.main.fragment_upload_image.*
import java.io.File
import java.util.*


class UploadImage : BaseFragment(),View.OnClickListener,UploadImageAdapter.ItemClickListener{
    private val args by navArgs<UploadImageArgs>()
    private lateinit var imageUrilist:String
    private var uri:Uri?=null
    private lateinit var uploadTask: UploadTask
    private var selectedimagelist :String? = null
   private lateinit var outputFileUri:String
   private lateinit var dialog:BottomSheetDialog
   private  var selectedImage: File?=null
   private var TAG="uploadIamge"
   private lateinit var DB:FirebaseFirestore
   private lateinit var storage: FirebaseStorage
   private lateinit var storageReference:StorageReference
   private lateinit var imageReference:StorageReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("MICKY",args.item.type!!)
        DB= FirebaseFirestore.getInstance()
        storage= FirebaseStorage.getInstance()
        storageReference=storage.getReference()
        listener()
        requestC()
    }

    private fun requestC() {
        (activity as MainActivity).getOnActivityResult(object : OnActivityResultData{
            override fun resultData(bundle: Bundle) {

                val mPath=bundle.getStringArrayList(Constants.IMAGE_LIST)
                Log.i("DIBYA1","${mPath?.get(0) }")
                Log.i("DIBYA2","${mPath?.get(1)}")
                uri=Uri.parse(mPath?.get(0))
                val path=mPath?.get(1)
                imgview.setImageURI(uri)

                selectedImage =File(path.toString())
                outputFileUri=path.toString()
                selectedimagelist=(path.toString())

            }

        })
    }



    private fun listener() {
        previewbtn.setOnClickListener(this)
        uploadbtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
             R.id.previewbtn->{
                 showBottomSheet()
             }
             R.id.uploadbtn->{
                  if(selectedImage==null || selectedImage!!.exists()){
                      Toast.makeText(requireContext(),"First Select Images",Toast.LENGTH_LONG).show()
                  }
                  else{
                      save()
                  }
             }
        }
    }

    private fun showBottomSheet() {
        val layoutInflater=requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=layoutInflater.inflate(R.layout.bottomsheet,null ,false)
        dialog= BottomSheetDialog(requireActivity())
        dialog.setContentView(view)
        dialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(android.R.color.transparent))

        val textViewPhoto=dialog.findViewById<TextView>(R.id.tvphoto)
        val textViewCamera=dialog.findViewById<TextView>(R.id.tvcamera)
        val textViewCancel=dialog.findViewById<TextView>(R.id.tvcancel)
        textViewCancel!!.setOnClickListener {
            dialog.dismiss()
        }
        textViewPhoto!!.setOnClickListener {
            dialog.dismiss()
            chooseImagePicker()
        }
        dialog.show()
        val lp=WindowManager.LayoutParams()
        val window=dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width=WindowManager.LayoutParams.MATCH_PARENT
        lp.height=WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes=lp
    }

    private fun chooseImagePicker(){
         val intent=Intent(Intent.ACTION_PICK)
         intent.type="image/*"
         requireActivity().startActivityForResult(intent,Constants.REQUEST_CODE)

    }

    override fun setItemClick() {
        showBottomSheet()
    }

    private fun save(){

             val file=File(selectedimagelist!!)
             upload(file,file.name)
    }
    private fun upload(file:File,name:String){
        imageReference=storageReference.child("images/$name")
        uploadTask=imageReference.putFile(uri!!)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation imageReference.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUrilist=task.result.toString()
                post()
    }
    }
    }

    private fun post() {

        val docId=DB.collection(args.item.type.toString()).document().id
        Log.i("DOCID","docId")
        val user_id=SharedPref(requireActivity()).getString(Constants.USER_ID)
        val s= SItem(args.item.price,args.item.brand,args.item.address,args.item.year,args.item.driven,
            args.item.title,args.item.description,args.item.phone,args.item.type,docId,
               user_id!!,
            Date(),imageUrilist,SharedPref(requireContext()).getString(Constants.CITY_NAME))

        DB.collection(args.item.type.toString())
            .add(s)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                Log.w(TAG, "DataasId" + it.id)
                updateDocId(it.id)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }
    private fun updateDocId(id:String){
        val docData= hashMapOf<String,String>(
            Constants.ID to id
        )

        DB.collection(args.item.type.toString())
            .document(id.toString())
            .update(docData as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(requireContext(), "Ad Posted Successfully", Toast.LENGTH_LONG).show()
            }
         val action=UploadImageDirections.actionUploadImageToMyAds()
          findNavController().navigate(action)
    }


}