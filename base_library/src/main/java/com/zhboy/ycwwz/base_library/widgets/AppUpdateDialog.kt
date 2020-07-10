package com.zhboy.ycwwz.base_library.widgets

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.zhboy.ycwwz.base_library.BuildConfig
import com.zhboy.ycwwz.base_library.R
import com.zhboy.ycwwz.base_library.data.AppUpdateBean
import com.zhboy.ycwwz.base_library.utils.DisplayUtil
import com.zhboy.ycwwz.base_library.utils.StringUtils
import com.zhboy.ycwwz.base_library.utils.ToastUtil
import com.zhboy.ycwwz.base_library.utils.downloadApk.DownLoadApkUtil
import com.zhboy.ycwwz.base_library.utils.downloadApk.FileSucessCallBack
import kotlinx.android.synthetic.main.apdate_dialog.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

/**
 * @author : HaoBoy
 * @date : 2019/5/9
 * @description :版本更新
 */
class AppUpdateDialog(private val mContext: Context, private val appUpdateBean: AppUpdateBean) :
    Dialog(mContext, R.style.Theme_AppCompat_Dialog) {

    lateinit var downLoadApkUtil: DownLoadApkUtil
    lateinit var mFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.apdate_dialog)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val window = this.window
        window!!.setGravity(Gravity.CENTER)
        val params = window.attributes
        params.width = DisplayUtil.getScreenWidth(mContext) / 10 * 9
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
        // 隐藏标题栏
        window.setBackgroundDrawableResource(R.color.transparent)
        llUpdateProgress.visibility = View.GONE
        tvInstall.visibility = View.GONE
        llUpdateBtn.visibility = View.VISIBLE
        //如果为null就写默认为非强制.0非强制 1强制
        val isForce = appUpdateBean.data?.isForce ?: "0"

        //上次强更的版本号
        val isMustUpdate = appUpdateBean.data?.lastForceVersionCode ?: "0"

        //如果是强更或者当前版本号小于上次强更的版本号
        if (isForce == "1" || BuildConfig.VERSION_CODE < isMustUpdate.toInt()) {
            tvNextTime.visibility = View.GONE
            setUpdateButtonClick()
        } else {
            tvNextTime.visibility = View.VISIBLE
            setUpdateButtonClick()
        }

        tvUpdate.text = appUpdateBean.data?.buttonText
        tvInstall.setOnClickListener {
            if (mFile != null) {
                downLoadApkUtil.installApk(mFile)
            } else {
                llUpdateProgress.visibility = View.GONE
                tvInstall.visibility = View.GONE
                llUpdateBtn.visibility = View.VISIBLE
            }
        }

        //下次再说
        tvNextTime.setOnClickListener {
            dismiss()
        }

        //内容
        appUpdateBean.data?.updateDesc?.let { updateDesc ->
            StringUtils.getClickableHtml(
                mContext, tvUpdateInfo, updateDesc
            )
            tvUpdateInfo.isClickable = true
            tvUpdateInfo.movementMethod = LinkMovementMethod.getInstance()
        }

    }

    private fun setUpdateButtonClick() {
        tvUpdate.setOnClickListener {
            if (appUpdateBean.data?.apkUrl.isNullOrEmpty().not()) {
                //应用内更新
                if (appUpdateBean.data?.buttonControlType == 0) {
                    updateWithAppIn()
                } else {//浏览器更新
                    val uri =
                        Uri.parse(appUpdateBean.data?.apkUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    mContext.startActivity(intent)
                }
                return@setOnClickListener
            }

            val uri =
                Uri.parse("https://youcaiapp.oss-cn-beijing.aliyuncs.com/yc_youcai_aligned_signed.apk")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mContext.startActivity(intent)
        }
    }

    /**
     * 应用内更新
     */
    private fun updateWithAppIn() {
        llUpdateProgress.visibility = View.VISIBLE
        llUpdateBtn.visibility = View.GONE
        tvInstall.visibility = View.GONE
        downLoadApkUtil = DownLoadApkUtil(context)
        downLoadApkUtil.downloadApk(
            appUpdateBean.data?.apkUrl,
            pbUpdateDialog,
            llUpdateProgress,
            tvInstall,
            llUpdateBtn,
            object :
                FileSucessCallBack {
                override fun fail() {
                    doAsync {
                        uiThread {
                            tvUpdate.isEnabled = true
                            llUpdateProgress.visibility = View.GONE
                            tvInstall.visibility = View.GONE
                            llUpdateBtn.visibility = View.VISIBLE
                            ToastUtil.showShortToast("下载失败，用浏览器下载试试")
                            //(如果下载失败)浏览器更新
                            val uri =
                                Uri.parse(appUpdateBean.data?.apkUrl)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mContext.startActivity(intent)
                        }
                    }
                }

                override fun getFile(file: File?) {
                    mFile = file!!
                    doAsync {
                        uiThread {
                            llUpdateProgress.visibility = View.GONE
                            tvInstall.visibility = View.VISIBLE
                            llUpdateBtn.visibility = View.GONE
                            downLoadApkUtil.installApk(file)
                        }
                    }
                }
            })
        tvUpdate.isEnabled = false
    }
}
