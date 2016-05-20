package com.huixiangtv.live;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.PreferencesHelper;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/4.
 */
public class App extends Application {
    private static App sContext;
    public static UMShareAPI mShareAPI ;

    private List<Activity> listActivity = new LinkedList<Activity>();
    //屏幕宽度，屏幕高度
    public static int screenWidth, screenHeight, statuBarHeight;




    private static User loginUser;
    private static PreferencesHelper loginHelper;

    public static long userCoin = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        PlatformConfig.setSinaWeibo("1912474872", "5ba10f129f1e5cdf37abd2c41bb7fd06");
        PlatformConfig.setWeixin("wx1e48313855ee1630", "4d395bee2cc7ce077773e0cc9d93da97");
        PlatformConfig.setQQZone("1105010761", "QWCN9CxD0blbth4M");
//      PlatformConfig.setSinaWeibo("3833863944", "dfea615e3114cf87412da53b2d3df173"); //自己的
        loginHelper = new PreferencesHelper(sContext, Constant.LOGIN_INFO);
        mShareAPI = UMShareAPI.get(this);
        //窗口管理器
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        statuBarHeight = getStatusBarHeight(sContext);
        //加载免费礼物数据
        loadFreeGiftList();

        if(null!=App.getLoginUser()){
            loadMyCoin();
        }
    }


    /**
     * 加载我的金币
     */
    private void loadMyCoin() {

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

    public void addActivity(Activity activity)
    {
        listActivity.add(activity);
    }

    public void finishAllActivity()
    {
        for(Activity activity:listActivity)
        {
            activity.finish();
        }
        listActivity.clear();
    }









    //普通礼物列表
    public static List<Gift> giftList = new ArrayList<Gift>();
    //礼物ID与礼物的映射
    public static Map<Integer, Gift> giftMap = new HashMap<Integer, Gift>();




    public static void loadFreeGiftList() {
        Map<String,String> giftParams = new HashMap<String, String>();
        giftParams.put("uid","001");
        giftParams.put("type","1");
        RequestUtils.sendPostRequest(Api.GIFT_LIST, giftParams, new ResponseCallBack<Gift>() {
            @Override
            public void onSuccessList(List<Gift> data) {
                super.onSuccessList(data);
                giftList = data;
                giftList = data;
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        },Gift.class);
    }



    public static User getLoginUser() {
        if (null != loginUser) {
            return loginUser;
        }
        if (!TextUtils.isEmpty(loginHelper.getValue("uid", "")) && !TextUtils.isEmpty(loginHelper.getValue("token", ""))) {
            loginUser = new User();
            loginUser.setToken(loginHelper.getValue("token", ""));
            loginUser.setUid(loginHelper.getValue("uid", ""));
            loginUser.setNickName(loginHelper.getValue("nickname", ""));
            loginUser.setSex(loginHelper.getValue("sex", ""));
            loginUser.setPhoto(loginHelper.getValue("photo", ""));
            loginUser.setBirthday(loginHelper.getValue("birthday", ""));
            loginUser.setCoins(loginHelper.getValue("coins", ""));
            loginUser.setHots(loginHelper.getValue("hots", ""));
            loginUser.setFans(loginHelper.getValue("fans", ""));
            loginUser.setOrders(loginHelper.getValue("orders", ""));
            loginUser.setLives(loginHelper.getValue("lives", ""));
            loginUser.setLoves(loginHelper.getValue("loves", ""));

        }
        return loginUser;
    }


    public static void saveLoginUser(User user) {
        loginHelper.setValue("uid", user.getUid());
        loginHelper.setValue("token", String.valueOf(user.getToken()));
        loginHelper.setValue("nickname", String.valueOf(user.getNickName()));
        loginHelper.setValue("sex", String.valueOf(user.getSex()));
        loginHelper.setValue("photo", String.valueOf(user.getPhoto()));
        loginHelper.setValue("birthday", String.valueOf(user.getBirthday()));
        loginHelper.setValue("coins", String.valueOf(user.getCoins()));
        loginHelper.setValue("hots", user.getHots());
        loginHelper.setValue("fans", user.getFans());
        loginHelper.setValue("orders", user.getOrders());
        loginHelper.setValue("lives", user.getLives());
        loginHelper.setValue("loves", user.getLoves());


    }

    public static  String getPreferencesValue(String key)
    {
        return loginHelper.getValue(key);
    }


}
