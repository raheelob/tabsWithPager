package com.cookpad.hiring.android.ui.main.view

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.cookpad.hiring.android.databinding.FragmentMainBinding
import com.cookpad.hiring.android.ui.base.BaseFragment
import com.cookpad.hiring.android.ui.main.adapter.MainViewpagerAdapter
import com.cookpad.hiring.android.ui.main.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(
    FragmentMainBinding::inflate
) {
    private lateinit var mainViewpagerAdapter: MainViewpagerAdapter

    override val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        mainViewpagerAdapter = MainViewpagerAdapter(this)
        binding.pager.adapter = mainViewpagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Discover"
                1 -> "Favorite"
                else -> ""
            }
        }.attach()
    }

    override fun observeViewModel(viewModel: MainViewModel) {}
}