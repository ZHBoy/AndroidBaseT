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

    class GameLibrary {
        companion object{
            const val ROUTER_GAME_HOME = "/game/home"
        }
    }

}