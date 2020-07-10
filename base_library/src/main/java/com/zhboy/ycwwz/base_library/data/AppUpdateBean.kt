package com.zhboy.ycwwz.base_library.data

/**
 * app版本更新的实体类
 */
class AppUpdateBean {

    /**
     * state : 1
     * message : success
     * data : {"id":1,"versionCode":"4","versionName":"2.0.1","isForce":"1","apkUrl":"https://imtt.dd.qq.com/16891/9F668E4C224C171F9764FB723DEA5FAF.apk?fsname=com.lixg.hcalendar_2.0.1_4.apk&amp;csr=1bbd","descAppList":["996","9点睡觉","9点起床","工作6小时"]}
     */

    var state: Int = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {
        /**
         * id : 1
         * versionCode : 4
         * versionName : 2.0.1
         * isForce : 1
         * apkUrl : https://imtt.dd.qq.com/16891/9F668E4C224C171F9764FB723DEA5FAF.apk?fsname=com.lixg.hcalendar_2.0.1_4.apk&amp;csr=1bbd
         * descAppList : ["996","9点睡觉","9点起床","工作6小时"]
         */

        var id: Int = 0
        var versionCode: String? = null
        var versionName: String? = null
        //是否强制更新 0非强制  1强制
        var isForce: String? = null
        var apkUrl: String? = null

        var buttonControlType = 0 //0 应用内版本更新 1 浏览器打开
        var updateDesc: String? = "发现新版本了，快去更新吧"//版本描述
        var buttonText: String? = "立即更新"//按钮文案

        var lastForceVersionCode: String? = null//上次强更的版本号
        var descAppList: List<String>? = null
    }
}
