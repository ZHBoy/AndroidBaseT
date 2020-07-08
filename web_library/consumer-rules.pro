# 保持 R文件不被混淆
-keep public class com.ychd.ycwwz.web_library.R$*{
    public static final int *;
}

# 保持 实体类不被混淆
-keep class com.ychd.ycwwz.web_library.webview.** { *; }
