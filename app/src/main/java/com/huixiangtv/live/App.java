package com.huixiangtv.live;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;
import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.ChatTokenCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.PreferencesHelper;
import com.huixiangtv.live.utils.RongyunUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.message.TextMessage;

/**
 * Created by hjw on 16/5/4.
 */
public class App extends MultiDexApplication {
    private static final String TAG = "App";
    private static final String ACTION_NAME = "RONGYUN_MSG";
    private static App sContext;
    public static UMShareAPI mShareAPI ;

    //设备版本
    public static String deviceVersion;
    //设备型号
    public static String model;
    public static RongIMClient imClient;

    private List<Activity> listActivity = new LinkedList<Activity>();
    //屏幕宽度，屏幕高度
    public static int screenWidth, screenHeight, statuBarHeight;




    private static User loginUser;
    private static PreferencesHelper loginHelper;

    public static String userCoin = "0";

    private String chatToken = "";



    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        CrashReport.initCrashReport(this, "900034558", true);
        PlatformConfig.setSinaWeibo("1912474872", "5ba10f129f1e5cdf37abd2c41bb7fd06");
        PlatformConfig.setWeixin("wxd42ecbf70403ea6e", "74e3218222aab649f12a0f7511957413");
        PlatformConfig.setQQZone("1105349203", "zMoNzPEO7NBREbdy");
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
        //loadFreeGiftList(null);
        model=android.os.Build.MODEL; // 手机型号
        deviceVersion=android.os.Build.VERSION.RELEASE; // android系统版本号
        if(null!=App.getLoginUser()){
            loadMyCoin();
        }
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }
        imClient = RongIMClient.getInstance();
        final RongyunUtils rongyun = new RongyunUtils(this);
        rongyun.chatToken(new ChatTokenCallBack() {
            @Override
            public void getTokenSuccess(String token) {
                RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
                rongyun.connect(token,null);
            }
        });
        for (String str : new String[]{"gnustl_shared", "qupai-media-thirdparty", "qupai-media-jni"}) {
            System.loadLibrary(str);
        }
        qupaiAuth();
    }

    public static void qupaiAuth() {

        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Log.e(TAG, "ErrorCode" + errorCode + "message" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                Constant.accessToken = responseMessage;
            }
        });
        service.startAuth(getContext(),Constant.APP_KEY, Constant.APP_SECRET,Constant.SPACE);
    }


    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }





    /**
     * 加载我的金币
     */
    private void loadMyCoin() {
        userCoin = !getLoginUser().getCoins().equals("null")?getLoginUser().getCoins():"0";
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
    public static Map<String, Gift> giftMap = new HashMap<String, Gift>();

    public static void loadFreeGiftList(final ApiCallback callback) {
        Map<String,String> giftParams = new HashMap<String, String>();
        giftParams.put("uid","001");
        giftParams.put("type","1");
        RequestUtils.sendPostRequest(Api.GIFT_LIST, giftParams, new ResponseCallBack<Gift>() {
            @Override
            public void onSuccessList(List<Gift> data) {
                super.onSuccessList(data);
                giftList = data;
                for (Gift gift : giftList) {
                    giftMap.put(gift.getGid(), gift);
                }
                if(null!=callback){
                    callback.onSuccess(giftMap);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        },Gift.class);
    }



    public static User getLoginUser() {
        if (!TextUtils.isEmpty(loginHelper.getValue("uid", "")) && !TextUtils.isEmpty(loginHelper.getValue("token", ""))) {
            loginUser = new User();
            loginUser.setToken(loginHelper.getValue("token", ""));
            loginUser.setUid(loginHelper.getValue("uid", ""));
            loginUser.setNickName(loginHelper.getValue("nickname", ""));
            loginUser.setSex(loginHelper.getValue("sex", ""));
            loginUser.setPhoto(loginHelper.getValue("photo", ""));
            loginUser.setBirthday(loginHelper.getValue("birthday", ""));
            if(loginHelper.getValue("coins", "").equals("null")){
                loginUser.setCoins("0");
            }else{
                loginUser.setCoins(loginHelper.getValue("coins", ""));
            }
            loginUser.setHots(loginHelper.getValue("hots", ""));
            loginUser.setFans(loginHelper.getValue("fans", ""));
            loginUser.setOrders(loginHelper.getValue("orders", ""));
            loginUser.setLives(loginHelper.getValue("lives", ""));
            loginUser.setLoves(loginHelper.getValue("loves", "0"));
            loginUser.setTags(loginHelper.getValue("tags",""));

        }else{
            loginUser = null;
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
        loginHelper.setValue("tags", user.getTags());




    }


    public static void upUserTag(String tags) {
        loginHelper.setValue("tags", tags);
    }

    public static void upUserLove(String loves) {
        loginHelper.setValue("loves", loves);
    }

    public static void upUserBalance(String balance) {
        loginHelper.setValue("coins", balance);
    }


    /**
     * setting body information
     * @param user
     */
    public static void saveBodyInfo(User user)
    {
        loginHelper.setValue("bust", user.getBust()); //胸围
        loginHelper.setValue("hip",user.getHip());
        loginHelper.setValue("waist",user.getWaist()); //腰部
        loginHelper.setValue("height",user.getHeight());
        loginHelper.setValue("weight",user.getWeight());

    }

    public static void saveBodyPic(User user)
    {
        loginHelper.setValue("img1",user.getImg1());
        loginHelper.setValue("img2",user.getImg2());
        loginHelper.setValue("img3",user.getImg3());
        loginHelper.setValue("img4",user.getImg4());
        loginHelper.setValue("img5",user.getImg5());
    }

    public static void saveBodyLocPic(User user)
    {
        loginHelper.setValue("imgloc1",user.getImgLoc1());
        loginHelper.setValue("imgloc2",user.getImgLoc2());
        loginHelper.setValue("imgloc3",user.getImgLoc3());
        loginHelper.setValue("imgloc4",user.getImgLoc4());
        loginHelper.setValue("imgloc5",user.getImgLoc5());
    }

    /**
     * 签到时间
     * @param context
     * @param date
     */
    public static void setDailyCheckInDate(Context context, Date date) {
        long time = date.getTime();
        loginHelper.setLongValue("time", time);
    }

    /**
     * 签到时间
     * @param key
     */
    public static Date getDailyCheckInDate(String key)
    {
        long time = loginHelper.getLongValue(key, -1);
        if(time != -1) {
            Date date = new Date(time);
            return date;
        }
        return null;
    }


    public static  String getPreferencesValue(String key)
    {
        return loginHelper.getValue(key);
    }


    public static void clearLoginUser() {
        loginHelper.setValue("token","");
        loginHelper.setValue("uid","");
        App.loginUser = null;

    }

    /*
	 * 获取当前程序的版本号
	 */
    public static String getVersionCode(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packInfo.versionName.toString();
    }




    /**
     * 接收消息监听
     */
    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int i) {
            if (message.getContent() instanceof TextMessage) {
                TextMessage tm = (TextMessage) message.getContent();
                final LiveMsg msg = JSON.parseObject(String.valueOf(tm.getExtra()), LiveMsg.class);
                Log.i("msgType","----------"+msg.getMsgType()+"----------");
                msg.setContent(tm.getContent().toString());
                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("msg",msg);
                //发送广播
                sendBroadcast(mIntent);
                Log.i("msgmsg", msg.toString());
            }


            return false;
        }
    }


}
