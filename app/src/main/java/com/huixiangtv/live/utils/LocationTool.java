package com.huixiangtv.live.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.List;
import java.util.Locale;


/**
 * Created by hjw on 2016/5/23.
 */
public class LocationTool {

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    private Context context;
    private Location mLocation;
    private LocationManager mLocationManager;

    public LocationTool(Context c) {
        super();
        context =c;

    }

    // 获取Location Provider
    private String getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(true);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return mLocationManager.getBestProvider(criteria, true);
    }


    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l != null) {
                mLocation = l;
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            mLocation = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocation = location;

            }

        }
    };

    public void closeLocation() {
        if (mLocationManager != null) {
            if (mLocationManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.removeUpdates(mLocationListener);
                mLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    public String[] jwd(){
        initLocal();
        String[] jwd = new String[3];
        Location location = getLocation();
        closeLocation();
        if(null!=location){
            jwd[0] = location.getLongitude()+"";
            jwd[1] = location.getLatitude()+"";

            List<Address> addrList=getAddressbyGeoPoint(location);
            if(null!=addrList){
                Address address = addrList.get(0);
                jwd[2] = address.getAddressLine(0);
            }else{
                jwd[2] = " ";
            }

        }
        return jwd;
    }

    private void initLocal() {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = mLocationManager.getLastKnownLocation(getProvider());

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new MyLocationListener(this));
    }

    private List<Address> getAddressbyGeoPoint(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                //获取 Geocoder ，通过 Geocoder 就可以拿到地址信息
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
