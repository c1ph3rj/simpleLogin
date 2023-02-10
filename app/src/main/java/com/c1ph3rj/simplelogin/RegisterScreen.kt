package com.c1ph3rj.simplelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.c1ph3rj.simplelogin.databinding.ActivityRegisterScreenBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegisterScreen : AppCompatActivity() {
    private lateinit var viewBindRegister : ActivityRegisterScreenBinding
    private lateinit var userNameField : TextInputEditText
    private lateinit var passwordField : TextInputEditText
    private lateinit var confirmPasswordField : TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindRegister = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(viewBindRegister.root)

        try{
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    finish()
                }

            })
            init()
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun init(){
        try{
            val alreadyUserBtn = viewBindRegister.alreadyAUserBtn
            userNameField = viewBindRegister.userNameField
            passwordField = viewBindRegister.passwordField
            confirmPasswordField = viewBindRegister.confirmPasswordField
            firebaseAuth = FirebaseAuth.getInstance()
            val signUpBtn = viewBindRegister.signUpBtn
            val signUpWithGoogle = viewBindRegister.signUpWithGoogleBtn


            try{
                alreadyUserBtn.setOnClickListener(){
                    startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
                    finish()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

            try{
                signUpBtn.setOnClickListener(){
                    signUpWithEmail()
                }

                signUpWithGoogle.setOnClickListener(){
                    signUpWithGoogle()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }catch(e: Exception){
            e.printStackTrace()
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
            Toast.makeText(this@RegisterScreen, response?.error?.message, Toast.LENGTH_SHORT).show()
        }
    }


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())


    private fun signUpWithEmail(){
        try{
            if(userNameField.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please Enter Your UserName to continue!", Toast.LENGTH_SHORT).show()
            }else if(userNameField.text.toString().trim().length < 4 || !userNameField.text.toString().matches(Regex("^[a-zA-Z]+[0-9a-zA-z.]+[@][a-zA-Z0-9]+\\.[a-z]{2,}\$"))){
                Toast.makeText(this, "Please Enter a valid UserName", Toast.LENGTH_SHORT).show()
            }else if(passwordField.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please Enter Password to continue!", Toast.LENGTH_SHORT).show()
            }else if(!passwordField.text.toString().trim().matches(Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$"))){
                MaterialAlertDialogBuilder(this@RegisterScreen).setTitle("Password Validation!")
                    .setMessage("Password Must Contains One small letter, on Capital letter and any one special character and Password must be longer than 8 letters.")
                    .setPositiveButton("Ok"){ dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }else if(confirmPasswordField.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please Enter Confirm Password to continue!", Toast.LENGTH_SHORT).show()
            }else if(passwordField.text.toString().trim() != confirmPasswordField.text.toString().trim()){
                Toast.makeText(this, "Password does not Match!", Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.createUserWithEmailAndPassword(userNameField.text.toString().trim(), passwordField.text.toString().trim())
                    .addOnSuccessListener(this@RegisterScreen
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
                        startActivity(Intent(this@RegisterScreen, DashboardScreen::class.java))
                    }.addOnFailureListener(this@RegisterScreen){
                        it.printStackTrace()
                        Toast.makeText(this@RegisterScreen, it.message , Toast.LENGTH_SHORT).show()
                    }
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

}