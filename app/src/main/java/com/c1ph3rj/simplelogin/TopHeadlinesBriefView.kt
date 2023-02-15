package com.c1ph3rj.simplelogin

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.c1ph3rj.simplelogin.databinding.FragmentTopHeadlinesBriefViewBinding


class TopHeadlinesBriefView : Fragment() {
    private lateinit var viewBindBriefView: FragmentTopHeadlinesBriefViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBindBriefView = FragmentTopHeadlinesBriefViewBinding.inflate(layoutInflater)
        return viewBindBriefView.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        try{
            val briefHeadlineTitle = viewBindBriefView.briefHeadlinesTitle
            val briefHeadlineImg = viewBindBriefView.briefHeadlinesImg
            val briefHeadlineContent = viewBindBriefView.briefHeadlinesContent
            val linkToTheArticle = viewBindBriefView.linkToFullArticle

            linkToTheArticle.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            linkToTheArticle.setOnClickListener() {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(articles.url)))
            }
            briefHeadlineTitle.text = articles.title

            Glide.with(this).load(articles.urlToImage).error(R.drawable.not_found_img).into(briefHeadlineImg)

            if(articles.description?.isEmpty() == true){
                briefHeadlineContent.visibility = View.GONE
            }else{
                briefHeadlineContent.text = articles.description
            }

        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}