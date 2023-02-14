package com.c1ph3rj.simplelogin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.c1ph3rj.simplelogin.databinding.ActivityDashboardScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs


class DashboardScreen : AppCompatActivity() {
    private final val ApiKey: String = "17e09070dd3c40efa6739f5e23d57aa3"
    private lateinit var viewBindDashBoard: ActivityDashboardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindDashBoard = ActivityDashboardScreenBinding.inflate(layoutInflater)
        setContentView(viewBindDashBoard.root)

        init()
    }

    private fun init() {
        try {
            val topHeadlinesView = viewBindDashBoard.topHeadlinesView

            Thread {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://newsapi.org/v2/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiCall = retrofit.create<NEWSRequestApi>()

                    apiCall.getTopTechNews("technology", "en", ApiKey)
                        .enqueue(object : Callback<NEWSResponse?> {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onResponse(
                                call: Call<NEWSResponse?>,
                                response: Response<NEWSResponse?>
                            ) {
                                println(response)
                                if (response.isSuccessful) {
                                    val responseFromApi: NEWSResponse? = response.body()
                                    if (responseFromApi != null) {
                                        val headlinesViewAdapter = topHeadlinesViewAdapter(
                                            this@DashboardScreen,
                                            responseFromApi.articles
                                        )

                                        topHeadlinesView.adapter = headlinesViewAdapter

                                        val pageIndicator = viewBindDashBoard.dotIndicator

                                        pageIndicator.setViewPager(topHeadlinesView)


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