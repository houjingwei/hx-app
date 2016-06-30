package com.huixiangtv.live.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.config.Config;
import com.huixiangtv.live.model.DropImageModel;
import com.huixiangtv.live.model.Share;
import com.huixiangtv.live.model.Upfeile;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ShareSdk;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.DropImageView;
import com.huixiangtv.live.utils.widget.MySeekBar;
import com.huixiangtv.live.utils.widget.ZdpImageView;
import com.tencent.upload.task.data.FileInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.widget.FrameLayout.LayoutParams;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import simbest.com.sharelib.ShareModel;

/**
 * Created by Stone on 16/5/30.
 */
public class RegPicListActivity extends BaseBackActivity {

    enum RequestCode {
        Button(R.id.txtUpload);

        @IdRes
        final int mViewId;

        RequestCode(@IdRes int viewId) {
            mViewId = viewId;
        }
    }


    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_CODE_CAT = 12;
    private static User user;
    private Vibrator vibrator;// 长按震动效果
    private boolean isMove = false;// 是否移动
    private int tag = -1;// 要变换图标
    private int moveTag = -1;// 当前移动到的位置
    private static int currentTag = 0;
    private Bitmap bm;
    private File path;
    static boolean drop_index = false;
    static String current_corp_img = "";
    private FileOutputStream fos = null;
    private File outputImage = null;
    private long lastClickTime;

    @ViewInject(R.id.txtName)
    TextView txtName;

    @ViewInject(R.id.txtWeight)
    TextView txtWeight;

    @ViewInject(R.id.txtSj)
    TextView txtSj;

    @ViewInject(R.id.txtHeight)
    TextView txtHeight;

    @ViewInject(R.id.tvLid)
    TextView tvLid;

    @ViewInject(R.id.txtFansi)
    TextView txtFansi;

    @ViewInject(R.id.ll_per_info)
    LinearLayout ll_per_info;

    @ViewInject(R.id.tvYW)
    TextView tvYW;

    @ViewInject(R.id.txtShare)
    TextView txtShare;

    @ViewInject(R.id.imageView1)
    ZdpImageView imageView1;


    @ViewInject(R.id.tvShareId)
    TextView tvShareId;

    @ViewInject(R.id.imageView2)
    ZdpImageView imageView2;

    @ViewInject(R.id.imageView3)
    ZdpImageView imageView3;

    @ViewInject(R.id.imageView4)
    ZdpImageView imageView4;

    @ViewInject(R.id.imageView5)
    ZdpImageView imageView5;

    @ViewInject(R.id.merto_content)
    FrameLayout merto_content;


    @ViewInject(R.id.txtSave)
    TextView txtSave;

    @ViewInject(R.id.txtUpload)
    TextView txtUpload;

    @ViewInject(R.id.back)
    ImageView back;

