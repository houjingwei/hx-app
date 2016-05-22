package com.huixiangtv.live.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.huixiangtv.live.App;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MyLocationListener implements LocationListener {

    private LocationTool gpsTool;

    /**构造方法，传入LocationTool
     * @param gpsTool
     */
    public MyLocationListener(LocationTool gpsTool) {
        super();
        this.gpsTool = gpsTool;
    }

    /**
     * 当前位置改变后，回调onLocationChanged方法，获取改变后的Location对象
     *
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            gpsTool.setLocation(location);
        }

    }

    /**
     * 当provider状态改变时回调的方法，当前的provider无法读取位置信息或者provider从无法读取位置信息变为能够读取为信息被回调的方法
     *
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    /**
     * 当provider被用户允许开启，回调的onProviderEnabled方法，比如：开启定位功能，回调该方法
     *
     */
    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location l = gpsTool.getLocationManager().getLastKnownLocation(provider);

        if (l != null) {
            gpsTool.setLocation(l);
        }

    }

    /**
     * 当provider不被用户允许开启，回调的onProviderDisabled方法，比如：无法开启定位功能，回调该方法
     *
     */
    @Override
    public void onProviderDisabled(String provider) {
        gpsTool.setLocation(null);

    }

}