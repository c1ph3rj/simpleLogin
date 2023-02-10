package com.c1ph3rj.simplelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.c1ph3rj.simplelogin.databinding.ActivityLoginScreenBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindLogin = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(viewBindLogin.root)

        onBackPressedDispatcher.addCallback(this, object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finishAffinity()
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
            firebaseAuth = FirebaseAuth.getInstance()

            try{
                registerNewUserBtn.setOnClickListener(){
                    startActivity(Intent(this, LoginScreen::class.java))
                }

                signInWithGoogleBtn.setOnClickListener(){
                    signUpWithGoogle()
                }

                signInBtn.setOnClickListener(){
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
            Toast.makeText(this, "Please Enter Your UserName to continue!", Toast.LENGTH_SHORT).show()
            userNameLayout.isErrorEnabled = true
            return
        }else{
            userNameLayout.isErrorEnabled = false
        }
        if(userNameField.text.toString().trim().length < 4 || !userNameField.text.toString().matches(Regex("^[a-zA-Z]+[0-9a-zA-z.]+[@][a-zA-Z0-9]+\\.[a-z]{2,}\$"))){
            Toast.makeText(this, "Please Enter a valid UserName", Toast.LENGTH_SHORT).show()
            userNameLayout.isErrorEnabled = true
            return
        }else {
            userNameLayout.isErrorEnabled = false
        }
        if(passwordField.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Enter Password to continue!", Toast.LENGTH_SHORT).show()
            passwordLayout.isErrorEnabled = true
            return
        }else {
            passwordLayout.isErrorEnabled = false
        }
        if(!passwordField.text.toString().trim().matches(Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$"))){
            MaterialAlertDialogBuilder(this@LoginScreen).setTitle("Password Validation!")
                .setMessage("Password Must Contains One small letter, on Capital letter and any one special character and Password must be longer than 8 letters.")
                .setPositiveButton("Ok"){ dialog, _ ->
                    dialog.dismiss()
                }.show()
            passwordLayout.isErrorEnabled = true
            return
        }
        else {
            passwordLayout.isErrorEnabled = false
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
                startActivity(Intent(this@LoginScreen, DashboardScreen::class.java))
            }.addOnFailureListener(this@LoginScreen){
                it.printStackTrace()
                Toast.makeText(this@LoginScreen, it.message , Toast.LENGTH_SHORT).show()
            }

    }

    private fun signUpWithGoogle(){
        try{
            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
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
            userDetailsEditor.apply()
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

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())


}