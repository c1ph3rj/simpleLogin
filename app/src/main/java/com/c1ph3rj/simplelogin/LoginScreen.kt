package com.c1ph3rj.simplelogin

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
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
            val listOfLoginPic = listOf(R.drawable.login_pic_one, R.drawable.login_pic_two, R.drawable.login_pic_three, R.drawable.login_pic_four)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}