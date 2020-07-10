package com.zhboy.ycwwz.base_library.event.main

/**
 * 首页 fragment 切换
 * @param fragmentIndex fragment 的下标
 */
class MainActivitySwitchEvent(val fragmentIndex: Int) {

    companion object {
        //首页
        const val HOME_FRAGMENT_INDEX = 0
        // 福利
        const val WELFARE_FRAGMENT_INDEX = 1
        // 任务
        const val TASL_FRAGMENT_INDEX = 2

    }
}