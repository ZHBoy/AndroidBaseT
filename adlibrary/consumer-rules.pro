# 保持 R文件不被混淆
-keep public class com.ychd.weather.adlibrary.R$*{
    public static final int *;
}

#穿山甲广告
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

#百度广告
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }