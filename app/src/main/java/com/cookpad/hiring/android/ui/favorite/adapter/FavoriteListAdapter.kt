package com.cookpad.hiring.android.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.databinding.FavoriteListItemBinding

class FavoriteListAdapter(private val mFavoriteListItemClickListener: FavoriteListItemClickListener) :
    ListAdapter<CollectionEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    inner class FavoriteListViewHolder(val binding: FavoriteListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class FavoriteListItemClickListener(val favoriteItemClickListener: (mItem: CollectionEntity, mPosition: Int) -> Unit) {
        fun onClick(mItem: CollectionEntity, mPosition: Int) = favoriteItemClickListener(mItem, mPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FavoriteListItemBinding.inflate(layoutInflater, parent, false)
        return FavoriteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, itemPosition: Int) {
        val mItem = getItem(itemPosition)
        with(holder as FavoriteListViewHolder) {
            with(binding) {
                data = mItem
                mPosition = itemPosition
                favoriteListItemClickListener = mFavoriteListItemClickListener
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CollectionEntity>() {
            override fun areItemsTheSame(
                oldItem: CollectionEntity,
                newItem: CollectionEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CollectionEntity,
                newItem: CollectionEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
