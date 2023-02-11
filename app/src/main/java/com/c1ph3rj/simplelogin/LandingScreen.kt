package com.c1ph3rj.simplelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.c1ph3rj.simplelogin.databinding.ActivityLandingScreenBinding


class LandingScreen : AppCompatActivity() {
    private lateinit var viewBindLanding : ActivityLandingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindLanding = ActivityLandingScreenBinding.inflate(layoutInflater)
        setContentView(viewBindLanding.root)
        window.statusBarColor = getColor(R.color.teal_700)
        window.navigationBarColor = getColor(R.color.teal_700)

        init()
    }

    private fun init(){
        val signInBtn = viewBindLanding.signIn
        val signUpBtn = viewBindLanding.signUp

        signInBtn.setOnClickListener(){
            startRevealActivity(it, Intent(this, LoginScreen::class.java))
        }

        signUpBtn.setOnClickListener(){
            startRevealActivity(it, Intent(this, RegisterScreen::class.java))
        }
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