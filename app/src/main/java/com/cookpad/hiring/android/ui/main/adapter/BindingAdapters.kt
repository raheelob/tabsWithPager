package com.cookpad.hiring.android.ui.main.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cookpad.hiring.android.R

object BindingAdapters {
    @BindingAdapter("imagePath")
    @JvmStatic
    fun imagePath(appCompatImageView: AppCompatImageView, previewImageUrls: List<String>) {
        Glide.with(appCompatImageView.context)
            .load(previewImageUrls.firstOrNull())
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .into(appCompatImageView)
    }

    @BindingAdapter("setRecipe")
    @JvmStatic
    fun setRecipe(appCompatTextView: AppCompatTextView, count: Int?) {
        count?.let {
            appCompatTextView.text = buildString {
                append(
                    appCompatTextView.context.getString(
                        R.string.collection_recipe_count,
                        count.toString()
                    )
                )
            }
        }
    }

    @BindingAdapter("isFavorite")
    @JvmStatic
    fun isFavorite(appCompatImageView: AppCompatImageView, isFavorite:Boolean) {
        val drawableId: Int = if(isFavorite){
            R.drawable.ic_baseline_favorite_24
        }else{
            R.drawable.ic_baseline_favorite_border_24
        }
        appCompatImageView.setImageResource(drawableId)
    }

}