package com.zhboy.ycwwz.base_library.jpush

import android.content.Context
import cn.jpush.android.service.WakedResultReceiver

/**
 * @author zhou_hao
 * @date 2020-02-25
 * @description: 应用被拉起
 */
class MyWakedResultReceiver: WakedResultReceiver() {

    override fun onWake(context: Context?, type: Int) {
        super.onWake(context, type)
    }
}