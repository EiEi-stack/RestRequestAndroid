package com.example.restrequestandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {
    var TAG = "Login Activity"
    lateinit var callbackManager: CallbackManager

    //Firebase 依存系
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Firebase 依存系
        auth = FirebaseAuth.getInstance()
        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }
        //Facebookの依存関係
        callbackManager = CallbackManager.Factory.create()
        btn_fb_sign.setPermissions(listOf("public_profile", "email"))
        btn_fb_sign.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(TAG, "Success Login")
                getUserProfile(result?.accessToken, result?.accessToken?.userId)
                showNextActivity()
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login Cancel？led", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, error?.message, Toast.LENGTH_LONG).show()
            }
        })
        if (isLoggedIn()) {
            // Show the Activity with the logged in user

        } else {
            // Show the Home Activity
        }
        //Google依存関係
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("204796193447-rrd5s8da5eo5vsi49c42tleh237dp326.apps.googleusercontent.com")
            .requestEmail()
            .build();
        //val btn_google_sign = findViewById<Button>(R.id.btn_google_sign)
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btn_google_sign.visibility = View.VISIBLE
        btn_google_sign.setSize(SignInButton.SIZE_STANDARD)
        btn_google_sign.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            btn_google_sign.visibility = View.GONE
            showNextActivity()
        }
        //Google依存関係
    }

    //Firebase 依存系
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Facebook依存関係
        callbackManager.onActivityResult(requestCode, resultCode, data)
        //Facebook依存関係
        //Google依存関係
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            //Google依存関係
        }
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            //Google依存関係
            // Signed in successfully, show authenticated UI.
            btn_google_sign.visibility = View.GONE
            showNextActivity()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            btn_google_sign.visibility = View.VISIBLE
            //Google依存関係
        }
    }

    fun showNextActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {
        {
            val parameter = Bundle()
            parameter.putString(
                "fields", "id,first_name,middle_name,last_name,name,picture,email"
            )

            GraphRequest(
                token,
                "/$userId/",
                parameter,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    val jsonObject = response.jsonObject

                    if (BuildConfig.DEBUG) {
                        FacebookSdk.setIsDebugEnabled(true)
                        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                    }
                    //Facebook Id
                    if (jsonObject.has("id")) {
                        val facebookId = jsonObject.getString("id")
                        Log.i("Facebook id:", facebookId.toString())
                    } else {
                        Log.i("Facebook id:", "Not exists")
                    }
                    //Facebook First Name
                    if (jsonObject.has("first_name")) {
                        val facebookFirstName = jsonObject.getString("first_name")
                        Log.i("Facebook First Name:", facebookFirstName.toString())
                    } else {
                        Log.i("Facebook First Name:", "Not exists")
                    }
                    //Facebook Middle Name
                    if (jsonObject.has("middle_name")) {
                        val facebookMiddleName = jsonObject.getString("middle_name")
                        Log.i("Facebook Middle Name:", facebookMiddleName.toString())
                    } else {
                        Log.i("Facebook Middle Name:", "Not exists")
                    }
                    //Facebook Last Name
                    if (jsonObject.has("last_name")) {
                        val facebookLastName = jsonObject.getString("last_name")
                        Log.i("Facebook Last Name:", facebookLastName.toString())
                    } else {
                        Log.i("Facebook Last Name:", "Not exists")
                    }
                    //Facebook Name
                    if (jsonObject.has("name")) {
                        val facebookName = jsonObject.getString("name")
                        Log.i("Facebook Name:", facebookName.toString())
                    } else {
                        Log.i("Facebook Name:", "Not exists")
                    }
                    //Facebook Picture Pic URL
                    if (jsonObject.has("picture")) {
                        val facebookPictureObject = jsonObject.getJSONObject("picture")
                        if (facebookPictureObject.has("data")) {
                            val facebookDataObject = facebookPictureObject.getJSONObject("data")
                            if (facebookDataObject.has("url")) {
                                val facebookProfilePicURL = facebookDataObject.getString("url")
                                Log.i("Facebook Picture Pic URL:", facebookProfilePicURL)
                            }
                        }
                    } else {
                        Log.i("Facebook Name:", "Not exists")
                    }
                }).executeAsync()
        }
    }

}