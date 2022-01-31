package com.exam.sample.utils.extention

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.exam.sample.App
import com.exam.sample.R
import com.exam.sample.utils.Const
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException

fun View.setCellSize(width: Float, height: Float): ViewGroup.LayoutParams? {
    val rlp = layoutParams
    val ratio: Float = height / width
    rlp.height = ((Const.SCREEN_WIDTH_HALF) * ratio).toInt()
    return rlp
}

fun View.setImageSize(width: Int, height: Int): ViewGroup.LayoutParams? {
    val rlp = layoutParams
    rlp.width = width
    rlp.height = height
    return rlp
}

fun ImageView.setRainBowBackgroundColorByPosition(position: Int) {
    if (position == 0)
        setBackgroundResource(Const.COLORS_RAINBOW[0])
    else
        setBackgroundResource(Const.COLORS_RAINBOW[position % Const.COLORS_RAINBOW.size])
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun getStringColorHex(colorResId: Int): String = App.getApplication().resources.getString(colorResId)

fun View.touchEffect(context: Context) {
    val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.touch_larger_scale)
    startAnimation(fadeAnim)
}

fun ImageView.glideLoadForUrl
(
       url: String,
       onLoadFailed:() -> Unit,
       onResourceReady:() -> Unit
) {
    Glide.with(this)
        .load(url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Drawable, model: Any, target: com.bumptech.glide.request.target.Target<Drawable>,
                dataSource: DataSource, isFirstResource: Boolean
            ): Boolean {
                onResourceReady()
                return false
            }
        }).into(this)
}

fun Context.imageViewBorder(
    borderColor:Int = Color.BLACK,
    borderWidthInDp: Int = 5
): ShapeDrawable {
    // convert dp to equivalent pixels value for border
    val borderWidthInPixels = borderWidthInDp.dpToPixels(this)

    val shapeDrawable = ShapeDrawable(RectShape())

    // specify the border properties
    shapeDrawable.paint.apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = borderWidthInPixels
        isAntiAlias = true
        flags = Paint.ANTI_ALIAS_FLAG
    }

    // set padding for drawable
    val padding = (borderWidthInPixels * 2).toInt()
    shapeDrawable.setPadding(padding,padding,padding,padding)

    // return image view border as shape drawable
    return shapeDrawable
}


// extension function to convert dp to equivalent pixels
fun Int.dpToPixels(context: Context):Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
)


// extension function to get bitmap from assets
fun Context.assetsToBitmap(fileName: String): Bitmap?{
    return try {
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) { null }
}

fun ViewGroup.inflate(@LayoutRes resource: Int): View {
    return LayoutInflater.from(context).inflate(resource, this, false)
}

fun View.onClick(onClick: () -> Unit) {
    setOnClickListener({ onClick() })
}
