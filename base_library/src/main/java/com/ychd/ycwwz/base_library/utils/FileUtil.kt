package com.ychd.ycwwz.base_library.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import com.ychd.ycwwz.base_library.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*

/**
 * 作者：changzhiyuan
 * 描述：
 */
class FileUtil {
    companion object {
        // 获取相册目录 /storage/emulated/0/DCIM/Camera/
        fun getCameraPath(): String {
            val cameraPath = Environment.getExternalStorageDirectory()
                .toString() + File.separator + "DCIM" + File.separator + "Camera" + File.separator
            createDir(cameraPath)
            return cameraPath
        }

        /**
         * 向相册数据库中插入一条数据
         *
         * @param title    标题
         * @param mimeType 文件类型
         * @param path     文件存储路径
         * @param duration 视频时间 毫秒
         */
        fun insertMediaDataToDatabase(
            context: Context,
            title: String,
            mimeType: String,
            path: String,
            duration: Long
        ) {
            try {
                val values = ContentValues()
                values.put(MediaStore.Video.Media.TITLE, title)
                values.put(MediaStore.Video.Media.MIME_TYPE, mimeType)
                values.put(MediaStore.Video.Media.DATA, path)
                values.put(MediaStore.Video.Media.DURATION, duration)
                context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 扫描指定文件 添加到数据库 以便在相册中显示
         * image/png
         * video/mp4
         * @param context
         * @param paths     文件存储路径
         * @param mimeTypes 文件类型
         */
        fun scanFile(
            context: Context,
            paths: Array<String>,
            mimeTypes: Array<String>,
            callback: MediaScannerConnection.OnScanCompletedListener? = null
        ):Boolean {
            try {
                MediaScannerConnection.scanFile(context, paths, mimeTypes, callback)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        /**
         * 创建文件夹
         */
        fun createDir(path: String): Boolean {
            try {
                val file: File = File(path)
                if (file.exists().not()) {
                    return file.mkdirs()
                } else {
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        /**
         * 删除文件
         */
        fun deleteFileBackground(path: String) {
            object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg params: Unit) {
                    deleteFile(path)
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

        /**
         * 删除文件
         */
        fun deleteFile(path: String): Boolean {
            val file = File(path)
            if (file.exists()) {
                file.delete()
                return true
            } else {
                return false
            }
        }

        /**
         * 删除该文件夹下所有的文件
         */
        fun deleteFiles(file: File) {
            if (file.isDirectory) {
                val files = file.listFiles()
                for (i in files.indices) {
                    val f = files[i]
                    deleteFiles(f)
                }
                // file.delete();//如要保留文件夹，只删除文件，请注释这行
            } else if (file.exists()) {
                file.delete()
            }
        }

        fun copyFileWithStream(
            outputStream: OutputStream?,
            inputStream: InputStream?
        ): Boolean {
            if (outputStream == null || inputStream == null) {
                return false
            }
            var read = 0
            return try {
                val buffer = ByteArray(1024)
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                    outputStream.flush()
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                try {
                    outputStream.close()
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun copyAsset(
            oriPath: String,
            desPath: String,
            beforeBlock: () -> Unit = {},
            succBlock: () -> Unit = {},
            failBlock: () -> Unit = {}
        ) {
            val outFile = File(desPath)
            if (createDir(outFile.parentFile.absolutePath)) {
                if (outFile.exists()) {
                    if (outFile.length() > 10) { //表示已经写入一次
                        succBlock.invoke()
                        return
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    beforeBlock.invoke()
                    var succ:Boolean = false
                    withContext(Dispatchers.IO){
                        val inputStream: InputStream = BaseApplication.appContext!!.assets.open(oriPath)
                        val fos = FileOutputStream(outFile)
                        try {
                            val buffer = ByteArray(1024)
                            var byteCount: Int = 0
                            while (inputStream.read(buffer).also { byteCount = it } != -1) {
                                fos.write(buffer, 0, byteCount)
                            }
                            fos.flush()
                            succ = true
                        } catch (e: IOException) {
                            succ = false
                            e.printStackTrace()
                        }finally {
                            inputStream.close()
                            fos.close()
                        }
                    }
                    if (succ){
                        succBlock.invoke()
                    } else {
                        failBlock.invoke()
                    }
                }
            } else {
                failBlock.invoke()
            }
        }

        fun isFile(file: File?): Boolean {
            return file != null && file.exists() && file.isFile
        }

        /**
         * Create a directory if it doesn't exist, otherwise do nothing.
         *
         * @param dirPath The path of directory.
         * @return `true`: exists or creates successfully<br></br>`false`: otherwise
         */
        fun createOrExistsDir(dirPath: String): Boolean {
            return createOrExistsDir(getFileByPath(dirPath))
        }

        fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        fun getFileByPath(filePath: String): File? {
            return if (isSpace(filePath)) null else File(filePath)
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) {
                return true
            }
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }

        fun getNowString(format: java.text.DateFormat): String {
            return millis2String(System.currentTimeMillis(), format)
        }

        /**
         * Milliseconds to the formatted time string.
         *
         * @param millis The milliseconds.
         * @param format The format.
         * @return the formatted time string
         */
        fun millis2String(millis: Long, format: java.text.DateFormat): String {
            return format.format(Date(millis))
        }
    }
}





