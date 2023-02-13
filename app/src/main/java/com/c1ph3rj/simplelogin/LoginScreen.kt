package com.c1ph3rj.simplelogin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.c1ph3rj.simplelogin.databinding.ActivityLoginScreenBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginScreen : AppCompatActivity() {
    private lateinit var viewBindLogin : ActivityLoginScreenBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var userNameField : TextInputEditText
    private lateinit var passwordField : TextInputEditText
    private lateinit var userNameLayout : TextInputLayout
    private lateinit var passwordLayout : TextInputLayout
    private lateinit var revealAnimation: RevealAnimation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindLogin = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(viewBindLogin.root)
        revealAnimation = RevealAnimation(viewBindLogin.root, this.intent, this)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = getColor(R.color.lightGreen)


        onBackPressedDispatcher.addCallback(this, object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                revealAnimation.unRevealActivity()
            }

        })

        init()
    }

    private fun init(){
        try{
            userNameField = viewBindLogin.userNameField
            userNameLayout = viewBindLogin.userNameLayout
            passwordLayout = viewBindLogin.passwordLayout
            passwordField = viewBindLogin.passwordField
            val forgetPassword = viewBindLogin.forgetPassword
            val signInBtn = viewBindLogin.signInBtn
            val signInWithGoogleBtn = viewBindLogin.signInWithGoogleBtn
            val registerNewUserBtn = viewBindLogin.newUserBtn
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val backBtn = viewBindLogin.backBtn

            try{
                backBtn.setOnClickListener(){
                    onBackPressedDispatcher.onBackPressed()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
            firebaseAuth = FirebaseAuth.getInstance()

            forgetPassword.setOnClickListener(){
                startRevealActivity(it, Intent(this, ForgetPasswordScreen::class.java))
            }

            try{
                userNameField.setOnFocusChangeListener(){_, hasFocus ->
                    if(hasFocus){
                        userNameLayout.error = null
                    }
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

            try{
                passwordField.setOnFocusChangeListener(){_, hasFocus ->
                    if(hasFocus){
                        passwordLayout.error = null
                    }
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

            try{
                registerNewUserBtn.setOnClickListener(){
                    startRevealActivity(it,Intent(this, RegisterScreen::class.java))
                }

                signInWithGoogleBtn.setOnClickListener(){
                    imm.hideSoftInputFromWindow(viewBindLogin.root.windowToken, 0)
                    userNameField.clearFocus()
                    passwordField.clearFocus()
                    signInWithGoogle()
                }

                signInBtn.setOnClickListener(){
                    imm.hideSoftInputFromWindow(viewBindLogin.root.windowToken, 0)
                    userNameField.clearFocus()
                    passwordField.clearFocus()
                    signInWithEmail()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun signInWithEmail(){
        if(userNameField.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Enter Your Email Address to continue!", Toast.LENGTH_SHORT).show()
            userNameLayout.isErrorEnabled = true
            userNameLayout.error = "Please Enter Your Email Address to continue!"
            return
        }else if(userNameField.text.toString().trim().length < 4 || !userNameField.text.toString().trim().matches(Regex("^[a-zA-Z]+[0-9a-zA-z.]+[@][a-zA-Z0-9]+\\.[a-z]{2,}\$"))){
            Toast.makeText(this, "Please Enter a valid Email Address", Toast.LENGTH_SHORT).show()
            userNameLayout.isErrorEnabled = true
            userNameLayout.error = "Please Enter a valid Email Address"
            return
        }else {
            userNameLayout.error = null
        }
        if(passwordField.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Enter Password to continue!", Toast.LENGTH_SHORT).show()
            passwordLayout.isErrorEnabled = true
            passwordLayout.error = "Please Enter Password to continue!"
            return
        }else if(!passwordField.text.toString().trim().matches(Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$"))){
            MaterialAlertDialogBuilder(this@LoginScreen).setTitle("Password Validation!")
                .setMessage("Password Must Contains One small letter, on Capital letter and any one special character and Password must be longer than 8 letters.")
                .setPositiveButton("Ok"){ dialog, _ ->
                    dialog.dismiss()
                }.show()
            passwordLayout.isErrorEnabled = true
            passwordLayout.error = "Please enter a valid password."
            return
        }
        else {
            passwordLayout.isErrorEnabled = false
            passwordLayout.error = null
        }

        firebaseAuth.signInWithEmailAndPassword(userNameField.text.toString().trim(), passwordField.text.toString().trim())
            .addOnSuccessListener(this@LoginScreen
            ) {
                val userDetails = getSharedPreferences("UserDetailsPref", Context.MODE_PRIVATE)
                val userDetailsEditor = userDetails.edit()
                userDetailsEditor.putString("e_mail", it.user?.email)
                userDetailsEditor.putString("uid", it.user?.uid)
                userDetailsEditor.putString("displayName", it.user?.displayName)
                userDetailsEditor.putString("photo", it.user?.photoUrl.toString())
                userDetailsEditor.putString("providerId", it.user?.providerId)
                userDetailsEditor.putString("phoneNumber", it.user?.phoneNumber)
                userDetailsEditor.apply()
                startRevealActivity(viewBindLogin.root, Intent(this@LoginScreen, DashboardScreen::class.java))
            }.addOnFailureListener(this@LoginScreen){
                it.printStackTrace()
                Toast.makeText(this@LoginScreen, it.message , Toast.LENGTH_SHORT).show()
            }

    }

    private fun signInWithGoogle(){
        try{
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("110262489068270042125")
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
            val signInIntent: Intent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        println(response)
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val userDetails = getSharedPreferences("UserDetailsPref", Context.MODE_PRIVATE)
            val userDetailsEditor = userDetails.edit()
            userDetailsEditor.putString("e_mail", user?.email)
            userDetailsEditor.putString("uid", user?.uid)
            userDetailsEditor.putString("displayName", user?.displayName)
            userDetailsEditor.putString("photo", user?.photoUrl.toString())
            userDetailsEditor.putString("providerId", user?.providerId)
            userDetailsEditor.putString("phoneNumber", user?.phoneNumber)
            response?.isNewUser?.let { userDetailsEditor.putBoolean("isNewUser", it) }
            userDetailsEditor.apply()
            startRevealActivity(viewBindLogin.root, Intent(this@LoginScreen, DashboardScreen::class.java))
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Toast.makeText(this@LoginScreen, response?.error?.message, Toast.LENGTH_SHORT).show()
        }
    }


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun startRevealActivity(v: View, intent:Intent) {
        //calculates the center of the View v you are passing
        val revealX = (v.x + v.width / 2).toInt()
        val revealY = (v.y + v.height / 2).toInt()

        //create an intent, that launches the second activity and pass the x and y coordinates

        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY)

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null)

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0)
    }
}