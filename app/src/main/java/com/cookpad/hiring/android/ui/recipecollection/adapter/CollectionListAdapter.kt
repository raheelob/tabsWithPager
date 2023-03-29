package com.cookpad.hiring.android.ui.recipecollection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.databinding.CollectionListItemBinding

class CollectionListAdapter(private val mCollectionListItemClickListener: CollectionListItemClickListener) :
    ListAdapter<CollectionEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    inner class CollectionListViewHolder(val binding: CollectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class CollectionListItemClickListener(val collectionItemClickListener: (mItem: CollectionEntity, mPosition: Int) -> Unit) {
        fun onClick(mItem: CollectionEntity, mPosition: Int) =
            collectionItemClickListener(mItem, mPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CollectionListItemBinding.inflate(layoutInflater, parent, false)
        return CollectionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, itemPosition: Int) {
        val mItem = getItem(itemPosition)
        with(holder as CollectionListViewHolder) {
            with(binding) {
                data = mItem
                mPosition = itemPosition
                collectionItemClickListener = mCollectionListItemClickListener
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
