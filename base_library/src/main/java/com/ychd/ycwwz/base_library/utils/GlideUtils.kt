package com.ychd.ycwwz.base_library.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ychd.ycwwz.base_library.R
import java.io.File

/**
 *@author : HaoBoy
 *@date : 2018/9/27
 *@description :glide图片加载，单例
 **/
class GlideUtils private constructor() {

    companion object {
        val instance: GlideUtils by lazy { GlideUtils() }
    }

    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.error_no_result)
                    .placeholder(R.drawable.error_no_result)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)
    }

    fun loadImageNoPlaceHolder(imageView: ImageView, url: String, errorPic: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(errorPic)
            )
            .load(url)
            .into(imageView)
    }

    /**
     * dialog图片，fitCenter会保持宽高比例放大图片去填充View
     */
    fun loadImageDialog(imageView: ImageView, url: String, width: Int) {

        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.error_no_result)
                    .override(width, 0)
                    .fitCenter()
            )
            .load(url)
            .into(imageView)
    }

    /**
     * dialog图片，fitCenter会保持宽高比例放大图片去填充View
     */
    fun loadImageDialog(imageView: ImageView, url: String) {
        val w = (DisplayUtil.getScreenWidth(imageView.context) - DisplayUtil.dip2px(
            imageView.context,
            40f
        ))
        //设置图片圆角角度
        val roundedCorners = RoundedCorners(10)
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        val options = RequestOptions.bitmapTransform(roundedCorners).override(w, 0)

        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .fitCenter()
            )
            .load(url)
            .apply(options)
            .into(imageView)
    }


    /**
     * 宽高比固定为320/425，fitCenter会保持宽高比例放大图片去填充View
     */
    fun loadImageShare(imageView: ImageView, url: String) {
        val w = (DisplayUtil.getScreenWidth(imageView.context) - 136) / 3
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .override(w, (w * 1.328).toInt())
                    .fitCenter()
            )
            .load(url)
            .into(imageView)
    }

    /**
     * 宽高比固定为320/425(图片默认展现方式CenterCrop，会裁剪)
     */
    fun loadImageVideo(imageView: ImageView, url: String, default: Int) {
        val w = (DisplayUtil.getScreenWidth(imageView.context) - 20) / 2
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(default)
                    .placeholder(default)
                    .override(w, (w * 1.328).toInt())
            )
            .load(url)
            .into(imageView)
    }

    /**
     * 设置图片大小
     * @param width 单位是px
     * @param height 单位是px
     */
    fun loadImageWH(imageView: ImageView, url: String, width: Int, height: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.error_no_result)
                    .placeholder(R.drawable.error_no_result)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )

            .load(url)
            .into(imageView)
    }

    /**
     * 设置图片大小
     * @param width 单位是px
     * @param height 单位是px
     */
    fun loadImageWHNoDefault(imageView: ImageView, url: String, width: Int, height: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .override(width, height)
            )
            .load(url)
            .into(imageView)
    }

    /**
     * 设置图片大小和默认图片
     * @param width 单位是px
     * @param height 单位是px
     */
    fun loadImageWH(imageView: ImageView, url: Int, width: Int, height: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .override(width, height)
            )
            .load(url)
            .into(imageView)
    }

    /**
     * 设置图片大小和默认图片
     * @param width 单位是px
     * @param height 单位是px
     */
    fun loadImageWHNoDefaultAndError(
        imageView: ImageView,
        url: String,
        width: Int,
        height: Int,
        image: Int
    ) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(image)
                    .override(width, height)
            )
            .load(url)
            .into(imageView)
    }

    /**
     * 设置图片大小
     * @param width 单位是px
     * @param height 单位是px
     */
    fun loadImageSkipMemory(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.error_no_result)
                    .skipMemoryCache(true)//不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)

    }

    /**
     * 设置图片大小
     * @param size 单位是px
     */
    fun loadImageOverride(imageView: ImageView, url: String, size: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .override(size)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)

    }

    fun loadImageNoDefault(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, url: String, default: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(default)
                    .placeholder(default)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)
    }

    fun loadImageNoDiskCache(imageView: ImageView, url: String, default: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(default)
                    .placeholder(default)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, file: File) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(RequestOptions().placeholder(R.drawable.error_no_result))
            .load(file)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, bitmap: Bitmap) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.error_no_result)
                    .error(R.drawable.error_no_result)
            )
            .load(bitmap)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    bitmap.recycle()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        if (isFirstResource)
                            imageView.setImageDrawable(resource)
                    }

                    bitmap.recycle()
                    return true
                }
            })
            .into(imageView)
    }

    /**
     * 设置圆角头像
     */
    fun loadAvatarWH(imageView: ImageView, url: String, width: Int, height: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.icon_default_header)
                    .placeholder(R.drawable.icon_default_header)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    /**
     * 设置圆角头像
     */
    fun loadAvatar(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.icon_default_header)
                    .placeholder(R.drawable.icon_default_header)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    /* * 设置圆角头像  自己设置默认图片
     */
    fun loadAvatarWithIcon(imageView: ImageView, url: String, erroricon: Int) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(erroricon)
                    .placeholder(erroricon)
            )
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    /**
     * 设置圆角头像
     */
    fun loadSnatchAvatar(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.icon_default_header)
                    .placeholder(R.drawable.icon_default_header)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不实用磁盘缓存
            )
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }


    /**
     * 圆角矩形
     * @roundingRadius 圆角度数
     */
    fun loadAvatar(imageView: ImageView, file: File, roundingRadius: Int) {
        //设置图片圆角角度
        val roundedCorners = RoundedCorners(roundingRadius)
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        val options = RequestOptions.bitmapTransform(roundedCorners).override(100, 100)
        Glide.with(imageView.context)
            .setDefaultRequestOptions(RequestOptions().placeholder(R.drawable.error_no_result))
            .load(file)
            .apply(options)
            .into(imageView)
    }

    /**
     * 圆角矩形
     * @roundingRadius 圆角度数
     */
    fun loadAvatar(
        imageView: ImageView,
        url: String,
        roundingRadius: Int, @DrawableRes placeholder: Int = R.drawable.error_no_result
    ) {
        //设置图片圆角角度
        val roundedCorners = RoundedCorners(roundingRadius)
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        val options =
            RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(), roundedCorners))
        Glide.with(imageView.context)
            .setDefaultRequestOptions(RequestOptions().placeholder(placeholder))
            .load(url)
            .apply(options)
            .into(imageView)
    }

    /**
     * 获取Bitmap
     */
    fun getBitmapByUrl(con: Context, url: String): Bitmap? {
        return try {
            Glide.with(con)
                .asBitmap()
                .load(url)
                .submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearGlideMemory(con: Context) {
        Glide.get(con).clearMemory()
    }
}
