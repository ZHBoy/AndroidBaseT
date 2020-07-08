package com.ychd.ycwwz.base_library.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.*
import java.lang.IllegalArgumentException

/**
 * 用于Android10私有文件夹处理
 */
class QFileUtils {
    companion object {
        /**
         * AndroidQ以上保存图片到公共目录
         *
         * @param imageName 图片名称
         * @param imageType 图片类型
         * @param relativePath 文件存储目标路径的相对路径
         */
        //fun getMediaStoreInsertUri(
        //    context: Context,
        //    imageName: String,
        //    imageType: String,
        //    relativePath: String = "DCIM"
        //): Uri? {
        //    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        //        return null
        //    }
        //    if (relativePath.isBlank()) {
        //        return null
        //    }
        //    var insertUri: Uri? = null
        //    val resolver: ContentResolver = context.contentResolver
        //    //设置文件参数到ContentValues中
        //    val values = ContentValues()
        //    //设置文件名
        //    values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
        //    //设置文件描述，这里以文件名代替
        //    values.put(MediaStore.Images.Media.DESCRIPTION, imageName)
        //    //设置文件类型为image/*
        //    values.put(MediaStore.Images.Media.MIME_TYPE, "image/$imageType")
        //    //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
        //    //故该方法只可在Android10的手机上执行
        //    values.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
        //    //EXTERNAL_CONTENT_URI代表外部存储器
        //    val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //    //insertUri表示文件保存的uri路径
        //    insertUri = resolver.insert(external, values)
        //    return insertUri
        //}

        /**
         * AndroidQ以上保存图片到公共目录
         *
         * @param fileName 文件名称
         * @param fileType 文件类型
         * @param relativePath 文件存储目标路径的相对路径
         */
        fun getMediaStoreInsertUri(
            context: Context,
            fileName: String,
            fileType: FileType,
            relativePath: String = "DCIM"
        ): Uri? {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                return null
            }
            if (relativePath.isBlank()) {
                return null
            }
            val resolver: ContentResolver = context.contentResolver
            //设置文件参数到ContentValues中
            val values = ContentValues()
            return when (fileType) {
                FileType.JPG,
                FileType.PNG -> {
                    values.apply {
                        //设置文件名
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName.plus(fileType.extension))
                        //设置文件描述，这里以文件名代替
                        put(MediaStore.Images.Media.DESCRIPTION, fileName)
                        //设置文件类型为image/*
                        put(MediaStore.Images.Media.MIME_TYPE, fileType.mimeType)
                        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
                        //故该方法只可在Android10的手机上执行
                        put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                        //EXTERNAL_CONTENT_URI代表外部存储器
                    }
                    //返回文件保存的uri路径
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                }
                FileType.MP4 -> {
                    values.apply {
                        //设置文件名
                        put(MediaStore.Video.Media.DISPLAY_NAME, fileName.plus(fileType.extension))
                        //设置文件描述，这里以文件名代替
                        put(MediaStore.Video.Media.DESCRIPTION, fileName)
                        //设置文件类型为image/*
                        put(MediaStore.Video.Media.MIME_TYPE, fileType.mimeType)
                        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
                        //故该方法只可在Android10的手机上执行
                        put(MediaStore.Video.Media.RELATIVE_PATH, relativePath)
                        //EXTERNAL_CONTENT_URI代表外部存储器
                    }
                    //返回文件保存的uri路径
                    resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                }
                FileType.APK -> {
                    values.apply {
                        //设置文件名
                        put(MediaStore.Downloads.DISPLAY_NAME, fileName.plus(fileType.extension))
                        //设置文件类型为image/*
                        put(MediaStore.Downloads.MIME_TYPE, fileType.mimeType)
                        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
                        //故该方法只可在Android10的手机上执行
                        put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
                        //EXTERNAL_CONTENT_URI代表外部存储器
                    }
                    //返回文件保存的uri路径
                    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                }
                else -> {
                    throw (IllegalArgumentException("不支持的文件类型"))
                    null
                }
            }
        }

        /**
         * 把文件从私有目录复制到公有目录
         * @param sourceFilePath 原文件目录
         * @param insertUri 公有目录
         */
        fun copyFileFromPrivateToPublic(
            context: Context,
            sourceFilePath: String,
            insertUri: Uri?
        ): Boolean {
            if (insertUri == null) {
                return false
            }
            val resolver: ContentResolver = context.contentResolver
            var inputStream: InputStream? = null //输入流
            var outputStream: OutputStream? = null //输出流
            return try {
                outputStream = resolver.openOutputStream(insertUri)
                if (outputStream == null) {
                    return false
                }
                val sourceFile = File(sourceFilePath)
                if (sourceFile.exists()) { // 文件存在时
                    inputStream = FileInputStream(sourceFile) // 读入原文件
                    //输入流读取文件，输出流写入指定目录
                    return FileUtil.copyFileWithStream(outputStream, inputStream)
                }
                false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                try {
                    inputStream?.close()
                    outputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    enum class FileType(val extension: String, val mimeType: String) {
        JPG(".jpg", "image/jpg"),
        PNG(".png", "image/png"),
        MP4(".mp4", "video/mp4"),
        APK(".apk", "application/vnd.android.package-archive"),
    }
}