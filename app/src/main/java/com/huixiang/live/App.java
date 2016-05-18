package com.huixiang.live;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.lang.reflect.Field;

/**
 * Created by hjw on 16/5/4.
 */
public class App extends Application {
    private static App sContext;
    public static UMShareAPI mShareAPI ;

    //屏幕宽度，屏幕高度
    public static int screenWidth, screenHeight, statuBarHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.

        PlatformConfig.setWeixin("wx1e48313855ee1630","4d395bee2cc7ce077773e0cc9d93da97");
        PlatformConfig.setQQZone("1105010761","QWCN9CxD0blbth4M");
        PlatformConfig.setSinaWeibo("1802731919","1780dd641fba0d3656a071c4e74ed840");

        mShareAPI = UMShareAPI.get(this);


        //窗口管理器
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        statuBarHeight = getStatusBarHeight(sContext);
    }


    /**
     * 获取手机状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static App getContext() {
        return sContext;
    }

    public void show(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
