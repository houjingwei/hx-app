package com.huixiangtv.live.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.pop.CameraWindow;
import com.huixiangtv.live.pop.LoginWindow;
import com.huixiangtv.live.pop.ShareWindow;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgressTip;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public static void showSharePopWindow(Activity activity, int atLocationId, ShareWindow.SelectShareListener listener) {
        pop = new ShareWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        if (null != listener) {
            ((ShareWindow)pop).setListener(listener);
        }
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



    /**
     * 弹出分享面板
     *
     * @param activity
     * @param atLocationId
     */
    public static void showSharePopWindow(final Activity activity, int atLocationId, final String content, final String cover, final String targetUrl) {
        pop = new ShareWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(activity.findViewById(atLocationId), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        ((ShareWindow)pop).setListener(new ShareWindow.SelectShareListener() {
            @Override
            public void select(SHARE_MEDIA platForm) {
                super.select(platForm);
                showTip(activity,"share to " + platForm);
                //App.share(activity, platForm, content, cover, targetUrl);
            }
        });
    }




    public static void showTip(Context activity, String msg) {
        ColaProgressTip cpTip = ColaProgressTip.show(activity, msg, false, true, null, null);
        ColaProgressTip.showTip(900l, cpTip);
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




    public static void share(final Activity activity, String title, String content, SHARE_MEDIA platForm, String url, String tarUrl, final ApiCallback back) {
        ShareSdk.startShare(activity, title, content, platForm, url, tarUrl,new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                showTip(activity,"分享成功");
                if(null!=back){
                    back.onSuccess("ok");
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                showTip(activity,"分享失败啦");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                showTip(activity,"分享取消了");
            }
        });
    }


    /**
     * 关注
     * @param artistId
     * @param apiCallback
     */
    public static void addFen(final String artistId, final ApiCallback<String> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("artistId",artistId);
        RequestUtils.sendPostRequest(Api.ATTENTIOIN_STATUS, params, new ResponseCallBack<Other>() {
            @Override
            public void onSuccess(Other data) {
                super.onSuccess(data);
                if(null!=data && data.getIsFollowed().equals("1")){
                    apiCallback.onFailure(new ServiceException("已关注过"));
                }else{
                    toFen(artistId,apiCallback);
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
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
    public static void myFansCount(final ApiCallback<Other> apiCallback) {
        Map<String, String> params = new HashMap<String, String>();
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

}
