package com.huixiangtv.liveshow.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.model.CustomMessage;
import com.huixiangtv.liveshow.model.DynamicComment;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.model.Other;
import com.huixiangtv.liveshow.model.PlayUrl;
import com.huixiangtv.liveshow.model.Share;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.pop.CameraWindow;
import com.huixiangtv.liveshow.pop.LoginWindow;
import com.huixiangtv.liveshow.pop.ShareTwoWindow;
import com.huixiangtv.liveshow.pop.ShareWindow;
import com.huixiangtv.liveshow.pop.UserWindow;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.LoginCallBack;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgressTip;
import com.huixiangtv.liveshow.ui.EmptyView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.UserInfo;
import simbest.com.sharelib.IShareCallback;
import simbest.com.sharelib.ShareModel;
import simbest.com.sharelib.ShareUtils;

/**
 * Created by hjw on 16/5/18.
 */
public class CommonHelper {
    private Context context;
    private ViewGroup rootContainer;


    public static PopupWindow pop;

    public  static Dialog dialog;

    public CommonHelper() {
    }

    public CommonHelper(Context context, ViewGroup rootContainer) {
        this.context = context;
        this.rootContainer = rootContainer;
    }





    /**
     * 关闭POP
     */
    public static void closePop(){
        if(pop!=null && pop.isShowing()){
            pop.dismiss();
        }
    }

    /**
     * 弹出分享面板
     *
     * @param activity
     * @param atLocationId
     * @param listener
     */
    public static void showSharePopWindow(Activity activity, int atLocationId, final ShareWindow.SelectShareListener listener) {
        pop = new ShareTwoWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        ((ShareTwoWindow)pop).setListener(new ShareTwoWindow.SelectShareListener() {
            @Override
            public void select(SHARE_MEDIA platForm) {
                super.select(platForm);
                if(null!=listener){
                    listener.select(platForm);
                }
            }

            @Override
            public void selectCopy() {
                super.selectCopy();
                if(null!=listener){
                    listener.selectCopy();
                }
            }
        });
    }


    public static void showCameraPopWindow(Activity activity, int atLocationId, CameraWindow.SelectListener listener) {
        pop = new CameraWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        if (null != listener) {
            ((CameraWindow)pop).setListener(listener);
        }

    }

    public static void showLoginPopWindow(Activity activity, int atLocationId, LoginCallBack callBack) {
        pop = new LoginWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        if (null != callBack) {
            ((LoginWindow)pop).setListener(callBack);
        }

    }


