package com.zhboy.ycwwz.base_library.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;

/**
 * @author zhou_hao
 * @date 2020-07-06
 * @description: OAID相关
 */
public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater _listener;

    public MiitHelper(AppIdsUpdater callback) {
        _listener = callback;

    }

    public void getDeviceIds(Context cxt) throws Exception {
        JLibrary.InitEntry(cxt);
        int nres = CallFromReflect(cxt);
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备

        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错

        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商

        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程

        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错

        }
    }


    /*
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     *
     * */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }


    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if (_supplier == null) {
            return;
        }
        String oaid = _supplier.getOAID();
        /*String vaid=_supplier.getVAID();
        String aaid=_supplier.getAAID();
        StringBuilder builder=new StringBuilder();
        builder.append("support: ").append(isSupport?"true":"false").append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext=builder.toString()*/
        ;
        if (_listener != null) {
            _listener.OnIdsAvalid(oaid);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull String ids);
    }

}
