package com.zhboy.ycwwz.base_library.jpush

import com.zhboy.ycwwz.base_library.BaseApplication
import com.zhboy.ycwwz.base_library.constants.AccessManager
import java.util.LinkedHashSet

/**
 * @author wtl
 * @date 2020-03-08.
 * description
 */
class JPushUtil {
    companion object{
        fun setTag(tag:String,sequence: Int) {
            //重新设置推送tag  设置为false  设置成功 会在TagAliasOperatorHelper的onTagOperatorResult方法中设置为true
            AccessManager.setJiPushTagIsSuccess(false)
            //设置推送的tag
            val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
            tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
            val tagSet = LinkedHashSet<String>()
            tagSet.add(tag)
            tagAliasBean.tags = tagSet
            //false 设置tag
            tagAliasBean.isAliasAction = false
            TagAliasOperatorHelper.getInstance()
                .handleAction(BaseApplication.appContext, sequence, tagAliasBean)
        }
    }
}