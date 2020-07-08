package com.ychd.ycwwz.base_library.event.main

/**
 * 首页 fragment 切换
 * @param fragmentIndex fragment 的下标
 */
class MainActivitySwitchEvent(val fragmentIndex: Int) {

    companion object {
        const val WEATHER_FRAGMENT_INDEX = 0
        // 福利
        const val WELFARE_FRAGMENT_INDEX = 1
        // 活动
        const val ACTIVITY_FRAGMENT_INDEX = 2
        // 我的
        const val MINE_FRAGMENT_INDEX = 3
    }
}