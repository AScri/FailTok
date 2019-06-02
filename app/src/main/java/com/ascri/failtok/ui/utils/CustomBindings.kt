package com.ascri.failtok.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ascri.failtok.R

object CustomBindings {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImageView(imageView: ImageView, imageThumbs: String) {
        if (imageThumbs.isNotEmpty()) {
            imageView.clipToOutline = true
            // If we don't do this, you'll see the old image appear briefly
            // before it's replaced with the current image
            if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageThumbs) {
                imageView.setImageBitmap(null)
                imageView.setTag(R.id.image_url, imageThumbs)
                GlideApp.with(imageView)
                        .load(imageThumbs)
                        .centerInside()
                        .into(imageView)
            }
        } else {
            imageView.setTag(R.id.image_url, null)
            imageView.setImageBitmap(null)
        }
    }
}