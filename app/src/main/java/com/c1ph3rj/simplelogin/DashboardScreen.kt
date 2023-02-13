package com.c1ph3rj.simplelogin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.c1ph3rj.simplelogin.databinding.ActivityDashboardScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class DashboardScreen : AppCompatActivity() {
    private final val ApiKey : String = "17e09070dd3c40efa6739f5e23d57aa3"
    private lateinit var viewBindDashBoard : ActivityDashboardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindDashBoard = ActivityDashboardScreenBinding.inflate(layoutInflater)
        setContentView(viewBindDashBoard.root)

        init()
    }

    private fun init(){
        try{
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
                            override fun onResponse(
                                call: Call<NEWSResponse?>,
                                response: Response<NEWSResponse?>
                            ) {
                                println(response)
                                if (response.isSuccessful) {
                                    val responseFromApi: NEWSResponse? = response.body()
                                    if (responseFromApi != null) {
                                        val headlinesViewAdapter = topHeadlinesViewAdapter(this@DashboardScreen, responseFromApi.articles)
                                        val tabDotsForHeadlinesView = viewBindDashBoard.headlinesViewIndicator
                                        topHeadlinesView.adapter = headlinesViewAdapter
                                        topHeadlinesView.setClipToPadding(false)
                                        topHeadlinesView.setClipChildren(false)
                                        topHeadlinesView.setOffscreenPageLimit(3)
                                        topHeadlinesView.getChildAt(0)
                                            .setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)

                                        // Code for carousel view animation in viewpager2.

                                        // Code for carousel view animation in viewpager2.
                                        val compositePageTransformer = CompositePageTransformer()
                                        compositePageTransformer.addTransformer(
                                            MarginPageTransformer(40)
                                        )
                                        compositePageTransformer.addTransformer { page: View, position: Float ->
                                            val `val` = 1 - Math.abs(position)
                                            page.scaleY = 0.95f + `val` * 0.15f
                                        }

                                        topHeadlinesView.setPageTransformer(
                                            compositePageTransformer
                                        )
                                        TabLayoutMediator(
                                            tabDotsForHeadlinesView, topHeadlinesView
                                        ) { tab: TabLayout.Tab?, position: Int -> }.attach()

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
}