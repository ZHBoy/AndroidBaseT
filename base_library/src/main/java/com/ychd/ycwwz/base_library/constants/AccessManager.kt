package com.ychd.ycwwz.base_library.constants

import com.ychd.ycwwz.base_library.utils.SPUtils

/**
 *@author : HaoBoy
 *@date : 2019/4/9
 *@description :用户信息管理类
 **/

class AccessManager private constructor() {

    companion object {

        /**
         * 清除当前用户信息
         */
        fun clearUserInfo() {
            SPUtils.clear()
        }

        /**
        设置tag成功与否的状态 设置成功时设置为true 重新设置时设置为false
         */
        fun setJiPushTagIsSuccess(tag: Boolean) {
            return SPUtils.setObject(SpDef.JIPUSH_SETTAG_ISSUCCESS, tag)
        }


        /**
        设置tag成功与否的状态 设置成功时设置为true 重新设置时设置为false
         */
        fun getJiPushTagIsSuccess(): Boolean {
            return SPUtils.getObjectForKey(SpDef.JIPUSH_SETTAG_ISSUCCESS, false) as Boolean
        }

        /**
         * 保存用户authorization
         */
        fun setUserAuthorization(authorization: String) {
            SPUtils.setObject(SpDef.USER_AUTHORIZATION, authorization)
        }

        /**
         * 获取用户authorization
         */
        fun getUserAuthorization(): String {
            return SPUtils.getObjectForKey(SpDef.USER_AUTHORIZATION, "") as String
        }

        /**
         * 获取用户的UID
         */
        fun getUserUid(): String {
            return SPUtils.getObjectForKey(SpDef.USER_UID, "") as String
        }

    }
}