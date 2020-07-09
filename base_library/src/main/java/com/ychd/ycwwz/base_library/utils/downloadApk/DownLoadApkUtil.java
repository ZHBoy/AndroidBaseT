package com.ychd.ycwwz.base_library.utils.downloadApk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ychd.ycwwz.base_library.widgets.InstallDialog;

import java.io.File;

/**
 * Created by yangminghui on 2018/6/12.
 */

public class DownLoadApkUtil {
    private  final String TAG = "DownLoadApkUtil";
    private Context context;
    public DownLoadApkUtil(Context context){
        this.context=context;
    }
    private String apkName = "action";

    public void downloadApk(final String downloadpath, final ProgressBar pbUpdateDialog,
                            LinearLayout llUpdateProgress, TextView tvInstall, LinearLayout llUpdateBtn, final FileSucessCallBack fileSucessCallBack) {
        File f;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            f = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/magkare/" + apkName + ".apk");
        }else{
            f=new File(Environment.getExternalStorageDirectory().getPath() + "/magkare/" + apkName + ".apk");
        }
        if (f.exists()) {
            f.delete();
        }
        //下载apk,并且指定放置下载文件的路径,sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //创建一个进度条对话框,用于显示下载进度
            llUpdateProgress.setVisibility(View.VISIBLE);
            tvInstall.setVisibility(View.GONE);
            llUpdateBtn.setVisibility(View.GONE);

            HttpDownLoadUtil.get().download(downloadpath, f.getParent(), apkName+".apk", new HttpDownLoadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    fileSucessCallBack.getFile(file);
                }

                @Override
                public void onDownloading(int progress) {
                    pbUpdateDialog.setProgress((int) ( progress));
                }

                @Override
                public void onDownloadFailed(Exception e) {
                    //下载异常进行相关提示操作
                    fileSucessCallBack.fail();

                }
            });

        }
    }


    /**
     * 下载完成安装apk
     *
     */
    Uri data;
    public void installApk(File file) {
        if (checkIsAndroidO()){
            Intent intent = new Intent(Intent.ACTION_VIEW);

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            //    QFileUtils.Companion.asyncMoveFileFromPriToPub(context, file.getAbsolutePath(), apkName, QFileUtils.FileType.APK, "Download", uri -> {
            //        data = uri;
            //        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
            //        intent.setDataAndType(data, "application/vnd.android.package-archive");
            //        context.startActivity(intent);
            //        return null;
            //    });
            //} else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
                data = FileProvider.getUriForFile(context, "com.ychd.ycwwz.NFileProvider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                data = Uri.fromFile(file);
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

        }else{
            //请求安装未知应用来源的权限
            new InstallDialog.Builder(context).setConfirmBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //此方法需要API>=26才能使用
                        toInstallPermissionSettingIntent();
                    }
                }
            }).build().show();
        }
       /* try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id="+"com.lixg.hcalendar"));
            context.startActivity(i);
        } catch (Exception e) {


        }*/


    }

    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    private boolean checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            return context.getPackageManager().canRequestPackageInstalls();

        } else {
            return  true;
        }
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:"+context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        context.startActivity(intent);
    }

}
