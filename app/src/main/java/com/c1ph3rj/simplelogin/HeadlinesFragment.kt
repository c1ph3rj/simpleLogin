package com.c1ph3rj.simplelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.c1ph3rj.simplelogin.databinding.FragmentHeadlinesBinding


class HeadlinesFragment(var articles: Articles) : Fragment() {

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
        val headlineImg = viewBindHeadlines.headlinesImgView
        val headlineTitle = viewBindHeadlines.headlinesTitleView

        Glide.with(this).load(articles.urlToImage).into(headlineImg)

        headlineTitle.text = articles.title
    }

}