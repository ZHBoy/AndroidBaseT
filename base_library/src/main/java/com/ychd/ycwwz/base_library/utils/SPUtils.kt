package com.ychd.ycwwz.base_library.utils

import android.content.Context
import android.content.SharedPreferences
import com.ychd.ycwwz.base_library.BaseApplication.Companion.appContext
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by JackHuang on 2016/9/14.
 */

object SPUtils {

    /**
     * 保存在手机里面的名字
     */
    val FILE_NAME = "yc_ycwwz_shared_data"

    /**
     * 切换用户不被清空的数据
     */
    val BROKER_SHARED_DATA_NOT_CLEAR = "yc_ycwwz_shared_data_not_clear"
    /**
     * 返回所有的键值对
     *
     * @return
     */
    val all: Map<String, *>
        get() { //只生成一个get方法
            val sharedPreferences =
                appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            return sharedPreferences.all
        }

    /**
     * 保存数据的方法，拿到数据保存数据的基本类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param obj
     */
    fun setObject(key: String, obj: Any) {
        val sharedPreferences = appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when (obj) {
            is String -> editor.putString(key, obj)
            is Int -> editor.putInt(key, obj)
            is Boolean -> editor.putBoolean(key, obj)
            is Float -> editor.putFloat(key, obj)
            is Long -> editor.putLong(key, obj)
            else -> editor.putString(key, obj.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 保存数据的方法，拿到数据保存数据的基本类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param obj
     */
    fun setObject(filename: String, key: String, obj: Any) {
        val sharedPreferences = appContext!!.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (obj is String) {
            editor.putString(key, obj)
        } else if (obj is Int) {
            TLog.i("------>$key", "$obj")
            editor.putInt(key, obj)
        } else if (obj is Boolean) {
            editor.putBoolean(key, obj)
        } else if (obj is Float) {
            editor.putFloat(key, obj)
        } else if (obj is Long) {
            editor.putLong(key, obj)
        } else {
            editor.putString(key, obj.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 获取保存数据的方法，我们根据默认值的到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           键的值
     * @param defaultObject 默认值
     * @return
     */

    fun getObjectForKey(key: String, defaultObject: Any): Any? {
        val sharedPreferences = appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        return if (defaultObject is String) {
            sharedPreferences.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            sharedPreferences.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            sharedPreferences.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            sharedPreferences.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            sharedPreferences.getLong(key, defaultObject)
        } else {
            sharedPreferences.getString(key, null)
        }

    }

    fun getObjectForKey(filename: String, key: String, defaultObject: Any): Any? {
        val sharedPreferences = appContext!!.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        return if (defaultObject is String) {
            sharedPreferences.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            sharedPreferences.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            sharedPreferences.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            sharedPreferences.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            sharedPreferences.getLong(key, defaultObject)
        } else {
            sharedPreferences.getString(key, null)
        }

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    fun remove(key: String) {
        val sharedPreferences = appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有的数据
     */
    fun clear() {
        val sharedPreferences = appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否存在
     *
     * @param key
     * @return
     */
    operator fun contains(key: String): Boolean {
        val sharedPreferences = appContext!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        return sharedPreferences.contains(key)
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod =
            findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }

}