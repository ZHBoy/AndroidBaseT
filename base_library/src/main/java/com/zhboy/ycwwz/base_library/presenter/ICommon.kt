package com.zhboy.ycwwz.base_library.presenter

import com.zhboy.ycwwz.base_library.base.BaseActivity
import com.zhboy.ycwwz.base_library.data.AppUpdateBean
import com.zhboy.ycwwz.base_library.data.ConfigBean
import com.zhboy.ycwwz.base_library.mvp.BasePresenter

/**
 * @author zhou_hao
 * @date 2020-07-06
 * @description: 公共操作P层
 */
interface ICommon {
    interface Presenter : BasePresenter {
        /**app内强更**/
        fun appUpdate(
            activity: BaseActivity,
            resultData: BasePresenter.ResponseListener<AppUpdateBean>
        )

        /**获取配置信息**/
        fun getConfig(
            activity: BaseActivity,
            resultData: BasePresenter.ResponseListener<ConfigBean>?
        )

        /**获取渠道配置信息**/
        fun getChannalAdIsOpen(activity: BaseActivity)
    }

}