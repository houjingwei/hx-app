package com.huixiang.live;

import android.app.Application;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

/**
 * Created by hjw on 16/5/4.
 */
public class App extends Application {
    private static App sContext;
    public static UMShareAPI mShareAPI ;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        PlatformConfig.setWeixin("wx1e48313855ee1630","4d395bee2cc7ce077773e0cc9d93da97");
        PlatformConfig.setQQZone("1105010761","QWCN9CxD0blbth4M");
        PlatformConfig.setSinaWeibo("1802731919","1780dd641fba0d3656a071c4e74ed840");

        mShareAPI = UMShareAPI.get(this);
    }

    public static App getContext() {
        return sContext;
    }

    public void show(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
