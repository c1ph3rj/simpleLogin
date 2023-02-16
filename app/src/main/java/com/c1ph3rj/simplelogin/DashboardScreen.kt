package com.c1ph3rj.simplelogin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.c1ph3rj.simplelogin.BubblePageIndicatorPkg.BubblePageIndicator
import com.c1ph3rj.simplelogin.databinding.ActivityDashboardScreenBinding
import com.c1ph3rj.simplelogin.newsApi.NEWSRequestApi
import com.c1ph3rj.simplelogin.newsApi.NEWSResponse
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.math.abs


class DashboardScreen : AppCompatActivity() {
    private lateinit var viewBindDashBoard: ActivityDashboardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindDashBoard = ActivityDashboardScreenBinding.inflate(layoutInflater)
        window.statusBarColor = Color.WHITE
        setContentView(viewBindDashBoard.root)

        init()
    }

    private fun init() {
        try {
            val userNameView = viewBindDashBoard.userNameView
            val profilePicView = viewBindDashBoard.profilePicView

            val userDetailsPref = getSharedPreferences("UserDetailsPref", MODE_PRIVATE)
            var userName = userDetailsPref.getString("displayName", "User")
            val profilePicUrl = userDetailsPref.getString("photo", "No Value")

            userName = "Hi $userName"
            userNameView.text = userName

            Glide.with(this).load(profilePicUrl).error(R.drawable.user_ic).into(profilePicView)
            try {
                getTopTechHeadLines()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try{
                getWeatherReport()
            }catch(e: Exception){
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getWeatherReport(){
        try{
            Thread {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://newsapi.org/v2/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiCall = retrofit.create<NEWSRequestApi>()

                    apiCall.getTopTechNews("technology", "en", getString(R.string.NEWS_API_KEY))
                        .enqueue(object : Callback<NEWSResponse?> {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onResponse(
                                call: Call<NEWSResponse?>,
                                response: Response<NEWSResponse?>
                            ) {
                                println(response)
                                if (response.isSuccessful) {
                                    val responseFromApi: NEWSResponse? = response.body()
                                    if (response.code() == 200) {
                                        try {
                                            if (responseFromApi != null) {
                                                val briefHeadline = Dialog(this@DashboardScreen)
                                                val inflater = getSystemService(
                                                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                                val popupView = inflater.inflate(R.layout.brief_headlines_layout, null)
                                                briefHeadline.setContentView(popupView)
                                                briefHeadline.window?.setLayout(
                                                    WindowManager.LayoutParams.MATCH_PARENT,
                                                    WindowManager.LayoutParams.MATCH_PARENT
                                                )
                                                briefHeadline.window?.setBackgroundDrawable(
                                                    ColorDrawable(Color.TRANSPARENT)
                                                )
                                                val briefHeadlinesAdapter = TopHeadlineWithBriefViewAdapter(this@DashboardScreen, responseFromApi.articles)
                                                val briefHeadlinesView = popupView.findViewById<ViewPager2>(R.id.briefHeadlinesViewPager)
                                                briefHeadlinesView.adapter = briefHeadlinesAdapter

                                                briefHeadlinesView.clipToPadding = false
                                                briefHeadlinesView.clipChildren = false
                                                briefHeadlinesView.offscreenPageLimit = 3
                                                briefHeadlinesView.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


                                                // Code for carousel view animation in viewpager2.
                                                val compositePageTransformer = CompositePageTransformer()
                                                compositePageTransformer.addTransformer(
                                                    MarginPageTransformer(10)
                                                )
                                                compositePageTransformer.addTransformer { page: View, position: Float ->
                                                    val `val` = 1 - abs(position)
                                                    page.scaleY = 0.75f + `val` * 0.10f
                                                }

                                                briefHeadlinesView.setPageTransformer(compositePageTransformer)
                                                val pageIndicator = popupView.findViewById<BubblePageIndicator>(R.id.dotIndicator)
                                                pageIndicator.setViewPager(briefHeadlinesView)

                                                val backLayout = popupView.findViewById<LinearLayout>(R.id.backBtn)
                                                backLayout.setOnClickListener(){
                                                    briefHeadline.cancel()
                                                }

                                                briefHeadline.create()
                                                val techHeadlinesView = viewBindDashBoard.techHeadLinesView
                                                techHeadlinesView.setOnClickListener(){
                                                    briefHeadline.show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }

                                }
                            }

                            override fun onFailure(call: Call<NEWSResponse?>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun getTopTechHeadLines() {
        try {
            Thread {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://newsapi.org/v2/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiCall = retrofit.create<NEWSRequestApi>()

                    apiCall.getTopTechNews("technology", "en", getString(R.string.NEWS_API_KEY))
                        .enqueue(object : Callback<NEWSResponse?> {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onResponse(
                                call: Call<NEWSResponse?>,
                                response: Response<NEWSResponse?>
                            ) {
                                println(response)
                                if (response.isSuccessful) {
                                    val responseFromApi: NEWSResponse? = response.body()
                                    if (response.code() == 200) {
                                        try {
                                            if (responseFromApi != null) {
                                                val briefHeadline = Dialog(this@DashboardScreen)
                                                val inflater = getSystemService(
                                                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                                val popupView = inflater.inflate(R.layout.brief_headlines_layout, null)
                                                briefHeadline.setContentView(popupView)
                                                briefHeadline.window?.setLayout(
                                                    WindowManager.LayoutParams.MATCH_PARENT,
                                                    WindowManager.LayoutParams.MATCH_PARENT
                                                )
                                                briefHeadline.window?.setBackgroundDrawable(
                                                    ColorDrawable(Color.TRANSPARENT)
                                                )
                                                val briefHeadlinesAdapter = TopHeadlineWithBriefViewAdapter(this@DashboardScreen, responseFromApi.articles)
                                                val briefHeadlinesView = popupView.findViewById<ViewPager2>(R.id.briefHeadlinesViewPager)
                                                briefHeadlinesView.adapter = briefHeadlinesAdapter

                                                briefHeadlinesView.clipToPadding = false
                                                briefHeadlinesView.clipChildren = false
                                                briefHeadlinesView.offscreenPageLimit = 3
                                                briefHeadlinesView.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


                                                // Code for carousel view animation in viewpager2.
                                                val compositePageTransformer = CompositePageTransformer()
                                                compositePageTransformer.addTransformer(
                                                    MarginPageTransformer(10)
                                                )
                                                compositePageTransformer.addTransformer { page: View, position: Float ->
                                                    val `val` = 1 - abs(position)
                                                    page.scaleY = 0.75f + `val` * 0.10f
                                                }

                                                briefHeadlinesView.setPageTransformer(compositePageTransformer)
                                                val pageIndicator = popupView.findViewById<BubblePageIndicator>(R.id.dotIndicator)
                                                pageIndicator.setViewPager(briefHeadlinesView)

                                                val backLayout = popupView.findViewById<LinearLayout>(R.id.backBtn)
                                                backLayout.setOnClickListener(){
                                                    briefHeadline.cancel()
                                                }

                                                briefHeadline.create()
                                                val techHeadlinesView = viewBindDashBoard.techHeadLinesView
                                                techHeadlinesView.setOnClickListener(){
                                                    briefHeadline.show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }

                                }
                            }

                            override fun onFailure(call: Call<NEWSResponse?>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}