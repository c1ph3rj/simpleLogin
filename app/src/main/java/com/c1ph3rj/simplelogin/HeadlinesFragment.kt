package com.c1ph3rj.simplelogin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.c1ph3rj.simplelogin.databinding.FragmentHeadlinesBinding


class HeadlinesFragment(private var articles: Articles, private var listOfArticles ) : Fragment() {

    private lateinit var viewBindHeadlines : FragmentHeadlinesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBindHeadlines = FragmentHeadlinesBinding.inflate(layoutInflater)
        return viewBindHeadlines.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createHeadlines()
    }

    private fun createHeadlines(){
        try{
            val headlineImg = viewBindHeadlines.headlinesImgView
            val headlineTitle = viewBindHeadlines.headlinesTitleView
            val headlineLayout = viewBindHeadlines.headlineLayout
            val briefHeadline = Dialog(requireContext())
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.brief_headlines_layout, null)
            briefHeadline.setContentView(popupView)
            briefHeadline.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            briefHeadline.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )



            headlineTitle.text = articles.title

            Glide.with(this).load(articles.urlToImage).error(R.drawable.not_found_img).into(headlineImg)

            headlineLayout.setOnClickListener(){
                briefHeadline.show()
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

}