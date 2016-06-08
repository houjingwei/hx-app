package com.huixiangtv.live.service;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.model.Rongyun;
import com.huixiangtv.live.utils.JsonValidator;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by hjw on 16/5/4.
 */
public class RequestUtils {

    private final static Gson gson = new Gson();


    /**
     *
     * @param url  请求地址
     * @param paramsMap  请求参数
     * @param callBack   回调
     * @param clazz      bean类
     * @param <T>
     */
    public static<T> void sendPostRequest(String url , Map<String, String> paramsMap, ResponseCallBack<T> callBack,Class<T> clazz) {
        sendRequest(HttpMethod.POST, url, paramsMap, callBack, clazz);
    }


    /**
     *
     * @param url  请求地址
     * @param paramsMap  请求参数
     * @param callBack   回调
     * @param clazz      bean类
     * @param <T>
     */
    public static<T> void sendGetRequest(String url , Map<String, String> paramsMap, ResponseCallBack<T> callBack,Class<T> clazz) {
        sendRequest(HttpMethod.GET, url, paramsMap, callBack, clazz);
    }


    public static <T> void sendRequest(HttpMethod method, final String url, Map<String, String> paramsMap,final ResponseCallBack<T> callBack, final Class<T> clazz) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Log.d(Constant.TAG, "--------------------------------------------------------");
        Log.d(Constant.TAG, "preparing call >" + url);
        Log.d(Constant.TAG, "originl params >" + paramsMap);

        RequestParams reParams = new RequestParams(url);
        reParams.setCharset("utf-8");
        if(null!= App.getLoginUser()){
            reParams.addBodyParameter("uid", App.getLoginUser().getUid()+"");
            reParams.addBodyParameter("token",App.getLoginUser().getToken()+"");

        }

        //解析封装参数
        if(null!=paramsMap && paramsMap.size()>0) {
            for (String key : paramsMap.keySet()) {
                reParams.addBodyParameter(key, paramsMap.get(key));
            }
        }
        x.http().request(method, reParams, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.d(Constant.TAG, "解析后内容 >> " + result);
                BaseResponse response = JSON.parseObject(result,BaseResponse.class);
                if(null!=response){
                    if(response.getCode()!=0){
                        callBack.onFailure(new ServiceException(response.getMsg()+",错误码:"+response.getCode()));
                    }else{
                        String jsonStr = String.valueOf(response.getData());
                        String firstChar = jsonStr.substring(0,1);
                        if(firstChar.equals("[")){
                            callBack.onSuccessList(JSON.parseArray(jsonStr,clazz));
                        }else{
                            if(jsonStr.equals("null")){
                                callBack.onSuccess(JSON.parseObject(jsonStr,clazz));
                            }else{
                                callBack.onSuccess(JSON.parseObject(jsonStr,clazz));
                            }

                        }

                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onFailure(new ServiceException("服务器异常："+ex.getMessage()));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException("已取消请求"));
            }

            @Override
            public void onFinished() {

            }
        });


    }

}
