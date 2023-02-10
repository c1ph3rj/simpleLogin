package com.c1ph3rj.simplelogin

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.c1ph3rj.simplelogin.databinding.ActivityLoginScreenBinding


class LoginScreen : AppCompatActivity() {
    private lateinit var viewBindLogin : ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindLogin = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(viewBindLogin.root)
        init()
    }

    private fun init(){
        try{
            val userNameField = viewBindLogin.userNameField
            val userNameLayout = viewBindLogin.userNameLayout
            val passwordLayout = viewBindLogin.passwordLayout
            val passwordField = viewBindLogin.passwordField
            val forgetPassword = viewBindLogin.forgetPassword
            val signInBtn = viewBindLogin.signInBtn
            val signInWithGoogleBtn = viewBindLogin.signInWithGoogleBtn
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}