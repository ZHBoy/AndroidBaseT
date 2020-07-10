# 保持 R文件不被混淆
-keep public class com.zhboy.ycwwz.splash_library.R$*{
    public static final int *;
}

# 保持 实体类不被混淆
-keep class com.zhboy.ycwwz.splash_library.data.** { *; }
-keep class com.zhboy.ycwwz.splash_library.service.body.** { *; }

# 保持 自定义控件不被混淆
-keep class com.zhboy.ycwwz.splash_library.widgets.** { *; }