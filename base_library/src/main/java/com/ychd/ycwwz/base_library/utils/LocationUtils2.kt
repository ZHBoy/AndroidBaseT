package com.ychd.ycwwz.base_library.utils

import android.annotation.SuppressLint
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener


import java.lang.ref.WeakReference


/**
 * 定位实现类
 */
class LocationUtils2(context: Context) : AMapLocationListener {

    private val mWeakReference: WeakReference<Context> = WeakReference(context)

    val context: Context?
        get() = if (mWeakReference.get() != null) {
            mWeakReference.get()
        } else null

    // 关于定位的参数
    private var mLocClient: AMapLocationClient? = null
    private var mOption: AMapLocationClientOption? = null

    private var callback: ResultCallback? = null

    /**
     * @return DefaultLocationClientOption  setScanSpan=0是只定位一次
     */
    private val defaultLocationClientOption: AMapLocationClientOption
        get() {
            if (mOption == null) {
                mOption = AMapLocationClientOption()
                mOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                //获取一次定位结果：
                //该方法默认为false。
                mOption!!.isOnceLocation = true
            }
            return mOption!!
        }

    fun startLocation(callback: ResultCallback) {
        this.callback = callback
        location()
    }

    @SuppressLint("CheckResult")
    fun location() {
        if (mLocClient == null) {
            // 定位初始化
            mLocClient = AMapLocationClient(context)
            mLocClient!!.setLocationOption(defaultLocationClientOption)
            mLocClient!!.setLocationListener(this)
        }
        mLocClient!!.startLocation()
    }

    /**
     * 再不需要定位的时候调用（必须）
     */
    fun stopLocation() {
        if (mLocClient != null) {
            mLocClient!!.unRegisterLocationListener(this)
            mLocClient!!.stopLocation()
            mLocClient = null
        }
    }

    override fun onLocationChanged(location: AMapLocation?) {

        if (null != location) {

//            val sb = StringBuffer()
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.errorCode == 0) {
                callback!!.success(location)

                /* amapLocation.getLocationType()//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude()//获取纬度
                amapLocation.getLongitude()//获取经度
                amapLocation.getAccuracy()//获取精度信息
                amapLocation.getAddress()//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry()//国家信息
                amapLocation.getProvince()//省信息
                amapLocation.getCity()//城市信息
                amapLocation.getDistrict()//城区信息
                amapLocation.getStreet()//街道信息
                amapLocation.getStreetNum()//街道门牌号信息
                amapLocation.getCityCode()//城市编码
                amapLocation.getAdCode()//地区编码
                amapLocation.getAoiName()//获取当前定位点的AOI信息
                amapLocation.getBuildingId()//获取当前室内定位的建筑物Id
                amapLocation.getFloor()//获取当前室内定位的楼层
                amapLocation.getGpsAccuracyStatus()//获取GPS的当前状态
                //获取定位时间
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = Date(amapLocation.getTime())
                df.format(date)*/

            } else {
                callback!!.fail(""+location.errorCode+location.errorInfo)
            }
            stopLocation()
        }
    }

    interface ResultCallback {
        fun success(location: AMapLocation)//定位成功的返回
        fun fail(s: String)//定位失败
    }


}