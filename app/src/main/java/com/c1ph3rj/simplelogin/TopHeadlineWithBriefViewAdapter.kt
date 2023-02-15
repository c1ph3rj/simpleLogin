package com.c1ph3rj.simplelogin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TopHeadlineWithBriefViewAdapter(fragmentActivity: FragmentActivity, var listOfArticles: ArrayList<Articles>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return listOfArticles.size
    }

    override fun createFragment(position: Int): Fragment {
        return HeadlinesFragment(listOfArticles[position])
    }

}