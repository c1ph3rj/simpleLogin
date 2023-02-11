package com.c1ph3rj.simplelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DashboardScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_screen)

        startActivity(Intent(this@DashboardScreen, LandingScreen::class.java))
    }
}