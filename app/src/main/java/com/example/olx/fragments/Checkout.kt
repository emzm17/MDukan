package com.example.olx.fragments


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.olx.BuildConfig
import com.example.olx.MainActivity
import com.example.olx.R
import com.example.olx.room.*
import com.example.olx.util.Constants
import com.example.olx.util.OnActivityResult
import com.example.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.json.JSONObject


class Checkout : Fragment() {
    private lateinit var db: AppDatabase
    private lateinit var repository: Repository
    private lateinit var vm: ProductViewModel
    private lateinit var DB:FirebaseFirestore
    private lateinit var flag1:String
    private val args by navArgs<CheckoutArgs>()
    private var flag=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserInfo()
        DB= FirebaseFirestore.getInstance()
        db=AppDatabase.getInstance(requireContext())
        repository= Repository(db)
        vm= ViewModelProvider(requireActivity(), ProductViewModelFactory(repository))[ProductViewModel::class.java]
        groupradio.setOnCheckedChangeListener { radioGroup, i ->
             when(i){
                   R.id.radia_id1->{
                        flag=1
                   }
                   R.id.radia_id2->{
                      flag=2
                   }
             }
        }
       setClick()


    }

    private fun check(): Boolean {
            if(user_addressEt.text.isNullOrEmpty() || user_phoneEt.text.isNullOrEmpty() || user_nameEt.text.isNullOrEmpty() || user_pincodeEt.text.isNullOrEmpty()
                ||   user_stateEt.text.isNullOrEmpty()  ||  flag == 0   )return false
     return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setClick() {
           BTNpayment.setOnClickListener {
                  if(check()) {
                      when(flag) {
                          1 -> {
                              val checkout = Checkout()
                              checkout.setKeyID(BuildConfig.RAZOR_PAY)
                              try {
                                  val options = JSONObject()
                                  options.put("name", "Olx-Clone")
                                  options.put("description", "Best C2C Ecommerce App")
                                  //You can omit the image option to fetch the image from dashboard
                                  options.put("theme.color", "#3399cc");
                                  options.put("currency", "INR");
                                  options.put("amount", "${args.cartItem.price.toInt()*100}")//pass amount in currency subunits
                                  val prefill = JSONObject()
                                  prefill.put("email", "dibya12345@razorpay.com")
                                  prefill.put("contact", "1234567891")
                                  options.put("prefill", prefill)
                                  checkout.open(requireActivity(), options)
                              } catch (e: Exception) {
                                  Toast.makeText(
                                      requireContext(),
                                      "Something went wrong " + e.message,
                                      Toast.LENGTH_LONG
                                  ).show()
                                  e.printStackTrace()
                              }
                              Paymentrequest()
                          }
                          2->{
                             upload()
                          }
                      }
                  }
           }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun Paymentrequest(){
        (activity as MainActivity).getResult(object : OnActivityResult {
            override fun result(bundle: Bundle) {
                flag1=bundle.get("PAYMENT").toString()
                when(flag1){
                    "Success"->{
                        upload()
                    }
                }
            }

        })

    }
    private fun upload() {
             saveData(args.cartItem.brand,args.cartItem.price,args.cartItem.user_id,args.cartItem.productid,args.cartItem.seller_name)
    }

    private fun saveData(name: String, price: String,sellerID:String, id: String,sellername:String) {
            val data= hashMapOf<String,Any>()
             data["name"]=name
             data["price"]= price
             data["id"]=id
             data["status"]="Ordered"
             data["sellerId"]=sellerID
             data["sellerName"]=sellername
             data["userId"]=SharedPref(requireContext()).getString(Constants.USER_ID).toString()
             val key=DB.collection("AllOrders").document().id
             data["OrderId"]=key
             DB.collection("AllOrders").add(data).addOnSuccessListener {
                  UpdateId(it.id)
            }.addOnFailureListener {
                 Toast.makeText(requireContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
    }
    @SuppressLint("SetTextI18n")
    private fun UpdateId(s:String){
         val hash= hashMapOf<String,String>()
         hash["OrderId"]=s
         DB.collection("AllOrders").document(s).update(hash as Map<String, Any>).addOnSuccessListener {
             Toast.makeText(requireContext(),"Order Placed",Toast.LENGTH_LONG).show()
             vm.delete(ProductModel(args.cartItem.productid,args.cartItem.price,args.cartItem.brand,args.cartItem.type,args.cartItem.user_id
                 ,args.cartItem.imagelist,args.cartItem.seller_name))
             val action = CheckoutDirections.actionCheckoutToHome()
             findNavController().navigate(action)
         }

    }


    private fun loadUserInfo() {
          user_nameEt.setText(SharedPref(requireContext()).getString(Constants.USER_NAME).toString())
          user_phoneEt.setText(SharedPref(requireContext()).getString(Constants.USER_PHONE).toString())
          user_addressEt.setText(SharedPref(requireContext()).getString(Constants.USER_ADDRESS).toString())
    }




}