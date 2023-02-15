package com.c1ph3rj.simplelogin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.c1ph3rj.simplelogin.databinding.ActivityDashboardScreenBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class DashboardScreen : AppCompatActivity() {
    private lateinit var viewBindDashBoard: ActivityDashboardScreenBinding
    private lateinit var topHeadlinesView: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindDashBoard = ActivityDashboardScreenBinding.inflate(layoutInflater)
        setContentView(viewBindDashBoard.root)

        init()
    }

    private fun init() {
        try {
            topHeadlinesView = viewBindDashBoard.topHeadlinesView

            try {
                getTopTechHeadLines()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
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
                                                val headlinesViewAdapter = TopHeadlinesViewAdapter(
                                                    this@DashboardScreen,
                                                    responseFromApi.articles
                                                )
                                                topHeadlinesView.adapter = headlinesViewAdapter
                                                val pageIndicator = viewBindDashBoard.dotIndicator
                                                pageIndicator.setViewPager(topHeadlinesView)

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