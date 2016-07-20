package com.huixiangtv.liveshow.utils;

import android.app.Activity;
import android.content.Intent;

import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.activity.AccountActivity;
import com.huixiangtv.liveshow.activity.AddFriendActivity;
import com.huixiangtv.liveshow.activity.AttentionMeActivity;
import com.huixiangtv.liveshow.activity.ChatMsgActivity;
import com.huixiangtv.liveshow.activity.CouponActivity;
import com.huixiangtv.liveshow.activity.DynamicDetialActivity;
import com.huixiangtv.liveshow.activity.FriendActivity;
import com.huixiangtv.liveshow.activity.H5Activity;
import com.huixiangtv.liveshow.activity.LiveActivity;
import com.huixiangtv.liveshow.activity.LiveRecordActivity;
import com.huixiangtv.liveshow.activity.LiveRecordFinishActivity;
import com.huixiangtv.liveshow.activity.LoginOrRegActivity;
import com.huixiangtv.liveshow.activity.MyAttentionActivity;
import com.huixiangtv.liveshow.activity.MyCircleActivity;
import com.huixiangtv.liveshow.activity.MyHotsActivity;
import com.huixiangtv.liveshow.activity.MylovesActivity;
import com.huixiangtv.liveshow.activity.PhoneBindActivity;
import com.huixiangtv.liveshow.activity.PushDynamicActivity;
import com.huixiangtv.liveshow.activity.RegLiveActivity;
import com.huixiangtv.liveshow.activity.RegLiveMainActivity;
import com.huixiangtv.liveshow.activity.RegLiveNextActivity;
import com.huixiangtv.liveshow.activity.RegLiveSuccessActivity;
import com.huixiangtv.liveshow.activity.RegPicListActivity;
import com.huixiangtv.liveshow.activity.SearchActivity;
import com.huixiangtv.liveshow.activity.SettingActivity;
import com.huixiangtv.liveshow.activity.TopicActivity;
import com.huixiangtv.liveshow.activity.UserInfoActivity;
import com.huixiangtv.liveshow.activity.UserTagActivity;
import com.huixiangtv.liveshow.activity.MyInfoActivity;
import com.huixiangtv.liveshow.activity.WebActivity;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjw on 16/5/9.
 */
public class ForwardUtils {

