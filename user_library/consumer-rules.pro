# 保持 R文件不被混淆
-keep public class com.ychd.ycwwz.user_library.R$*{
    public static final int *;
}

# 保持 实体类不被混淆
-keep class com.ychd.ycwwz.user_library.data.** { *; }

# 保持 自定义控件不被混淆
-keep class [com.ychd.ycwwz.user_library.widgets].** { *; }