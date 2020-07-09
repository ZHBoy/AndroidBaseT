package com.ychd.commonlibrary.data.login

import java.io.Serializable

class LoginBean : Serializable {


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
         * id : 212
         * phone : 1254
         * imei : 54a5sd4asd
         * userId : asda5s4d5asd
         * nickname : 嘻嘻哈哈
         * userType : 1
         * headImageUrl : ssdasdasd.com/sdsd.jpg
         */

        var id: String? = null
        var phone: String? = null
        var imei: String? = null
        var userId: String? = null
        var nickname: String? = null
        var userType: String? = null //用户类型1是微信  2是微信手机号绑定后的
        var headImageUrl: String? = null
        var openId: String? = null  //给前端用
        var userStatus: String? = null  //是否是老用户
        var createTime: String? = null   //账号创建时间
        var forceLogin39: String? = null  //强制登录的版本
        var guidanceProgress: String? = null  //当前新人引导进度

        var isNewUser: String? = null  //用于判断是否领了送二十次抽奖机会 1是没领 0是领了

        var awardIntegral: String? = null  //礼品库，当是新用户的是送的具体财运币的值

        var newGuidance: String? = null  //0 老用户 1新用户 10完成了十连抽  20赠送了150次抽奖 10000完成了新人引导

    }
}