    public static void showUserPopWindow(Activity activity, int atLocationId,Live live) {
        pop = new UserWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,live);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();


    }





    public static void showTip(Context activity, String msg) {


        try{
            ColaProgressTip cpTip = ColaProgressTip.show(activity, msg, false, true, null, null);
            ColaProgressTip.showTip(900l, cpTip);
        }catch (Exception e){
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }

    }


    public static void delayViewIsEnable(final View view, long time, final boolean bool) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(bool);
            }
        }, time);
    }

    public static void viewSetBackageImag(final String url, final View view) {

        (new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                if (StringUtil.isNotNull(url)) {
                    return GaussUtils.returnBitMap(url);
                }
                return null;
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (null != bitmap) {
                    Drawable drawable = GaussUtils.hdAdvanceFastBlur(bitmap);
                    view.setBackground(drawable);
                }
            }
        }).execute();
    }



    public static void viewSetBackage(final String url, final View view) {
        (new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                if (StringUtil.isNotNull(url)) {
                    return GaussUtils.returnBitMap(url);
                }
                return null;
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (null != bitmap) {
                    Drawable drawable = new BitmapDrawable(bitmap);
                    view.setBackground(drawable);
                }
            }
        }).execute();
    }







    public static void share(final Activity activity, ShareModel model, final SHARE_MEDIA platform, final int shareType, final ApiCallback back) {
        ShareUtils su = new ShareUtils(activity);
        su.share(model, platform, new IShareCallback() {
            @Override
            public void onSuccess() {
                if (null != back) {
                    back.onSuccess("ok");
                }
                shareAddLove(shareType, platform);
            }

            @Override
            public void onFaild() {
                if (null != back) {
                    back.onSuccess("ok");
                }
                shareAddLove(shareType, platform);
            }

            @Override
            public void onCancel() {
                shareAddLove(shareType, platform);
            }
        });
    }



    /**
     * 分享送爱心
     * @param shareType
     * @param platform
     */
    private static void shareAddLove(int shareType, SHARE_MEDIA platform) {
        String platForm = "0";
        if(platform==SHARE_MEDIA.QQ){
            platForm  = "1";
        }else if(platform==SHARE_MEDIA.SINA){
            platForm  = "2";
        }else if(platform==SHARE_MEDIA.WEIXIN_CIRCLE){
            platForm  = "3";
        }else if(platform==SHARE_MEDIA.WEIXIN){
            platForm  = "4";
        }else if(platform==SHARE_MEDIA.QZONE){
            platForm  = "5";
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("shareType",shareType+"");
        params.put("platform",platForm);
        RequestUtils.sendPostRequest(Api.ADD_LOVE, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, String.class);
    }


    /**
     * 关注状态
     * @param artistId
     * @param apiCallback
     */
    public static void fenStatus(final String artistId, final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("artistId",artistId);
        RequestUtils.sendPostRequest(Api.ATTENTIOIN_STATUS, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data);
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }


    /**
     * 认证状态
     * @param apiCallback
     */
    public static void authStauts(final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        RequestUtils.sendPostRequest(Api.AUTH_STATUS, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data);
            }
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }


    /**
     * 艺人卡上传状态
     * @param uid
     * @param apiCallback
     */
    public static void cardStatus(String uid, final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        if(StringUtil.isNotNull(uid)){
            params.put("uid",uid);
        }
        RequestUtils.sendPostRequest(Api.GET_USER_ARTISTCARD_STATUS, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data);
            }
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }


    /**
     * 举报
     * @param apiCallback
     */
    public static void jubao(String type,String content,String bid,final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type",type);
        params.put("content",content);
        params.put("bid",bid);
        RequestUtils.sendPostRequest(Api.TIP_OFF, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data);
            }
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }


    /**
     * 我的朋友
     * @param apiCallback
     */
    public static void myFriend(final ApiCallback<List<Friend>> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page","1");
        params.put("pageSize","10000");
        RequestUtils.sendPostRequest(Api.MY_FRIEND, params, new ResponseCallBack<Friend>() {
            @Override
            public void onSuccessList(List<Friend> data) {
                super.onSuccessList(data);
                apiCallback.onSuccess(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                apiCallback.onFailure(e);
            }
        }, Friend.class);
    }

    /**
     * 关注
     * @param artistId
     * @param apiCallback
     */
    public static void addFen(final String artistId,final boolean istoFen, final ApiCallback<String> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("artistId",artistId);
        RequestUtils.sendPostRequest(Api.ATTENTIOIN_STATUS, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data && data.getIsFollowed().equals("1")){
                    apiCallback.onFailure(new ServiceException("已关注过"));
                }else{
                    if(istoFen)
                    toFen(artistId,apiCallback);
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }





    private static void toFen(String artistId, final ApiCallback<String> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("followid",artistId);
        RequestUtils.sendPostRequest(Api.ATTENTION, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data);
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onSuccess(e.getMessage());
            }
        }, String.class);
    }


    /**
     * 我的粉丝数量
     * @param apiCallback
     */
    public static void myFansCount(String uid,final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        if(StringUtil.isNotNull(uid)){
            params.put("uid",uid);
        }
        RequestUtils.sendPostRequest(Api.FANS_COUNT, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }else{
                    apiCallback.onSuccess(null);
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }

    public static void myAccount(final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        RequestUtils.sendPostRequest(Api.ACCOUNT, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }else{
                    apiCallback.onSuccess(null);
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Other.class);
    }


    /**
     * 用户信息
     * @param apiCallback
     */
    public static void userInfo(String uid,final ApiCallback<User> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        if(StringUtil.isNotNull(uid)){
            params.put("uid",uid);
        }
        RequestUtils.sendPostRequest(Api.USER_INFO, params, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }else{
                    apiCallback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, User.class);
    }


    /**
     * 是否是好友
     * @param apiCallback
     */
    public static void isFriend(String fid,final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fid",fid);
        RequestUtils.sendPostRequest(Api.IS_FRIEND, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }else{
                    apiCallback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }


    public static void joinFansGroup(String fid, final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("aid",fid);
        RequestUtils.sendPostRequest(Api.JOIN_FANS_GROUP, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }else{
                    apiCallback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onFailure(e);
            }
        }, Other.class);
    }

    public static void cutScreen(Activity activity)  {
        String dir = getSDCardPath();
        String filepath = dir+"/"+ System.currentTimeMillis()+".png";
        File path = new File(dir);
        File file = new File(filepath);
        try{
            if(!path.exists()){
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch(Exception e){

        }
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
            CommonHelper.showTip(activity,"截图已保存至:"+filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private static String getSDCardPath(){
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdcardExist){
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }



    public static void noData(String msg, ListView mListView, Activity activity,int num) {
        Log.i("noData","noData"+mListView.getHeaderViewsCount());
        if(null!=mListView){
            EmptyView view = new EmptyView(activity);
            view.setMsg(msg);
            if(mListView.getHeaderViewsCount()==num) {
                mListView.addHeaderView(view);
            }
        }

    }


    /**
     *
     * @param platform  1:QQ  2:SINA 3:朋友圈 4:webChat 5:QQZONE
     * @param type  0直播  1 艺人卡
     * @param bid   业务ID 直播ID & 艺人卡ID
     * @param apiCallback
     */
    public static void shareInfo(SHARE_MEDIA platform,String type,String bid,final ApiCallback<Share> apiCallback)
    {


        String flag = "";
        if(platform==SHARE_MEDIA.QQ){
            flag="1";
        }else if(platform==SHARE_MEDIA.SINA){
            flag="2";
        }else if(platform==SHARE_MEDIA.WEIXIN_CIRCLE){
            flag="3";
        }else if(platform==SHARE_MEDIA.WEIXIN){
            flag="4";
        }else if(platform==SHARE_MEDIA.QZONE){
            flag="5";
        }


        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("platform",flag);
        paramsMap.put("type",type);
        paramsMap.put("bid",bid);

        //待用
        //paramsMap.put("tip1",tip1);
        //paramsMap.put("tip2",tip2);

        RequestUtils.sendPostRequest(Api.SHARE_INFO, paramsMap, new ResponseCallBack<Share>() {
            @Override
            public void onSuccess(Share data) {
                apiCallback.onSuccess(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onSuccess(null);
            }
        }, Share.class);
    }


    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 回复评论
     * @param dc
     * @param apiCallback
     */
    public static void reComment(DynamicComment dc, final ApiCallback<String> apiCallback) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId",dc.getDynamicId());
        paramsMap.put("content",dc.getContent());

        RequestUtils.sendPostRequest(Api.ADD_COMMENT, paramsMap, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                apiCallback.onSuccess(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                apiCallback.onSuccess(null);
            }
        }, String.class);
    }

    public static void videoPlayUrl(Map<String, String> params, final ApiCallback<String> apiCallback) {

        RequestUtils.sendPostRequest(Api.VIDEO_URL, params, new ResponseCallBack<PlayUrl>() {
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }

            @Override
            public void onSuccess(PlayUrl data) {
                super.onSuccess(data);
                apiCallback.onSuccess(data.getUrl());
            }

            @Override
            public void onSuccessList(List<PlayUrl> datas) {
                super.onSuccessList(datas);
                if (null != datas && datas.size() > 0) {
                    apiCallback.onSuccess(datas.get(0).getUrl());
                }
            }
        }, PlayUrl.class);
    }


    /**
     * 组装自定义消息
     * @param msgContent
     * @param uid
     * @param uname
     * @param uphoto
     * @return
     */
    public static CustomMessage initMessageContent(final String msgContent, String uid, String uname, String uphoto) {
        CustomMessage mc = new CustomMessage() {
            @Override
            public byte[] encode() {
                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("content", msgContent);
                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
                }

                try {
                    return jsonObj.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }

        };
        UserInfo user = new UserInfo(uid,uname, Uri.parse(uphoto));
        mc.setUserInfo(user);
        return mc;
    }
}
