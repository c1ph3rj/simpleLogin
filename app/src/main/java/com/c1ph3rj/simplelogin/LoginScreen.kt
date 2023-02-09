package com.c1ph3rj.simplelogin

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.c1ph3rj.simplelogin.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {
    private lateinit var viewBindLogin : ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        init()
    }

    private fun init(){
        try{
            val loginPicView = viewBindLogin.loginPicView
            val listOfLoginPic = listOf(R.drawable.login_pic_one, R.drawable.login_pic_two, R.drawable.login_pic_three, R.drawable.login_pic_four)

            loginPicView.setImageDrawable(AppCompatResources.getDrawable(this, listOfLoginPic.random()))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}