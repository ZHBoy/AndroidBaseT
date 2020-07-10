package com.zhboy.commonlibrary.data.login

import java.io.Serializable

class LoginXyBindBean : Serializable {

    /**
     * state : 1
     * message : kk
     * data : {"id":212,"phone":"1254","imei":"54a5sd4asd","userId":"asda5s4d5asd","nickname":"嘻嘻哈哈","userType":1,"headImageUrl":"ssdasdasd.com/sdsd.jpg"}
     */

    var state: Int = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {
        /**
         * isBind : true
         * localismUserinfo : {"ycId":"oplsjdhjahsdja","localismOpenid":"ajshdjashd","localismToken":"sdkuajksdhj","localismSign":"sdahjshdja","mappingTime":"2019-12-15"}
         */

        var isBind: Boolean = false//是否绑定
        var localismUserinfo: LocalismUserinfoBean? = null

        class LocalismUserinfoBean {
            /**
             * ycId : oplsjdhjahsdja
             * localismOpenid : ajshdjashd
             * localismToken : sdkuajksdhj
             * localismSign : sdahjshdja
             * mappingTime : 2019-12-15
             */
            var id: String? = null//id
            var ycId: String? = null//基础框架用户id
            var localismOpenid: String? = null//
            var localismToken: String? = null//
            var localismSign: String? = null//登录sign
            var score: String? = null//分数
            var mappingTime: String? = null//时间
            var isGuarder: Boolean? = false//是否是守护者
            var localismUid: String? = null//
        }
    }
}