    private ArrayList<DropImageModel> mertoBeans = new ArrayList<DropImageModel>();
    private ArrayList<DropImageModel> startBeans;// 保存itme变换前的内容
    private static ArrayList<DropImageView> mertoItemViews;
    MainActivity activity;
    int window_width, window_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = RegPicListActivity.this.getWindow();
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_pic_list);

        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();

        x.view().inject(this);
        startOnMertoItemViewListener();
        user = new User();
        String uid = getIntent().getStringExtra("uid");
        if (null == uid && App.getLoginUser() != null) {
            allEdit();
        } else if (null != uid) {
            noEdit(uid);
        } else {
            Toast.makeText(RegPicListActivity.this, "UID is " + uid, Toast.LENGTH_SHORT).show();
            finish();
        }


        txtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUpload.getTag().equals("2")) {
                    ll_per_info.setTag("1");
                    txtUpload.setText("上传");
                    v.setTag("1");
                    startOnMertoItemViewListener();
                    txtSave.setVisibility(View.VISIBLE);
                } else {
                    checkPermission(RequestCode.Button);
                }
            }
        });

        ArtistCardInfoStatus();

        ll_per_info.getBackground().setAlpha(150);


        ll_per_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_per_info.getTag().equals("1"))
                    showRegAlert(RegPicListActivity.this);
            }
        });

        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareAlert(RegPicListActivity.this, RegPicListActivity.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first validation
                for (DropImageView dropImageView : mertoItemViews) {
                    if (dropImageView.getIsFinish() < 5) {
                        Toast.makeText(getBaseContext(), "请选择选择图片到第" + (Integer.parseInt(dropImageView.getTag().toString()) + 1) + "张卡片", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                ArtistCardInfoSave();
            }
        });

        lastClickTime = 0;
    }

    private void allEdit() {
        user.setUid(App.getPreferencesValue("uid"));
    }

    private void noEdit(String uid) {
        txtUpload.setVisibility(View.GONE);
        ll_per_info.setTag("2");
        txtSave.setVisibility(View.GONE);
        user.setUid(uid);
    }


    private void checkPermission(@NonNull RequestCode requestCode) {

        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionState != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode.ordinal());

            }
        } else {
            onClicks(requestCode.mViewId);
        }
    }


    private void onClicks(@IdRes int viewId) {

        switch (viewId) {
            case R.id.txtUpload: {
                Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
                PhotoPickerIntent.setPhotoCount(intent, 5);
                PhotoPickerIntent.setColumn(intent, 4);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            }
        }
    }


    /**
     * select take photo the return result to elements control
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            List<String> photos = null;
            BitmapDrawable bd;
            if (data != null && requestCode != REQUEST_CODE_CAT) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
//            if (requestCode == REQUEST_CODE_ALL) {
//                File file = new File(photos.get((0)));
//                current_corp_img = file.getAbsolutePath();
//                Uri uri = Uri.fromFile(file);
//                startPhotoZoom(uri);
//
//            }

            if (requestCode == REQUEST_CODE_CAT) {
                if (data != null) {
                    Bundle extras = data.getBundleExtra("bundle");
                    bm = extras.getParcelable("data");
                    bm = extras.getParcelable("data");
                    //图片名称 时间命名
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    //Date date = new Date(System.currentTimeMillis());
                    //String filename = format.format(date);
                    //创建File对象用于存储拍照的图片 SD卡根目录
//                    File outputImage = new File(Environment.getExternalStorageDirectory(),test.jpg);
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    outputImage = new File(Environment.getExternalStorageDirectory(), "cj" + currentTag + ".jpg");

                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String local = outputImage.getAbsolutePath();
                    fos = new FileOutputStream(outputImage);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);// (0 -// 100)压缩文件
                    fos.flush();
                    fos.close();

                    bd = new BitmapDrawable(bm);
                    mertoItemViews.get(currentTag).setIcon(bd);
                    mertoBeans.get(currentTag).setIconId(bd);
                    mertoBeans.get(currentTag).setIsFinish(15);
                    mertoBeans.get(currentTag).setLocUrl(local);
                    mertoItemViews.get(currentTag).setIsFinish(15);
                    mertoItemViews.get(currentTag).setLocUrl(local);

                } else {
                    //record first
                    if (current_corp_img.length() != 0) {
                        bm = BitmapHelper.readBitMap(new File(current_corp_img), true);
                        String loc = WriteFileImgLoc(bm, currentTag);
                        bd = new BitmapDrawable(bm);
                        mertoItemViews.get(currentTag).setIcon(bd);
                        mertoBeans.get(currentTag).setIconId(bd);
                        mertoBeans.get(currentTag).setIsFinish(15);
                        mertoBeans.get(currentTag).setLocUrl(loc);
                        mertoItemViews.get(currentTag).setIsFinish(15);
                        mertoItemViews.get(currentTag).setLocUrl(loc);
                        current_corp_img = "";
                    }
                }
            } else {

                if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
                    if (photos.size() > 0 && photos.size() < 2) {

                        bm = BitmapHelper.readBitMap(new File(photos.get(0)), true);
                        String loc = WriteFileImgLoc(bm, currentTag);
                        bd = new BitmapDrawable(bm);
                        mertoItemViews.get(currentTag).setIcon(bd);
                        mertoBeans.get(currentTag).setIconId(bd);
                        mertoBeans.get(currentTag).setLocUrl(loc);
                        mertoBeans.get(currentTag).setIsFinish(15);
                        mertoItemViews.get(currentTag).setIsFinish(15);
                        mertoItemViews.get(currentTag).setLocUrl(loc);
                    } else if (photos.size() > 1) {
                        for (int size = 0; size < photos.size(); size++) {
                            bm = BitmapHelper.readBitMap(new File(photos.get(size)), true);
                            String loc = WriteFileImgLoc(bm, size);
                            bd = new BitmapDrawable(bm);
                            mertoItemViews.get(size).setIcon(bd);
                            mertoBeans.get(size).setIconId(bd);
                            mertoBeans.get(size).setIsFinish(15);
                            mertoBeans.get(size).setLocUrl(loc);
                            mertoItemViews.get(size).setIsFinish(15);
                            mertoItemViews.get(size).setLocUrl(loc);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(RegPicListActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Write bm to location
     *
     * @param bm
     * @param i
     * @return
     * @throws IOException
     */
    private String WriteFileImgLoc(Bitmap bm, int i) throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //String filename = ""+i;
        //创建File对象用于存储拍照的图片 SD卡根目录
        path = Environment.getExternalStorageDirectory();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(Environment.getExternalStorageDirectory(), "cj" + i + ".jpg");
        fos = new FileOutputStream(outputImage);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String local = outputImage.getAbsolutePath();
        fos = new FileOutputStream(outputImage);
        bm.compress(Bitmap.CompressFormat.JPEG, 75, fos);// (0 -// 100)压缩文件
        fos.flush();
        fos.close();
        return local;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay!
            onClicks(RequestCode.values()[requestCode].mViewId);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", 100);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, REQUEST_CODE_CAT);
    }

    /**
     * Share info
     *
     * @param context
     */
    public static void showShareAlert(final Activity activity, final Context context) {
        final ShareModel model = new ShareModel();
        final AlertDialog dlg = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.open_share);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = App.screenHeight;
        lp.alpha = 2.9f;
        window.setAttributes(lp);


        window.findViewById(R.id.rlqq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInfotoDB(activity, model, context, SHARE_MEDIA.QQ, dlg);

            }
        });


        window.findViewById(R.id.rlzone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInfotoDB(activity, model, context, SHARE_MEDIA.QZONE, dlg);
            }
        });

        window.findViewById(R.id.rlwx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInfotoDB(activity, model, context, SHARE_MEDIA.WEIXIN, dlg);
            }
        });

        window.findViewById(R.id.rlpyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInfotoDB(activity, model, context, SHARE_MEDIA.WEIXIN_CIRCLE, dlg);
            }
        });

        window.findViewById(R.id.rlwb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInfotoDB(activity, model, context, SHARE_MEDIA.SINA, dlg);
            }
        });

        window.findViewById(R.id.rlcopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(Config.HOST + "h5/card.html?aid=" + user.getUid() + "&uid=" + App.getPreferencesValue("uid").toString(), context);
                Toast.makeText(context, "链接复制成功", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

    }

    private static void shareInfotoDB(final Activity activity, final ShareModel model, final Context context, final SHARE_MEDIA platform, final AlertDialog dlg) {
        CommonHelper.shareInfo(platform, "1", user.getUid(), new ApiCallback<Share>() {
            @Override
            public void onSuccess(Share share) {
                if (null != share) {
                    UMImage image = new UMImage(activity, share.getCover());
                    model.setTitle(share.getTitle());
                    model.setTargetUrl(share.getUrl());
                    model.setImageMedia(image);
                    model.setContent(share.getTitle());

                    if (platform.equals(SHARE_MEDIA.QZONE)) {

                        CommonHelper.share(activity, model, SHARE_MEDIA.QZONE, 1, null);
                    } else if (platform.equals(SHARE_MEDIA.QQ)) {
                        CommonHelper.share(activity, model, SHARE_MEDIA.QQ, 1, null);

                    } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                        CommonHelper.share(activity, model, SHARE_MEDIA.WEIXIN, 1, null);

                    } else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {

                        CommonHelper.share(activity, model, SHARE_MEDIA.WEIXIN_CIRCLE, 1, null);

                    } else if (platform.equals(SHARE_MEDIA.SINA)) {
                        CommonHelper.share(activity, model, SHARE_MEDIA.SINA, 1, null);
                    }
                    if (dlg.isShowing())
                        dlg.dismiss();

                } else {
                    Toast.makeText(context, "获取分享信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * sj info
     *
     * @param context
     */
    public void showRegAlert(final Context context) {

        try {

            final AlertDialog dlg = new AlertDialog.Builder(context).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
            window.setContentView(R.layout.res_list_inner);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.9f;
            window.setAttributes(lp);
            TextView tvSave = (TextView) window.findViewById(R.id.tvSave);

            final MySeekBar ms_height = (MySeekBar) window.findViewById(R.id.ms_height);
            final MySeekBar ms_weight = (MySeekBar) window.findViewById(R.id.ms_weight);
            final MySeekBar ms_hip = (MySeekBar) window.findViewById(R.id.ms_hip);
            final MySeekBar ms_waist = (MySeekBar) window.findViewById(R.id.ms_waist);
            final MySeekBar ms_bust = (MySeekBar) window.findViewById(R.id.ms_bust);

            if (user != null) {
                if (!TextUtils.isEmpty(user.getHeight()))
                    ms_height.SetProcess(Integer.parseInt(user.getHeight()));

                if (!TextUtils.isEmpty(user.getWeight()))
                    ms_weight.SetProcess(Integer.parseInt(user.getWeight()));

                if (!TextUtils.isEmpty(user.getHip()))
                    ms_hip.SetProcess(Integer.parseInt(user.getHip()));

                if (!TextUtils.isEmpty(user.getWaist()))
                    ms_waist.SetProcess(Integer.parseInt(user.getWaist()));

                if (!TextUtils.isEmpty(user.getBust()))
                    ms_bust.SetProcess(Integer.parseInt(user.getBust()));
            }
            tvSave.setClickable(true);
            tvSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    user.setNickName(App.getPreferencesValue("nickname"));
                    user.setUid(App.getPreferencesValue("uid"));
                    user.setFansCount(App.getPreferencesValue("fans"));
                    user.setHotValue(App.getPreferencesValue("hots"));
                    user.setHeight(ms_height.getProcess());
                    user.setBust(ms_bust.getProcess());
                    user.setHip(ms_hip.getProcess());
                    user.setWeight(ms_weight.getProcess());
                    user.setWaist(ms_waist.getProcess());
                    App.saveBodyInfo(user);
                    bindLeftInfo(user);
                    dlg.dismiss();
                }
            });
        } catch (Exception e) {

            // Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

        }

    }

    private void resetOnMertoItemViewListener() {

        drop_index = false;

    }


    private void startOnMertoItemViewListener() {

        drop_index = true;

    }


    private void ArtistCardInfoStatus() {
        cp = ColaProgress.show(RegPicListActivity.this, "正在加载数据...", true, false, null);
        String token = null == App.getLoginUser() ? "" : App.getLoginUser().getToken();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("uid", user.getUid());
        paramsMap.put("token", token);

        RequestUtils.sendPostRequest(Api.GET_USER_ARTISTCARD_STATUS, paramsMap, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {

                    if (data.getStatus().equals("1")) //status
                    {
                        txtSave.setVisibility(View.GONE);
                        txtUpload.setText("编辑");
                        ll_per_info.setTag("2");
                        txtUpload.setTag("2");
                        //get Info
                        ArtistCardInfo();
                    } else {

                        ll_per_info.setTag("1");
                        if (cp != null && cp.isShowing())
                            cp.dismiss();
                        mertoBeans.clear();
                        CommonUtil.setGuidImage(RegPicListActivity.this, R.id.r1, R.drawable.click_pic, "first1", new ApiCallback() {

                            @Override
                            public void onSuccess(Object data) {
                                if (data.equals("no")) {
                                } else {
                                    CommonUtil.setGuidImage(RegPicListActivity.this, R.id.r1, R.drawable.drop_pic, "first2", new ApiCallback() {

                                        @Override
                                        public void onSuccess(Object data) {
                                        }
                                    });
                                }
                            }
                        });


                    }
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, User.class);
    }

    ColaProgress cp;

    private void ArtistCardInfoSave() {

        boolean isUploadAll = false;
        cp = ColaProgress.show(RegPicListActivity.this, "正在保存...", true, false, null);
        ArrayList<String> imgurl = new ArrayList<String>();
        ArrayList<String> imgUpTag = new ArrayList<String>();
        ArrayList<String> imgUp = new ArrayList<String>();
        String loc = "";
        try {
            for (DropImageView iv : mertoItemViews) {
                if (iv.getIsFinish() == 15) {
                    bm = BitmapHelper.readBitMap(new File(iv.getLocUrl()), false); //267,213
                    bm = BitmapHelper.createScaledBitmap(bm, iv.getWidth(), iv.getHeight(), "CROP");
                    loc = WriteFileImgLoc(BitmapHelper.resizeBitmap(bm, iv.getWidth(), iv.getHeight()), Integer.parseInt(iv.getTag().toString()));
                    imgUp.add(loc);
                    imgUpTag.add(iv.getTag().toString());

                }
                if (iv.getIsFinish() == 55) {
                    isUploadAll = true;

                }
                imgurl.add(loc);
            }
        } catch (Exception ex) {

        }
        App.saveBodyLocPic(user);
        final List<String> pics = new ArrayList<String>();

        if (isUploadAll) {
            upEveryPhoto(imgurl.get(0), imgurl, pics, imgUpTag);
        } else {

            for (int index = 0; index < imgUp.size(); index++) {
                if (imgUpTag.get(index).equals("0")) {
                    user.setImgLoc1(imgUp.get(index));
                } else if (imgUpTag.get(index).equals("1")) {
                    user.setImgLoc2(imgUp.get(index));
                } else if (imgUpTag.get(index).equals("2")) {
                    user.setImgLoc3(imgUp.get(index));
                } else if (imgUpTag.get(index).equals("3")) {
                    user.setImgLoc4(imgUp.get(index));
                } else if (imgUpTag.get(index).equals("4")) {
                    user.setImgLoc5(imgUp.get(index));
                }
            }
            if (imgUp.size() == 0) {
                sendDB(imgUp, imgUpTag);
            } else {
                upEveryPhoto(imgUp.get(0), imgUp, pics, imgUpTag);
            }
        }
    }

    private void sendDB(final List<String> pics, final List<String> imgUpTag) {

        for (int index = 0; index < pics.size(); index++) {
            if (imgUpTag.get(index).equals("0")) {
                user.setImg1(pics.get(index));
            } else if (imgUpTag.get(index).equals("1")) {
                user.setImg2(pics.get(index));
            } else if (imgUpTag.get(index).equals("2")) {
                user.setImg3(pics.get(index));
            } else if (imgUpTag.get(index).equals("3")) {
                user.setImg4(pics.get(index));
            } else if (imgUpTag.get(index).equals("4")) {
                user.setImg5(pics.get(index));
            }
        }

        Map<String, String> paramsMap = new HashMap<>();

        App.saveBodyPic(user);
        paramsMap.put("bust", user.getBust());
        paramsMap.put("waist", user.getWaist());
        paramsMap.put("hip", user.getHip());
        paramsMap.put("height", user.getHeight());
        paramsMap.put("weight", user.getWeight());
        paramsMap.put("Img1", user.getImg1());
        paramsMap.put("Img2", user.getImg2());
        paramsMap.put("Img3", user.getImg3());
        paramsMap.put("Img4", user.getImg4());
        paramsMap.put("Img5", user.getImg5());

        RequestUtils.sendPostRequest(Api.SET_ARTIST_CARD_INFO, paramsMap, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {

                for (DropImageView iv : mertoItemViews) {
                    iv.setIsFinish(5);
                }
                saveSuccessfully();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, String.class);
    }

    private void saveSuccessfully() {
        resetOnMertoItemViewListener();
        cp.dismiss();
        txtSave.setVisibility(View.GONE);
        ll_per_info.setTag("2");
        txtUpload.setText("编辑");
        txtUpload.setTag("2");
        CommonHelper.showTip(RegPicListActivity.this, "保存成功");
    }

    private void upEveryPhoto(final String path, final ArrayList<String> imgurl, final List<String> pics, final ArrayList<String> imgUpTag) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        ImageUtils.upFileInfo(params, new ApiCallback<Upfeile>() {
            @Override
            public void onSuccess(Upfeile data) {
                ImageUtils.upFile(RegPicListActivity.this, data, path, new ApiCallback<FileInfo>() {
                    @Override
                    public void onSuccess(FileInfo file) {
                        pics.add(file.url);
                        if (pics.size() == imgurl.size()) {
                            sendDB(pics, imgUpTag);
                        } else {
                            upEveryPhoto(imgurl.get(pics.size()), imgurl, pics, imgUpTag);
                        }
                    }
                });
            }
        });
    }

    //get artist card info
    private void ArtistCardInfo() {
        String token = null == App.getLoginUser() ? "" : App.getLoginUser().getToken();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("uid", user.getUid());
        paramsMap.put("token", token);

        RequestUtils.sendPostRequest(Api.GET_USER_ARTISTCARD, paramsMap, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {
                    user = data;
                    try {
                        ArrayList<Object> ss1 = new ArrayList<Object>();
                        ss1.add(data.getImg1());
                        ss1.add(data.getImg2());
                        ss1.add(data.getImg3());
                        ss1.add(data.getImg4());
                        ss1.add(data.getImg5());
                        //bind info the left
                        bindLeftInfo(data);
                        App.saveBodyInfos(data);

                        //compare local pic
                        if (!compareWithLocalPic(data) || isExistsFile()) {
                            App.saveBodyPic(data);

                            ArrayList<Object> ss2 = new ArrayList<Object>();
                            DownloadPic(ss1, ss2);
                        } else {

                            Bitmap bm1 = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc1")), false);
//                          Bitmap bm1 = BitmapHelper.copressImage(App.getPreferencesValue("imgloc1"));
                            BitmapDrawable bd1 = new BitmapDrawable(bm1);


                            Bitmap bm2 = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc2")), false);
                            BitmapDrawable bd2 = new BitmapDrawable(bm2);


                            Bitmap bm3 = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc3")), false);
                            BitmapDrawable bd3 = new BitmapDrawable(bm3);


                            Bitmap bm4 = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc4")), false);
                            BitmapDrawable bd4 = new BitmapDrawable(bm4);


                            Bitmap bm5 = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc5")), false);
                            BitmapDrawable bd5 = new BitmapDrawable(bm5);

                            user.setImgLoc1(App.getPreferencesValue("imgloc1"));

                            user.setImgLoc2(App.getPreferencesValue("imgloc2"));

                            user.setImgLoc3(App.getPreferencesValue("imgloc3"));

                            user.setImgLoc4(App.getPreferencesValue("imgloc4"));

                            user.setImgLoc5(App.getPreferencesValue("imgloc5"));


                            user.setBitmapImg1(bm1);

                            user.setBitmapImg2(bm2);

                            user.setBitmapImg3(bm3);

                            user.setBitmapImg4(bm4);

                            user.setBitmapImg5(bm5);

                            user.setDrawableImg1(bd1);

                            user.setDrawableImg2(bd2);

                            user.setDrawableImg3(bd3);

                            user.setDrawableImg4(bd4);

                            user.setDrawableImg5(bd5);
                            initData(1);
                            mertoBeans.clear();
                        }

                    } catch (Exception ex) {
                    }
                } else {
                    user = new User();
                    mertoBeans.clear();
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, User.class);

    }

    private boolean isExistsFile() {

        if (!new File(App.getPreferencesValue("imgloc1")).exists() || !new File(App.getPreferencesValue("imgloc2")).exists() || !new File(App.getPreferencesValue("imgloc3")).exists() && !new File(App.getPreferencesValue("imgloc4")).exists() || !new File(App.getPreferencesValue("imgloc5")).exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void bindLeftInfo(User user) {
        txtFansi.setText(user.getFansCount() == null ? "0 :粉丝" : user.getFansCount() + " :粉丝");
        txtName.setText(user.getNickName() == null ? "" : user.getNickName());
        txtWeight.setText(user.getWeight() == null ? "" : user.getWeight() + " :kg");
        txtHeight.setText(user.getHeight() == null ? "" : user.getHeight() + " :cm");
        txtSj.setText(user.getHotValue() == null ? "0 :身价" : user.getHotValue() + " :身价");
        tvLid.setText(user.getUid() == null ? "回响号: 0" : "回响号: " + user.getUid());
        tvShareId.setText(user.getLoves() == null ? "0 :分享" : user.getLoves() + " :分享");
        String yw = user.getHip() + "-" + user.getWaist() + "-" + user.getBust();
        tvYW.setText(yw);
    }

    private boolean compareWithLocalPic(User data) {

        if (null != App.getPreferencesValue("img1") && null != App.getPreferencesValue("imgloc1")) {
            if (App.getPreferencesValue("img1").equals(data.getImg1()) && App.getPreferencesValue("img2").equals(data.getImg2()) && App.getPreferencesValue("img3").equals(data.getImg3()) && App.getPreferencesValue("img4").equals(data.getImg4()) && App.getPreferencesValue("img5").equals(data.getImg5())) {
                return true;
            }
        }
        return false;
    }

    private void DownloadPic(ArrayList<Object> ss1, ArrayList<Object> ss2) {
        new ProgressThreadPicAsyncTask().execute(ss1, ss2);
    }

    public class ProgressThreadPicAsyncTask extends AsyncTask<ArrayList<Object>, Integer, Bitmap> {
        ArrayList<Object> ss1;
        Bitmap bm;
        ArrayList<Object> ss2 = new ArrayList<>();

        @Override
        protected void onPostExecute(Bitmap result) {
            if (null != result) {
                if (ss2.size() == 5) {

                    try {
                        user.setImgLoc1(WriteFileImgLoc((Bitmap) ss2.get(0), 0));
                        user.setImgLoc2(WriteFileImgLoc((Bitmap) ss2.get(1), 1));
                        user.setImgLoc3(WriteFileImgLoc((Bitmap) ss2.get(2), 2));
                        user.setImgLoc4(WriteFileImgLoc((Bitmap) ss2.get(3), 3));
                        user.setImgLoc5(WriteFileImgLoc((Bitmap) ss2.get(4), 4));
                        App.saveBodyLocPic(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    user.setBitmapImg1((Bitmap) ss2.get(0));

                    user.setBitmapImg2((Bitmap) ss2.get(1));

                    user.setBitmapImg3((Bitmap) ss2.get(2));

                    user.setBitmapImg4((Bitmap) ss2.get(3));

                    user.setBitmapImg5((Bitmap) ss2.get(4));


                    user.setDrawableImg1(new BitmapDrawable((Bitmap) ss2.get(0)));

                    user.setDrawableImg2(new BitmapDrawable((Bitmap) ss2.get(1)));

                    user.setDrawableImg3(new BitmapDrawable((Bitmap) ss2.get(2)));

                    user.setDrawableImg4(new BitmapDrawable((Bitmap) ss2.get(3)));

                    user.setDrawableImg5(new BitmapDrawable((Bitmap) ss2.get(4)));
                    initData(1);
                    mertoBeans.clear();
                } else {
                    new ProgressThreadPicAsyncTask().execute(ss1, ss2);
                }
            }
        }


        private void localConvertDrawable(Bitmap bitmap, int i) {
            user.setDrawableImg1(new BitmapDrawable(bitmap));
            try {
                user.setImg1(WriteFileImgLoc(bitmap, 1));
                user.setImg2(WriteFileImgLoc(bitmap, 2));
                user.setImg3(WriteFileImgLoc(bitmap, 3));
                user.setImg4(WriteFileImgLoc(bitmap, 4));
                user.setImg5(WriteFileImgLoc(bitmap, 5));
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setDrawableImg2(new BitmapDrawable(bitmap));

            user.setDrawableImg3(new BitmapDrawable(bitmap));

            user.setDrawableImg4(new BitmapDrawable(bitmap));

            user.setDrawableImg5(new BitmapDrawable(bitmap));

        }


        @Override
        protected Bitmap doInBackground(ArrayList<Object>... params) {
            try {
                ss2 = params[1];
                ss1 = params[0];
                bm = returnBitMap(params[0].get(ss2.size()).toString());
                ss2.add(bm);
                return bm;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
        }

    }

    private void initData(int status) {

        if (status == 1) {
            imageView1.setImageBitmap(user.getBitmapImg1());
            imageView2.setImageBitmap(user.getBitmapImg2());
            imageView3.setImageBitmap(user.getBitmapImg3());
            imageView4.setImageBitmap(user.getBitmapImg4());
            imageView5.setImageBitmap(user.getBitmapImg5());


        }


        if (status == 0) {

            imageView1.setImageBitmap(BitmapHelper.readBitMap(RegPicListActivity.this, R.drawable.pic1));
            imageView2.setImageBitmap(BitmapHelper.readBitMap(RegPicListActivity.this, R.drawable.pic2));
            imageView3.setImageBitmap(BitmapHelper.readBitMap(RegPicListActivity.this, R.drawable.pic3));
            imageView4.setImageBitmap(BitmapHelper.readBitMap(RegPicListActivity.this, R.drawable.pic4));
            imageView5.setImageBitmap(BitmapHelper.readBitMap(RegPicListActivity.this, R.drawable.pic5));

        }

        imageView1.setmActivity(RegPicListActivity.this);
        imageView2.setmActivity(RegPicListActivity.this);
        imageView3.setmActivity(RegPicListActivity.this);
        imageView4.setmActivity(RegPicListActivity.this);
        imageView5.setmActivity(RegPicListActivity.this);

        if (cp != null) {
            cp.dismiss();
        }

        viewTreeObserver = imageView1.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (state_height == 0) {
                    // 获取状况栏高度
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    state_height = frame.top;
                    imageView1.setScreen_H(window_height - state_height);
                    imageView1.setScreen_W(window_width);
                }

            }
        });



        viewTreeObserver = imageView2.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (state_height == 0) {
                    // 获取状况栏高度
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    state_height = frame.top;
                    imageView2.setScreen_H(window_height - state_height);
                    imageView2.setScreen_W(window_width);
                }

            }
        });


    }

    private int state_height = 0;
    private ViewTreeObserver viewTreeObserver;

    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) myFileUrl.openConnection();

            conn.setDoInput(true);
            conn.connect();

            BitmapFactory.Options opt = new BitmapFactory.Options();
            //opt.inSampleSize =BitmapHelper.calculateInSampleSize(opt,opt.outWidth,opt.outHeight);
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is, null, opt);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
