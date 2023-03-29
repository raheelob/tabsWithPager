package com.cookpad.hiring.android.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cookpad.hiring.android.ui.favorite.fragment.FavoriteFragment
import com.cookpad.hiring.android.ui.recipecollection.fragment.CollectionListFragment

class MainViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        CollectionListFragment(), FavoriteFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}