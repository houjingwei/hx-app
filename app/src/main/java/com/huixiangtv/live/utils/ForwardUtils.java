package com.huixiangtv.live.utils;

import android.app.Activity;
import android.content.Intent;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.AccountActivity;
import com.huixiangtv.live.activity.H5Activity;
import com.huixiangtv.live.activity.LiveActivity;
import com.huixiangtv.live.activity.LoginOrRegActivity;
import com.huixiangtv.live.activity.RefreshAndLoadmoreActivity;
import com.huixiangtv.live.activity.RegLiveActivity;
import com.huixiangtv.live.activity.RegLiveMainActivity;
import com.huixiangtv.live.activity.RegLiveNextActivity;
import com.huixiangtv.live.activity.RegLiveSuccessActivity;
import com.huixiangtv.live.activity.SearchActivity;
import com.huixiangtv.live.activity.SetActivity;
import com.huixiangtv.live.activity.StartLiveActivity;
import com.huixiangtv.live.activity.TopicActivity;
import com.huixiangtv.live.activity.UserTagActivity;
import com.huixiangtv.live.activity.UserinfoActivity;

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
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://refresh")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://login")) {
                Intent intent = new Intent(oriActivity, LoginOrRegActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://register")) {
                Intent intent = new Intent(oriActivity, LoginOrRegActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://forgot")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://live")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://push")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://ucenter")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://charge")) {
                Intent intent = new Intent(oriActivity, RefreshAndLoadmoreActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://userinfo")) {
                Intent intent = new Intent(oriActivity, UserinfoActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith("huixiang://userTag")) {
                Intent intent = new Intent(oriActivity, UserTagActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.ACCOUNT)){
                Intent intent = new Intent(oriActivity, AccountActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.LOGIN)){
                Intent intent = new Intent(oriActivity, LoginOrRegActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.SETINT)){
                Intent intent = new Intent(oriActivity, SetActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.START_LIVE)){
                Intent intent = new Intent(oriActivity, StartLiveActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.LIVE_TOPIC)){
                Intent intent = new Intent(oriActivity, TopicActivity.class);
                setIntentInfo(intent,params);
                oriActivity.startActivityForResult(intent, 1);
                oriActivity.overridePendingTransition(R.anim.push_left_in1, R.anim.push_right_out1);
            }else if (url.startsWith(Constant.LIVE)){
                Intent intent = new Intent(oriActivity, LiveActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.SEARCH)){
                Intent intent = new Intent(oriActivity, SearchActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.REG_LIVE)){
                Intent intent = new Intent(oriActivity, RegLiveActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.REG_LIVE_NEXT)){
                Intent intent = new Intent(oriActivity, RegLiveNextActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.REG_LIVE_DES)){
                Intent intent = new Intent(oriActivity, RegLiveSuccessActivity.class);
                toIntent(oriActivity, params, intent);
            }else if (url.startsWith(Constant.REG_LIVE_MAIN)){
                Intent intent = new Intent(oriActivity, RegLiveMainActivity.class);
                toIntent(oriActivity, params, intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


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
