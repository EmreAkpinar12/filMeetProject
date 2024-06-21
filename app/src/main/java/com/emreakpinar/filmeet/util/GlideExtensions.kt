package com.emreakpinar.filmeet.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.emreakpinar.filmeet.R

    fun ImageView.loadCircleImage(path: String) {


    Glide.with(this.context).load(Constans.Image_Base_URL + path)
        .apply(centerCropTransform().error(R.drawable.eroor_icon).circleCrop()).into(this)
    }
    fun ImageView.loadImage(path: String) {


    Glide.with(this.context).load(Constans.Image_Base_URL + path)
        .apply(centerCropTransform().error(R.drawable.eroor_icon)).into(this)
    }