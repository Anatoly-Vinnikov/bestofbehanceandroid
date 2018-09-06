package com.inostudio.avinnikov.bestofbehanceandroid.utilities

import android.databinding.BindingAdapter
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.ablanco.zoomy.Zoomy
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.inostudio.avinnikov.bestofbehanceandroid.R


object ImageBindingAdapter {
    var isGrid: Boolean = true
    //var module = MyGlideModule()

    @JvmStatic
    @BindingAdapter("postImage")
    fun setPostImage(view: ImageView, url: String) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions().transforms(FitCenter(), RoundedCorners(20)))
                .into(view)
    }

    @JvmStatic
    @BindingAdapter("profileImage")
    fun setProfileImage(view: ImageView, url: String) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(view)
    }

    @JvmStatic
    @BindingAdapter("moduleImage")
    fun setModuleImage(view: ImageView, url: String) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions().transforms(FitCenter())
                        .placeholder(ColorDrawable(ContextCompat.getColor(view.context, R.color.nav_active))))
                .into(view)
        val builder = Zoomy.Builder(view.context as AppCompatActivity).target(view)
        builder.register()
    }

    @JvmStatic
    @BindingAdapter("text")
    fun setTextSize(view: TextView, url: String) {
        if (isGrid) {
            view.textSize = 10f
        } else {
            view.textSize = 14f
        }
    }

    @JvmStatic
    @BindingAdapter("icon")
    fun setIconSize(view: ImageView, url: String) {
        if (isGrid) {
            when (view.id) {
                R.id.eye -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_views_small))
                R.id.thumbs -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_appreciation_small))
                R.id.comments -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_comments_small))
            }
        } else {
            when (view.id) {
                R.id.eye -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_views))
                R.id.thumbs -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_appreciation))
                R.id.comments -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_comments))
            }
        }
    }
}

/*@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(Log.VERBOSE)
    }
}*/