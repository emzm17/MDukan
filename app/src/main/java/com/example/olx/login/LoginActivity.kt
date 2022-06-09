package com.example.olx.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.olx.BaseActivity
import com.example.olx.MainActivity
import com.example.olx.R
import com.example.olx.util.Constants
import com.example.olx.util.SharedPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:BaseActivity() , View.OnClickListener{
  private var googleSignInOptions:GoogleSignInOptions?=null
  private var googleSignInClient:GoogleSignInClient?=null
  private var RC_SIGN_IN=1000
  private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()
        clickListener()
        configureGoogleSign()
    }


    private fun clickListener(){
        lview_google.setOnClickListener(this)
        lview_facebook.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
           when(p0?.id){
               R.id.lview_google->{
                      googleSignIn()
               }
               R.id.lview_facebook->{

               }
           }
    }
    /*Method for signing in with google*/
    private fun googleSignIn() {
          val signInIntent=googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    /*Confguring Google Sign In*/
    private fun configureGoogleSign() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,googleSignInOptions!!)

    }
    /* OnActivity Result CallBack
       * This methods is callback when you successfull or failed
       * to login with google or facebook
       * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN){
           val task = GoogleSignIn.getSignedInAccountFromIntent(data)
           try{
               val account=task.getResult(ApiException::class.java)
               fireabaseAuthWithGoogle(account!!)
           }catch (e:ApiException){
              Log.w("LoginActivity","Google Sign in Failed")
           }
        }
    }
    /*Check if User Login with google is Successfull or Failed*/
    private fun fireabaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credentials=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                if(account.email!=null){
                    SharedPref(this).setString(Constants.USER_EMAIL, account.email!!)
                }
                if(account.id!=null){
                    SharedPref(this).setString(Constants.USER_ID, account.id!!)
                }
                if(account.displayName!=null){
                    SharedPref(this).setString(Constants.USER_NAME, account.displayName!!)
                }
                if(account.photoUrl!=null){
                    SharedPref(this).setString(Constants.USER_PHOTO, account.photoUrl.toString()!!)
                }
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this,"Google Sign in Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

}