package com.gdalamin.bcs_pro.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.gdalamin.bcs_pro.R

object Animations {
    var lastPosition = -1
    fun setAnimationLeftIn(ctx: Context?, viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val slideIn = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left)
            viewToAnimate.animation = slideIn
            lastPosition = position
        }
    }

    fun setAnimationFadeIn(ctx: Context?, viewToAnimate: View, position: Int) {
        viewToAnimate.clearAnimation()
        if (position > lastPosition) {
            val slideIn = AnimationUtils.loadAnimation(ctx, android.R.anim.fade_in)
            viewToAnimate.animation = slideIn
            lastPosition = position
        }
    }


}