    public static void target(Activity oriActivity, String url,Map<String, String> params){

        try {
            if ("".equals(url)) {
                return;
            }
            if(oriActivity==null){
                return;
            }
            if(url.startsWith("http:")){
                Intent intent = new Intent(oriActivity, H5Activity.class);
                toH5Intent(oriActivity, url, intent);
            }else if (url.equals(Constant.ACCOUNT)) {
                Intent intent = new Intent(oriActivity, AccountActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.ACCOUNT)) {
                Intent intent = new Intent(oriActivity, LoginOrRegActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.MY_INFO)) {
                Intent intent = new Intent(oriActivity, MyInfoActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.USER_INFO)) {
                Intent intent = new Intent(oriActivity, UserInfoActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.USERTAG)) {
                Intent intent = new Intent(oriActivity, UserTagActivity.class);
                setIntentInfo(intent,params);
                oriActivity.startActivityForResult(intent, 108);
                oriActivity.overridePendingTransition(R.anim.push_left_in1, R.anim.push_right_out1);
            }else if (url.equals(Constant.ACCOUNT)){
                Intent intent = new Intent(oriActivity, AccountActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.LOGIN)){
                Intent intent = new Intent(oriActivity, LoginOrRegActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.SETINT)){
                Intent intent = new Intent(oriActivity, SettingActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.START_LIVE)){
                Intent intent = new Intent(oriActivity, LiveRecordActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.LIVERECORD_FINISH)){
                Intent intent = new Intent(oriActivity, LiveRecordFinishActivity.class);
                noAniToIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.LIVE_TOPIC)){
                Intent intent = new Intent(oriActivity, TopicActivity.class);
                setIntentInfo(intent,params);
                oriActivity.startActivityForResult(intent, 1);
                oriActivity.overridePendingTransition(R.anim.push_left_in1, R.anim.push_right_out1);
            }else if (url.equals(Constant.LIVE)){
                Intent intent = new Intent(oriActivity, LiveActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.SEARCH)){
                Intent intent = new Intent(oriActivity, SearchActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.REG_LIVE)){
                Intent intent = new Intent(oriActivity, RegLiveActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.REG_LIVE_NEXT)){
                Intent intent = new Intent(oriActivity, RegLiveNextActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.REG_LIVE_DES)){
                Intent intent = new Intent(oriActivity, RegLiveSuccessActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.REG_LIVE_MAIN)){
                Intent intent = new Intent(oriActivity, RegLiveMainActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.equals(Constant.FANS)){
                Intent intent = new Intent(oriActivity, MyAttentionActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.PHONE_BIND)){
                Intent intent = new Intent(oriActivity, PhoneBindActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.MY_FANS)){
                Intent intent = new Intent(oriActivity, AttentionMeActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.FANED_ME)){
                Intent intent = new Intent(oriActivity, MyAttentionActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.MY_LOVES)){
                Intent intent = new Intent(oriActivity, MylovesActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.PIC_LIST)){
                Intent intent = new Intent(oriActivity, RegPicListActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.MY_HOTS)){
                Intent intent = new Intent(oriActivity, MyHotsActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.BINNER_URL)){
                Intent intent = new Intent(oriActivity, WebActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.OWN_CIRCLE)){
                Intent intent = new Intent(oriActivity, MyCircleActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.PUSH_DYNAMIC)){
                Intent intent = new Intent(oriActivity, PushDynamicActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.DYNAMIC_DETAIL)){
                Intent intent = new Intent(oriActivity, DynamicDetialActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.COUPON)){
                Intent intent = new Intent(oriActivity, CouponActivity.class);
                toIntent(oriActivity,params,intent);
            }else if (url.equals(Constant.FRIEND)){
                Intent intent = new Intent(oriActivity, FriendActivity.class);
                toIntent(oriActivity,params,intent);
            }else if(url.equals(Constant.ADD_FRIEND)){
                Intent intent = new Intent(oriActivity, AddFriendActivity.class);
                setIntentInfo(intent,params);
                oriActivity.startActivityForResult(intent, 1);
                oriActivity.overridePendingTransition(R.anim.push_left_in1, R.anim.push_right_out1);

            }else if (url.equals(Constant.CHAT_MSG)){
                Intent intent = new Intent(oriActivity, ChatMsgActivity.class);
                toIntent(oriActivity,params,intent);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void toH5Intent(Activity oriActivity,String url, Intent intent) throws Exception {
        int index = 0;
        String s1 = "";
        if(url.contains("?")){
            index = url.indexOf("?");
            s1 = url.substring(0,index);
        }else{
            s1 = url;
        }

        Map params = new HashMap();
        params.put("url",URLDecoder.decode(s1,"utf-8"));

        if(index>0){
            String s2 = url.substring(index+1,url.length());
            if(StringUtil.isNotEmpty(s2)){
                String[] s = s2.split("&");
                for (String param : s) {
                    String[] s3 = param.split("=");
                    params.put(s3[0],s3[1]);
                }
            }
        }
        toIntent(oriActivity, params, intent);
    }


    /**
     * 跳转到对应的activity
     * @param oriActivity
     * @param params
     * @param intent
     */
    private static void toIntent(Activity oriActivity, Map<String, String> params, Intent intent) {
        setIntentInfo(intent,params);
        oriActivity.startActivity(intent);
        oriActivity.overridePendingTransition(R.anim.push_left_in1, R.anim.push_right_out1);
    }

    /**
     * 无动画跳转到对应的activity
     * @param oriActivity
     * @param params
     * @param intent
     */
    private static void noAniToIntent(Activity oriActivity, Map<String, String> params, Intent intent) {
        setIntentInfo(intent,params);
        oriActivity.startActivity(intent);
        oriActivity.finish();
    }

    private static void setIntentInfo(Intent intent, Map<String, String> paramsMap) {
        //解析封装参数
        if(null!=paramsMap && paramsMap.size()>0) {
            for (String key : paramsMap.keySet()) {
                intent.putExtra(key,paramsMap.get(key));
            }
        }
    }


    /**
     * 参数解析
     * @param url 跳转地址
     * @return
     * @throws Exception
     */
    public static Map<String,String> parseParameters(String url)throws Exception {
        String arg = url.substring(url.indexOf("?")+1,url.length());
        if(arg!=null){
            arg = URLDecoder.decode(arg,"utf-8");
        }
        String[] strs = arg.split("&");
        HashMap<String, String> map = new HashMap<String,String>();
        for(int x=0;x<strs.length;x++){
            String[] strs2 = strs[x].split("=");
            if(strs2.length==2){
                System.out.println(strs2[0]+"  "+strs2[1]);
                map.put(strs2[0], strs2[1]);
            }
        }
        return map;
    }
}
