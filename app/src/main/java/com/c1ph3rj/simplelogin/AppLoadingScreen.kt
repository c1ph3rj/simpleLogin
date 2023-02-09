package com.c1ph3rj.simplelogin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.c1ph3rj.simplelogin.databinding.ActivityAppLoadingScreenBinding
import com.google.firebase.auth.FirebaseAuth

class AppLoadingScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewBindLoadingScreen: ActivityAppLoadingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_loading_screen)

        init()
    }

    private fun init() {
        try {
            firebaseAuth = FirebaseAuth.getInstance()

            try {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        if (firebaseAuth.currentUser == null)
                            startActivity(Intent(this, LoginScreen::class.java))
                        else startActivity(Intent(this, DashboardScreen::class.java))
                    }, 6000
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}