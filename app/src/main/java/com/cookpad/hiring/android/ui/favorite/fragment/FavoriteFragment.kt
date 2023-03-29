package com.cookpad.hiring.android.ui.favorite.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.databinding.FragmentFavoriteListBinding
import com.cookpad.hiring.android.ui.base.BaseFragment
import com.cookpad.hiring.android.ui.favorite.adapter.FavoriteListAdapter
import com.cookpad.hiring.android.ui.favorite.event.FavoriteRecipeDataEvent
import com.cookpad.hiring.android.ui.main.viewmodel.MainViewModel
import com.cookpad.hiring.android.utils.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteListBinding, MainViewModel>(
    FragmentFavoriteListBinding::inflate
) {
    private lateinit var favoriteListAdapter: FavoriteListAdapter

    override val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentFavoriteListBinding, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        initSwipeToRefresh()
        mProgressDialog = ProgressDialog(requireActivity())
    }

    override fun onPause() {
        super.onPause()
        manageUIState(viewNumber = 0, list = null)
    }

    override fun observeViewModel(viewModel: MainViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                delay(200)
                viewModel.getFavoriteList()
                viewModel.favoriteTasksEvent.collect { eventState ->
                    when (eventState) {
                        is FavoriteRecipeDataEvent.FavoriteList -> {
                            hideLoading()
                            if (eventState.favoriteList.isEmpty())
                                manageUIState(viewNumber = 1, list = null)
                            else
                                inflateViewWithFavoriteList(eventState)
                        }

                        is FavoriteRecipeDataEvent.UpdateFavoriteList -> {
                            if (eventState.updateFavoriteList.isEmpty())
                                manageUIState(viewNumber = 1, list = null)
                            else
                                favoriteListAdapter.submitList(eventState.updateFavoriteList)
                        }
                    }
                }
            }
        }
    }

    private fun inflateViewWithFavoriteList(eventState: FavoriteRecipeDataEvent.FavoriteList) {
        if (eventState.favoriteList.isEmpty()) {
            manageUIState(viewNumber = 1, list = null)
        } else {
            manageUIState(viewNumber = 2, list = eventState.favoriteList)
        }
    }

    private fun manageUIState(viewNumber: Int, list: List<CollectionEntity>?) {
        binding.viewFlipper.displayedChild = viewNumber
        favoriteListAdapter.submitList(list)
    }

    private fun setUpRecyclerView() {
        binding.favoriteListLayout.collectionList.apply {
            favoriteListAdapter = FavoriteListAdapter(onCollectionListItemClicked())
            adapter = favoriteListAdapter
        }
    }

    private fun onCollectionListItemClicked() =
        FavoriteListAdapter.FavoriteListItemClickListener { mItem, mPosition ->
            viewModel.removeFavoriteFromFavoriteList(
                favoriteListAdapter.currentList.toMutableList(),
                mItem
            )
        }

    private fun initSwipeToRefresh() = binding.favoriteListLayout.swipeToRefresh.apply {
        setOnRefreshListener {
            isRefreshing = false
            viewModel.getFavoriteList()
        }
    }
}
