package com.zhboy.ycwwz.user_library.data

import java.io.Serializable

/**
 * 被绑定的手机号下的卡片和抽奖次数的model
 */
class BindPhoneBean : Serializable {

    var state: Int = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {

        var lotteryNum: Int? = null//抽奖次数
        var cardNum: Int? = null//卡片数目
        var isNew: String? = null//是否是新用户  0不是新用户  1是
    }
}