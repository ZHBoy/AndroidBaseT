package com.zhboy.ycwwz.base_library.utils.downloadApk;

import java.io.File;

public interface FileSucessCallBack {
    void getFile(File file);
    void fail();
}
