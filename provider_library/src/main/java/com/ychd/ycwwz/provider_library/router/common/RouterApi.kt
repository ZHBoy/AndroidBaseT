package com.ychd.ycwwz.provider_library.router.common

/**
 * @author hao
 * @date 2020-02-11
 * @description: 页面跳转的路由
 */
class RouterApi {

    class MainLibrary {

        companion object {

            const val ROUTER_MAIN_URL = "/mainLibrary/main"

            const val ROUTER_MAIN_SELECT_CITY = "/mainLibrary/selectCity"

            const val ROUTER_MAIN_GUIDE_URL = "/mainLibrary/guide"

            const val ROUTER_MAIN_ONLINE_SERVICE = "/mainLibrary/service"
        }
    }

    class UserCenter {
        companion object {
            // 登录
            const val ROUTER_USER_LOGIN_URL = "/user/login"

            // 手机号登录
            const val ROUTER_USER_LOGIN_WITH_PHONE_URL = "/user/login/withPhone"

            // 获取验证码
            const val ROUTER_USER_LOGIN_VERIFY_URL = "/user/login/verify"

            // 个人中心
            const val ROUTER_USER_CENTER_URL = "/user/center"

            // 用户提现页面
            const val ROUTER_USER_REFLECT_URL = "/user/reflect"

            // 用户通知设置页面
            const val ROUTER_USER_NOTIFY_SETTING_URL = "/user/notifySetting"
        }
    }

    class WebLibrary {

        companion object {

            const val ROUTER_WEB_BASE_URL = "/web/weburl"
        }
    }

    class SplashLibrary {
        companion object{
            // SplashApplication
            const val ROUTER_SPLASH_APPLICATION = "/splash/splashApplication"
            const val ROUTER_MAIN_SPLASH_URL = "/splash/splashActivity"
        }
    }

}