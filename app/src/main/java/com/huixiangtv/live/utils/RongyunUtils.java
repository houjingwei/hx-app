package com.huixiangtv.live.utils;

import android.content.Context;
import android.util.Log;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.model.Rongyun;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ChatTokenCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;

import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.RongIMClient;

/**
 * Created by WangWeiJie on 2016/5/24.
 */
public class RongyunUtils {

    private Context ct;

    public RongyunUtils(Context context) {
        this.ct = context;
    }
    public RongyunUtils() {

    }

    public void chatToken(final ChatTokenCallBack tokenCallBack) {
        //1.获取token
        Map<String,String> params = new HashMap<String,String>();
        if(null!= App.getLoginUser()) {
            User user = App.getLoginUser();
            params.put("nickName", user.getNickName());
            params.put("photo", user.getPhoto());
        }else {
            params.put("nickName","");
            params.put("photo","");
        }

        RequestUtils.sendGetRequest(Api.CHAT_TOKEN, params, new ResponseCallBack<Rongyun>() {
            @Override
            public void onSuccess(Rongyun data) {
                super.onSuccess(data);
                if(null!=tokenCallBack){
                    tokenCallBack.getTokenSuccess(data.getToken());
                }
            }

            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Rongyun.class);

    }






    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connect(String token, final ChatTokenCallBack tokenCallBack) {

        if (ct.getApplicationInfo().packageName.equals(App.getCurProcessName(ct.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--onSuccess---" + userid);
                    if(null!=tokenCallBack){
                        tokenCallBack.getTokenSuccess(userid);
                    }
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
}
