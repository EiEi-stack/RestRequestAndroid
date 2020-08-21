package com.example.restrequestandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.btn_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    //Firebase 依存系
    private lateinit var auth: FirebaseAuth
    var TAG="SignUpActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        btn_sign_up.setOnClickListener {
            signUpUser()
        }
    }
    fun signUpUser(){

        if(tv_username.text.toString().isEmpty()){
            tv_username.error="Plese enter email";
            tv_username.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(tv_username.text.toString()).matches()){
            tv_username.error="Please enter valid email";
            tv_username.requestFocus()
            return
        }
        if(tv_password.text.toString().isEmpty()){
            tv_password.error="Plese enter password";
            tv_password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(tv_username.text.toString(), tv_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "SignUp failed.Try again after some times",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }
}
