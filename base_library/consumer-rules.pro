 # 保持 R文件不被混淆
-keep public class com.ychd.ycwwz.base_library.R$*{
    public static final int *;
}

# 保持 实体类不被混淆
-keep class com.ychd.ycwwz.base_library.data.** { *; }
-keep class com.ychd.ycwwz.base_library.constants.** { *; }
-keep class com.ychd.ycwwz.base_library.service.body.** { *; }

# 保持 自定义控件不被混淆
#-keep class com.ychd.ycwwz.base_library.widgets.** { *; }

-keep class com.ychd.ycwwz.base_library.wxapi.** { *; }
-keep class com.ychd.ycwwz.base_library.jpush.** { *; }
-keep class com.ychd.ycwwz.base_library.event.** { *; }
-keep class com.ychd.ycwwz.base_library.network.** { *; }

# --------- 微信 ---------
-keep class com.tencent.mm.opensdk.** {
    *;
}
-keep class com.tencent.wxop.** {
    *;
}
-keep class com.tencent.mm.sdk.** {
    *;
}
# --------- 微信 ---------

# --------- 极光推送---------
-dontoptimize
-dontpreverify

#----高德定位-----

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#极光push的混淆
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
# 讯飞语音混淆
-keep class com.iflytek.**{*;}
-keepattributes Signature

#oaid SDK
-keep class com.bun.miitmdid.core.** {*;}