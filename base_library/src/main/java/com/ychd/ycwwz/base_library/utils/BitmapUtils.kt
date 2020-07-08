package com.ychd.ycwwz.base_library.utils

import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import java.io.*

class BitmapUtils {
    companion object {
        val instance: BitmapUtils by lazy { BitmapUtils() }
    }


    /**
     * bitmap 转指定大小的 byte[]
     */
    fun bitmapToByteArray4AssignSize(
        bmp: Bitmap,
        assignSize: Long = 120 * 1024L,
        needRecycle: Boolean = true
    ): ByteArray {
        var i: Int
        var j: Int
        i = bmp.width
        j = bmp.height

        val localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565)
        val localCanvas = Canvas(localBitmap)

        while (true) {
            localCanvas.drawBitmap(bmp, Rect(0, 0, i, j), Rect(0, 0, i, j), null)
            if (needRecycle)
                bmp.recycle()

            val localByteArrayOutputStream = ByteArrayOutputStream()
            var options = 100
            localBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                localByteArrayOutputStream
            )
            while (localByteArrayOutputStream.toByteArray().size > assignSize) {
                options -= 5
                if (options <= 5) options = 5
                localByteArrayOutputStream.reset()
                localBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    options,
                    localByteArrayOutputStream
                )
                if (options == 5) break
            }

            localBitmap.recycle()
            val arrayOfByte = localByteArrayOutputStream.toByteArray()
            try {
                localByteArrayOutputStream.close()
                return arrayOfByte
            } catch (e: Exception) {
                e.printStackTrace()
            }

            i = bmp.width
            j = bmp.height
        }
    }

    /**
     * bitmap 转 byte[]
     */
    fun bitmapToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }

        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    //改变bitmap尺寸的方法
    fun zoomImg(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }

    private val MAX_DECODE_PICTURE_SIZE = 1920 * 1440
    fun extractThumbNail(path: String?, height: Int, width: Int, crop: Boolean): Bitmap? {
        var options: BitmapFactory.Options? = BitmapFactory.Options()

        try {
            options!!.inJustDecodeBounds = true
            var tmp: Bitmap? = BitmapFactory.decodeFile(path, options)
            if (tmp != null) {
                tmp.recycle()
                tmp = null
            }

            val beY = options.outHeight * 1.0 / height
            val beX = options.outWidth * 1.0 / width
            options.inSampleSize =
                (if (crop) if (beY > beX) beX else beY else if (beY < beX) beX else beY).toInt()
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++
            }

            var newHeight = height
            var newWidth = width
            if (crop) {
                if (beY > beX) {
                    newHeight =
                        (newWidth.toDouble() * 1.0 * options.outHeight.toDouble() / options.outWidth).toInt()
                } else {
                    newWidth =
                        (newHeight.toDouble() * 1.0 * options.outWidth.toDouble() / options.outHeight).toInt()
                }
            } else {
                if (beY < beX) {
                    newHeight =
                        (newWidth.toDouble() * 1.0 * options.outHeight.toDouble() / options.outWidth).toInt()
                } else {
                    newWidth =
                        (newHeight.toDouble() * 1.0 * options.outWidth.toDouble() / options.outHeight).toInt()
                }
            }

            options.inJustDecodeBounds = false

            var bm: Bitmap? = BitmapFactory.decodeFile(path, options)
            if (bm == null) {
                return null
            }

            val scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true)
            if (scale != null) {
                bm.recycle()
                bm = scale
            }

            if (crop) {
                val cropped = Bitmap.createBitmap(
                    bm,
                    bm.width - width shr 1,
                    bm.height - height shr 1,
                    width,
                    height
                )
                    ?: return bm

                bm.recycle()
                bm = cropped
            }
            return bm

        } catch (e: OutOfMemoryError) {
            options = null
        }

        return null
    }

    /**
     * 获取指定大小的 bitmap
     */
    fun getTargetSquareBitmap(
        targetWidth: Int, @DrawableRes res: Int,
        resources: Resources
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, res, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = targetWidth
        return BitmapFactory.decodeResource(resources, res, options)
    }


    /**
     * 合并 2 个 bitmap
     */
    fun combineBitmap(first: Bitmap, second: Bitmap): Bitmap {
        val width = Math.max(first.width, second.width)
        val height = first.height + second.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        // 设置canvas画布背景
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, 0f, first.height.toFloat(), null)
        return result
    }

    /**
     * 合并 2 个 bitmap
     */
    fun combineBitmap(vararg bitmaps: Bitmap): Bitmap {
        val width = bitmaps.maxBy {
            it.width
        }?.width ?: 1

        val height = bitmaps.sumBy {
            it.height
        }

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        var offsetHeught: Float = 0F
        bitmaps.forEach {
            canvas.drawBitmap(it, 0f, offsetHeught, null)
            offsetHeught += it.height
        }
        return result
    }

    /**
     * 保存图片到存储盘
     *
     * @param bitmap  存储的bitmap对象
     * @param savePath 存储路径
     * @param format   图片格式 (默认 PNG 格式)
     * @param quality  图片质量 0 - 100,如<=0,则默认为100
     */
    fun saveBitmap(
        bitmap: Bitmap, savePath: String,
        format: CompressFormat = CompressFormat.PNG, @IntRange(from = 0) quality: Int = 100
    ): Boolean {
        val desFile: File = File(savePath)
        return if (FileUtil.createDir(desFile.parent)) {
            try {
                val fos = FileOutputStream(savePath)
                val bos = BufferedOutputStream(fos)
                bitmap.compress(format, quality, bos)
                bos.flush()
                bos.close()
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        } else {
            false
        }
    }

    fun compressImage(
        pBitmap: Bitmap,
        targetSize: Int,
        format: CompressFormat = CompressFormat.JPEG
    ): Bitmap {
        var quality = 100
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            pBitmap.compress(format, quality, baos)
            while (baos.toByteArray().size > targetSize) {
                quality -= 5
                if (quality <= 0) {
                    break
                }
                baos.reset()
                pBitmap.compress(format, quality, baos)
            }
            val bytes = baos.toByteArray()
            val isBm = ByteArrayInputStream(bytes)
            return BitmapFactory.decodeStream(isBm)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                baos?.close()
            } catch (e: IOException) {
            }
        }
        return pBitmap
    }

    fun clipViewCornerByDp(view: View, pixel: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.clipToOutline = true
            view.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, pixel)
                }
            }
        }
    }

}