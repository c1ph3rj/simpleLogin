package com.c1ph3rj.simplelogin

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.c1ph3rj.simplelogin.databinding.ActivityForgetPasswordScreenBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordScreen : AppCompatActivity() {

    private lateinit var viewBindForgetPassword: ActivityForgetPasswordScreenBinding
    private lateinit var userNameField : TextInputEditText
    private lateinit var userNameLayout : TextInputLayout
    private lateinit var revealAnimation: RevealAnimation
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindForgetPassword = ActivityForgetPasswordScreenBinding.inflate(layoutInflater)
        setContentView(viewBindForgetPassword.root)
        revealAnimation = RevealAnimation(viewBindForgetPassword.root, this.intent, this)
        window.navigationBarColor = getColor(R.color.lightGreen)

        try {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    revealAnimation.unRevealActivity()
                }
            })
            init()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        init()
    }

    private fun init(){
        try{
            userNameField = viewBindForgetPassword.userNameField
            userNameLayout = viewBindForgetPassword.userNameLayout
            firebaseAuth = FirebaseAuth.getInstance()
            val sendLinkBtn = viewBindForgetPassword.sendLinkBtn
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val backBtn = viewBindForgetPassword.backBtn

            try{
                backBtn.setOnClickListener(){
                    onBackPressedDispatcher.onBackPressed()
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

            sendLinkBtn.setOnClickListener(){
                try{
                    imm.hideSoftInputFromWindow(viewBindForgetPassword.root.windowToken, 0)
                    sendPasswordResetLink()
                }catch(e: Exception){
                    e.printStackTrace()
                }
            }

            try {
                userNameField.setOnFocusChangeListener() { _, hasFocus ->
                    if (hasFocus) {
                        userNameLayout.error = null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun sendPasswordResetLink(){
        try{
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

            firebaseAuth.sendPasswordResetEmail(userNameField.text.toString().trim())
                .addOnCompleteListener(){
                    if(it.isSuccessful){
                        MaterialAlertDialogBuilder(this).setTitle("Alert!")
                            .setMessage("Resent link send to the mail id : ${userNameField.text.toString()}")
                            .setPositiveButton("Done") { _, _ ->
                                revealAnimation.unRevealActivity()
                            }.show()
                    }
                }
        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}