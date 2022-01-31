package com.exam.sample.common

import android.graphics.Color
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.exam.sample.utils.Const
import java.util.*

@BindingAdapter("photo")
fun setImage(view: ImageView, url: String?) {
    val index = Random().nextInt(Const.COLORS_RAINBOW.size - 1)
    Glide.with(view)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(Const.COLORS_RAINBOW[index])
        )
        .transition(DrawableTransitionOptions.withCrossFade(factory))
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}


fun setAppCompatImageView(view: AppCompatImageView, url: String?) {
    val index = Random().nextInt(Const.COLORS_RAINBOW.size - 1)
    Glide.with(view)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(Const.COLORS_RAINBOW[index])
        )
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}


private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

fun ImageView.clear() = Glide.with(context).clear(this)

//fun ImageView.load(url: String, corner: Float = 0f, scaleType: Transformation<Bitmap> = CenterInside()) {
//    Glide.with(this)
//        .load(url)
//        .transition(DrawableTransitionOptions.withCrossFade(factory))
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .apply {
//            if (corner > 0) transforms(scaleType, RoundedCorners(corner.fromDpToPx()))
//        }
//        .into(this)
//}

@BindingAdapter("textEffectTwinkle")
fun textEffectTwinkle(view: TextView, text:String) {
    val index = Random().nextInt(Const.COLORS_RAINBOW.size - 1)
    val colorString = Const.COLORS_RAINBOW_STRING[index]

    view.text = text
    view.setTextColor(Color.parseColor(colorString))
    AlphaAnimation(0.0f, 1.0f).apply {
        duration = 350 //You can manage the blinking time with this parameter
        startOffset = 20
        repeatMode = Animation.REVERSE
        repeatCount = Animation.INFINITE

        view.startAnimation(this);
    }
}