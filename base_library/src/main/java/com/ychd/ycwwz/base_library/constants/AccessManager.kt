package com.ychd.ycwwz.base_library.constants

import com.ychd.ycwwz.base_library.mmkv.MMKVUtils

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
            MMKVUtils.clearAll()
        }

        /**
        设置tag成功与否的状态 设置成功时设置为true 重新设置时设置为false
         */
        fun setJiPushTagIsSuccess(tag: Boolean) {
            return MMKVUtils.encode(SpDef.JIPUSH_SETTAG_ISSUCCESS, tag)
        }

        /**
        设置tag成功与否的状态 设置成功时设置为true 重新设置时设置为false
         */
        fun getJiPushTagIsSuccess(): Boolean {
            return MMKVUtils.decodeBoolean(SpDef.JIPUSH_SETTAG_ISSUCCESS) as Boolean
        }

        /**
         * 保存用户authorization
         */
        fun setUserAuthorization(authorization: String) {
            MMKVUtils.encode(SpDef.USER_AUTHORIZATION, authorization)
        }

        /**
         * 获取用户authorization
         */
        fun getUserAuthorization(): String {
            return MMKVUtils.decodeString(SpDef.USER_AUTHORIZATION) as String
        }

        /**
         * 获取用户的UID
         */
        fun getUserUid(): String {
            return MMKVUtils.decodeString(SpDef.USER_UID) as String
        }

    }
}