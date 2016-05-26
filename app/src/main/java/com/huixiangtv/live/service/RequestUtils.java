package com.huixiangtv.live.service;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
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
        if(null!= App.getLoginUser()){
            reParams.addQueryStringParameter("uid", App.getLoginUser().getUid()+"");
            reParams.addQueryStringParameter("token",App.getLoginUser().getToken()+"");
        }

        //解析封装参数
        if(null!=paramsMap && paramsMap.size()>0) {
            for (String key : paramsMap.keySet()) {
                reParams.addQueryStringParameter(key, paramsMap.get(key));
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
                        String jsonStr = "{"+String.valueOf(response.getData())+"}";
                        boolean isJson =  new JsonValidator().validate(jsonStr);
                        if(isJson){
                            if(jsonStr.contains("[")){
                                callBack.onSuccessList(JSON.parseArray(jsonStr,clazz));
                            }else if(!jsonStr.contains("[")){
                                callBack.onSuccess(JSON.parseObject(jsonStr,clazz));
                            }
                        }else{
                            callBack.onSuccessString((String) response.getData());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onFailure(new ServiceException(ex.getMessage()));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException(cex.getMessage()));
            }

            @Override
            public void onFinished() {

            }
        });


    }



    public static <T> void getIOToken(final String url, Map<String, String> paramsMap,final ResponseCallBack<T> callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Log.d(Constant.TAG, "--------------------------------------------------------");
        Log.d(Constant.TAG, "preparing call >" + url);
        Log.d(Constant.TAG, "originl params >" + paramsMap);

        RequestParams reParams = new RequestParams(url);
        if(null!= App.getLoginUser()){
            reParams.addQueryStringParameter("uid", App.getLoginUser().getUid()+"");
            reParams.addQueryStringParameter("token",App.getLoginUser().getToken()+"");
        }else{
            reParams.addQueryStringParameter("uid", "");
            reParams.addQueryStringParameter("token","");
        }

        //解析封装参数
        if(null!=paramsMap && paramsMap.size()>0) {
            for (String key : paramsMap.keySet()) {
                reParams.addQueryStringParameter(key, paramsMap.get(key));
            }
        }
        x.http().request(HttpMethod.POST, reParams, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.d(Constant.TAG, "解析后内容 >> " + result);
                BaseResponse response = JSON.parseObject(result,BaseResponse.class);
                try {
                    if (response == null) {
                        if (callBack != null) {
                            callBack.onFailure(new ServiceException("未请求到数据"));
                        }
                        return;
                    }
                    if (callBack != null) {
                        callBack.onSuccessString(String.valueOf(response.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onFailure(new ServiceException(e.getMessage()));
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onFailure(new ServiceException(ex.getMessage()));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException(cex.getMessage()));
            }

            @Override
            public void onFinished() {

            }
        });


    }


}
