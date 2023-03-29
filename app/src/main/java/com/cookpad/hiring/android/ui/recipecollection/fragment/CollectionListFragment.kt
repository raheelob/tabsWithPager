package com.cookpad.hiring.android.ui.recipecollection.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.databinding.FragmentCollectionListBinding
import com.cookpad.hiring.android.ui.base.BaseFragment
import com.cookpad.hiring.android.ui.recipecollection.adapter.CollectionListAdapter
import com.cookpad.hiring.android.ui.recipecollection.event.CollectionRecipeDataEvent
import com.cookpad.hiring.android.ui.main.viewmodel.MainViewModel
import com.cookpad.hiring.android.utils.ProgressDialog
import com.cookpad.hiring.android.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionListFragment : BaseFragment<FragmentCollectionListBinding, MainViewModel>(
    FragmentCollectionListBinding::inflate
) {

    private lateinit var collectionListAdapter: CollectionListAdapter

    override val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentCollectionListBinding, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        initSwipeToRefresh()
        mProgressDialog = ProgressDialog(requireActivity())
    }

    override fun observeViewModel(viewModel: MainViewModel) {
        binding.viewModel = viewModel
        viewModel.result.observe(this){

        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.discoverCollectionTasksEvent.collect { eventState ->
                    when (eventState) {

                        is CollectionRecipeDataEvent.Loading -> {
                            showLoading()
                        }

                        is CollectionRecipeDataEvent.Error -> {
                            hideLoading()
                            showToast(requireContext(), eventState.errorData.error.toString())
                            manageUIState(viewNumber = 1, list = null)

                        }

                        CollectionRecipeDataEvent.RemoteErrorByNetwork -> {
                            hideLoading()
                            showToast(requireContext(), "fetch from local data source")
                            viewModel.fetchFromLocal(true)
                        }

                        is CollectionRecipeDataEvent.RecipeCollectionList -> {
                            hideLoading()
                            if (eventState.recipeCollectionList.isEmpty())
                                manageUIState(viewNumber = 1, list = null)
                            else
                                inflateViewWithCollectionList(eventState)
                        }

                        is CollectionRecipeDataEvent.UpdateCollectionList ->{
                            collectionListAdapter.submitList(eventState.recipeCollectionList)
                        }

                        is CollectionRecipeDataEvent.FavoriteRemovedFromTheList ->{
                            val currentList = collectionListAdapter.currentList.toMutableList()
                            val index = currentList.indexOfFirst { it.id == eventState.collectionEntity.id }
                            if(index >= 0){
                                val recipe = viewModel.consolidateRecipeEntity(recipe = currentList[index])
                                currentList[index] = recipe
                                collectionListAdapter.submitList(currentList)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun inflateViewWithCollectionList(eventState: CollectionRecipeDataEvent.RecipeCollectionList) {
        if (eventState.recipeCollectionList.isEmpty()) {
            manageUIState(viewNumber = 0, list = null)
        } else {
            manageUIState(viewNumber = 2, list = eventState.recipeCollectionList)
        }
    }

    private fun manageUIState(viewNumber: Int, list: List<CollectionEntity>?) {
        viewFlipNumber(viewNumber = viewNumber)
        collectionListAdapter.submitList(list)
    }

    private fun viewFlipNumber(viewNumber: Int) {binding.viewFlipper.displayedChild = viewNumber}

    private fun initSwipeToRefresh() = binding.collectionListLayout.swipeToRefresh.apply {
        setOnRefreshListener {
            isRefreshing = false
            viewModel.refresh()
        }
    }

    private fun setUpRecyclerView() {
        binding.collectionListLayout.collectionList.apply {
            collectionListAdapter = CollectionListAdapter(onCollectionListItemClicked())
            adapter = collectionListAdapter
            itemAnimator = null
        }

        Color.values().forEach { item ->
println(item)
        }

    }

    private fun onCollectionListItemClicked() =
        CollectionListAdapter.CollectionListItemClickListener { mItem, mPosition ->
            viewModel.whenFavoriteClick(collectionListAdapter.currentList.toMutableList(), mPosition, mItem)
        }


}

enum class Color(color:String){
    Red("Red"),
    Green("Green"),
    Blue("Blue")
}