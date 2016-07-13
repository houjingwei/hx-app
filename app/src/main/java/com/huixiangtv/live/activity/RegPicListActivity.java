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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
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
import com.huixiangtv.live.model.Upfile;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.DropImageView;
import com.huixiangtv.live.utils.widget.MySeekBar;
import com.tencent.upload.task.data.FileInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
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

    public static ImageView[] imageViews;
    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_CODE_ALL = 11;
    public final static int REQUEST_CODE_CAT = 12;
    private static User user;
    private Vibrator vibrator;// 长按震动效果
    private boolean isMove = false;// 是否移动
    private int tag = -1;// 要变换图标
    private int moveTag = -1;// 当前移动到的位置
    private DropImageView positionView;// 当前要移动的布局
    private DropImageView moveView;// 移动中的布局
    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMaritx = new Matrix();
    private ArrayList<String> UrlLoc = new ArrayList<String>();
    private static int currentTag = 0;
    private Bitmap bm;
    private File path;
    static boolean drop_index = false;
    static String current_corp_img = "";
    private FileOutputStream fos = null;
    private File outputImage = null;
    private int mode = 0; // 用于标记模式
    private static final int DRAG = 1; // 拖动
    private static final int ZOOM = 2; // 放大
    private float startDis = 0;
    private PointF midPoint; // 中心点
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
    ImageView imageView1;


    @ViewInject(R.id.tvShareId)
    TextView tvShareId;

    @ViewInject(R.id.imageView2)
    ImageView imageView2;

    @ViewInject(R.id.imageView3)
    ImageView imageView3;

    @ViewInject(R.id.imageView4)
    ImageView imageView4;

    @ViewInject(R.id.imageView5)
    ImageView imageView5;

    @ViewInject(R.id.merto_content)
    FrameLayout merto_content;

    @ViewInject(R.id.txtssj)
    TextView txtssj;

    @ViewInject(R.id.txtEdit)
    TextView txtEdit;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = RegPicListActivity.this.getWindow();
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_pic_list);
        x.view().inject(this);
        startOnMertoItemViewListener();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imageView1.setTag(0);
        imageView2.setTag(1);
        imageView3.setTag(2);
        imageView4.setTag(3);
        imageView5.setTag(4);
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
        mertoItemViews = new ArrayList<DropImageView>();
        mertoItemViews.add((DropImageView) imageView1);
        mertoItemViews.add((DropImageView) imageView2);
        mertoItemViews.add((DropImageView) imageView3);
        mertoItemViews.add((DropImageView) imageView4);
        mertoItemViews.add((DropImageView) imageView5);

        ((DropImageView) imageView1).setOnDropImageViewListener(onMertoItemViewListener);
        ((DropImageView) imageView2).setOnDropImageViewListener(onMertoItemViewListener);
        ((DropImageView) imageView3).setOnDropImageViewListener(onMertoItemViewListener);
        ((DropImageView) imageView4).setOnDropImageViewListener(onMertoItemViewListener);
        ((DropImageView) imageView5).setOnDropImageViewListener(onMertoItemViewListener);

        initImageView("2");

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


        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOnMertoItemViewListener();
                txtSave.setVisibility(View.VISIBLE);
                txtEdit.setVisibility(View.GONE);
                txtShare.setVisibility(View.GONE);
                txtssj.setVisibility(View.VISIBLE);
                txtUpload.setVisibility(View.VISIBLE);
            }
        });

        ArtistCardInfoStatus();

        ll_per_info.getBackground().setAlpha(150);



        ll_per_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


        txtssj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegAlert(RegPicListActivity.this);
            }
        });
        lastClickTime = 0;
    }

    private void allEdit() {
        user.setUid(App.getPreferencesValue("uid"));
        txtSave.setVisibility(View.GONE);
        txtssj.setVisibility(View.GONE);
        txtUpload.setVisibility(View.GONE);
        txtShare.setVisibility(View.VISIBLE);
        txtEdit.setVisibility(View.VISIBLE);
    }

    private void noEdit(String uid) {
        txtUpload.setVisibility(View.GONE);
        //ll_per_info.setTag("2");
        txtSave.setVisibility(View.GONE);
        txtEdit.setVisibility(View.VISIBLE);
        txtShare.setVisibility(View.VISIBLE);
        user.setUid(uid);
    }

    private boolean isMoveAll() {

        if (!drop_index) {
            return false;
        }
        for (DropImageView dropImageView : mertoItemViews) {

            if (dropImageView.getIsFinish() < 5) {
                return false;
            }
        }
        return true;
    }

    private class TouchListener implements View.OnTouchListener {

        private ImageView imageView;

        public TouchListener(final ImageView imageView) {
            this.imageView = imageView;
        }

        private PointF startPoint = new PointF();
        private Matrix matrix = new Matrix();
        private Matrix currentMaritx = new Matrix();
        private int mode = 0; // 用于标记模式
        private static final int DRAG = 1; // 拖动
        private static final int ZOOM = 2; // 放大
        private float startDis = 0;
        private PointF midPoint; // 中心点

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = DRAG; // 拖拽
                    currentMaritx.set(imageView.getImageMatrix()); // 记录ImageView当前移动位置
                    startPoint.set(event.getX(), event.getY()); // 开始点
                    break;
                case MotionEvent.ACTION_MOVE:// 移动事件
                    if (mode == DRAG) { // 图片拖动事件
                        float dx = event.getX() - startPoint.x; // x轴移动距离
                        float dy = event.getY() - startPoint.y;
                        matrix.set(currentMaritx); // 在当前的位置基础上移动
                        matrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) { // 图片放大事件
                        float endDis = distance(event); // 结束距离
                        if (endDis > 10f) {
                            float scale = endDis / startDis; // 放大倍数
                            matrix.set(currentMaritx);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mode = 0;
                    break;
                // 有手指离开屏幕，但屏幕还有触点（手指）
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点（手指），再有一个手指压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = ZOOM;
                    startDis = distance(event);
                    if (startDis > 10f) { // 避免手指上有两个
                        midPoint = mid(event);
                        currentMaritx.set(imageView.getImageMatrix()); // 记录当前的缩放倍数
                    }
                    return false;
            }
            // Display scaled image
            imageView.setImageMatrix(matrix);
            return true;
        }

    }


    /**
     * Calculate the distance between two points
     *
     * @param event
     * @return
     */
    public static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Intermediate point between the two points is calculated
     *
     * @param event
     * @return
     */
    public static PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }


    /**
     * 5 is Finished
     * 0 is Init
     * locUrl is location image
     * IconId is location Drawable
     * Status 2 is init
     * Status 1 have setting
     * Status 0 is don't setting
     *
     * @param status
     */
    private void addData(String status) {
        DropImageModel dropImageModel = new DropImageModel();
        if (mertoBeans.size() == 0) {
            if (status.equals("1")) {
                dropImageModel.setIsFinish(5);
                dropImageModel.setLocUrl(user.getImgLoc1());
            } else {
                dropImageModel.setIsFinish(0);
            }
            dropImageModel.setIconId(user.getDrawableImg1() != null ? user.getDrawableImg1() : getResources().getDrawable(R.drawable.pic1));

        } else if (mertoBeans.size() == 1) {

            if (status.equals("1")) {
                dropImageModel.setIsFinish(5);
                dropImageModel.setLocUrl(user.getImgLoc2());
            } else {
                dropImageModel.setIsFinish(0);
            }
            dropImageModel.setIconId(user.getDrawableImg2() != null ? user.getDrawableImg2() : getResources().getDrawable(R.drawable.pic2));

        } else if (mertoBeans.size() == 2) {
            if (status.equals("1")) {
                dropImageModel.setIsFinish(5);
                dropImageModel.setLocUrl(user.getImgLoc3());
            } else {
                dropImageModel.setIsFinish(0);
            }

            dropImageModel.setIconId(user.getDrawableImg3() != null ? user.getDrawableImg3() : getResources().getDrawable(R.drawable.pic3));

        } else if (mertoBeans.size() == 3) {

            if (status.equals("1")) {
                dropImageModel.setIsFinish(5);
                dropImageModel.setLocUrl(user.getImgLoc4());
            } else {
                dropImageModel.setIsFinish(0);
            }
            dropImageModel.setIconId(user.getDrawableImg4() != null ? user.getDrawableImg4() : getResources().getDrawable(R.drawable.pic4));
        } else if (mertoBeans.size() == 4) {
            dropImageModel.setIconId(user.getDrawableImg5() != null ? user.getDrawableImg5() : getResources().getDrawable(R.drawable.pic5));
            //have' a tag
            if (status.equals("1")) {
                dropImageModel.setIsFinish(5);
                dropImageModel.setLocUrl(user.getImgLoc5());
                startOnMertoItemViewListener();
                resetOnMertoItemViewListener();
            } else if (status.equals("0")) {
                dropImageModel.setIsFinish(0);
                showRegAlert(RegPicListActivity.this);
            } else {
                dropImageModel.setIsFinish(0);
            }

            if (cp != null && cp.isShowing()) {
                ll_per_info.setVisibility(View.VISIBLE);
                cp.dismiss();
            }
        } else {
            return;
        }
        mertoBeans.add(dropImageModel);
        setView();


    }

    /**
     * init setting view
     */
    private void setView() {
        for (int i = 0; i < mertoItemViews.size(); i++) {
            if (mertoBeans.size() > i) {
                if (mertoBeans.size() <= 5) {
                    mertoItemViews.get(i).setVisibility(View.VISIBLE);
                    mertoItemViews.get(i)
                            .setIcon(mertoBeans.get(i).getIconId());
                    mertoItemViews.get(i).setIsFinish(mertoBeans.get(i).getIsFinish());
                    mertoItemViews.get(i).setLocUrl(mertoBeans.get(i).getLocUrl());
                    mertoItemViews.get(i).setText(mertoBeans.get(i).getName());
                }
            } else {
                if (mertoBeans.size() < 5) {
                    mertoItemViews.get(i).setVisibility(View.GONE);
                } else {
                    if (i < 12) {
                        mertoItemViews.get(i + 1).setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    boolean isdbClick = false;
    /**
     * drop imageview listener
     */
    private DropImageView.DropImageViewListener onMertoItemViewListener = new DropImageView.DropImageViewListener() {

        @Override
        public void onClick(DropImageView v) {
            try {
                currentTag = Integer.parseInt(v.getTag().toString());
                if (drop_index) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < 500) {
                        isdbClick = false;
                        currentTag = Integer.parseInt(v.getTag().toString());


//                        Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
//                        PhotoPickerIntent.setPhotoCount(intent, 1);
//                        PhotoPickerIntent.setShowCamera(intent, true);
//                        startActivityForResult(intent, REQUEST_CODE_ALL);
                        //PhotoPickerIntent.setPhotoCount(intent, 1);
                        //PhotoPickerIntent.setShowCamera(intent, true);

                        Intent intent = new Intent(RegPicListActivity.this, CropImageUI.class);
                        intent.putExtra("currentTag", currentTag);
                        if (currentTag != 0) {
                            intent.putExtra("width", 267);//267  213
                            intent.putExtra("height", 213);
                        } else {
                            intent.putExtra("width", v.getWidth());//267  213
                            intent.putExtra("height", v.getHeight());
                        }
                        startActivityForResult(intent, REQUEST_CODE_CAT);


                    } else {
                        isdbClick = true;
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = 10;
                        handler.sendMessageDelayed(message, 500);
                    }
                    lastClickTime = System.currentTimeMillis();

                } else {

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < 500) {
                        isdbClick = false;
                    } else {
                        isdbClick = true;
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = 10;
                        handlerTow.sendMessageDelayed(message, 500);

                    }
                    lastClickTime = System.currentTimeMillis();
                }

            } catch (Exception ex) {

            }
        }

        /**
         * move object
         * @param v
         * @param e1
         * @param e2
         * @return
         */
        @Override
        public boolean onMove(DropImageView v, MotionEvent e1, MotionEvent e2) {
            if (!isMove) {
                return false;
            }

            int[] moveLocation = new int[2];
            v.getLocationOnScreen(moveLocation);
            if (moveView == null) {
                tag = (Integer) v.getTag();
                startBeans = new ArrayList<DropImageModel>(mertoBeans);
                moveView = new DropImageView(RegPicListActivity.this);
                moveView.setIcon(v.getIcon());
                moveView.setText(v.getText());
                moveView.setIsFinish(v.getIsFinish());
                moveView.setIsFinish(55);
                moveView.setLocUrl(v.getLocUrl());
                moveView.setTextColor(v.getTextColor());
                moveView.setTextSize(v.getTextSize());
                FrameLayout.LayoutParams moveParams = new FrameLayout.LayoutParams(
                        v.getWidth(), v.getHeight());
                moveParams.setMargins((int) (e1.getRawX() - e1.getX()),
                        (int) (e1.getRawY() - e1.getY()), 0, 0);
                moveParams.gravity = Gravity.TOP | Gravity.LEFT;
                moveView.setLayoutParams(moveParams);
                moveView.setBackgroundDrawable(v.getBackground());
                merto_content.addView(moveView);
                moveView.getBackground().setAlpha(200);
            }
            setParams(
                    (int) (e1.getRawX() - e1.getX() + e2.getRawX() - e1
                            .getRawX()),
                    (int) (e1.getRawY() - e1.getY() + e2.getRawY() - e1
                            .getRawY()) - 40);
            changeData((int) e2.getRawX(), (int) e2.getRawY());
            return true;
        }

        @Override
        public void onLongClick(DropImageView v) {
            isMove = isMoveAll();
            if (isMove)
                vibrator.vibrate(100);
        }

        @Override
        public void onUp(DropImageView v) {
            v.setVisibility(View.VISIBLE);
            if (isMove) {
                merto_content.removeView(moveView);
                moveView = null;
                positionView.setVisibility(View.VISIBLE);
                tag = -1;
                moveTag = -1;
                setView();
                isMove = false;
            }
        }
    };

    private void setParams(int left, int top) {
        FrameLayout.LayoutParams moveParams = (LayoutParams) moveView
                .getLayoutParams();
        moveParams.setMargins(left, top, 0, 0);
        moveView.setLayoutParams(moveParams);
    }

    /**
     * change object data for imageview
     *
     * @param x
     * @param y
     */
    private void changeData(int x, int y) {
        if (positionView != null) {
            positionView.setVisibility(View.VISIBLE);
        }
        changeTag(x, y);
        if (tag != -1 && moveTag != -1) {
            mertoBeans = new ArrayList<DropImageModel>(startBeans);
            if (mertoBeans.size() <= 5) {
                DropImageModel mertoBean = new DropImageModel(mertoBeans.get(tag));
                //mertoBean.setIconId(mertoItemViews.get(tag).getIcon());

                mertoBean.setIsFinish(55);


                // mertoBeans.get(moveTag).setIconId(mertoBean.getIconId());


//                mertoBeans.get(moveTag).setIconId(mertoBean.getIconId());


                mertoBeans.set(tag, mertoBeans.get(moveTag));
                mertoBeans.set(moveTag, mertoBean);



            } else {
                DropImageModel mertoBean = new DropImageModel(mertoBeans.get(tag - 1));
                mertoBean.setIsFinish(55);
                mertoBeans.set(tag - 1, mertoBeans.get(moveTag - 1));
                mertoBeans.set(moveTag - 1, mertoBean);
            }
            setView();
            positionView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * change object tag for imageview
     *
     * @param x
     * @param y
     */
    private void changeTag(int x, int y) {
        moveTag = -1;
        for (int i = 0; i < mertoItemViews.size(); i++) {
            int[] location = new int[2];
            mertoItemViews.get(i).getLocationOnScreen(location);
            if (x > location[0]
                    && x < location[0] + mertoItemViews.get(i).getWidth()
                    && y > location[1]
                    && y < location[1] + mertoItemViews.get(i).getHeight()
                    && mertoItemViews.get(i).getVisibility() == View.VISIBLE) {
                moveTag = i;
                positionView = mertoItemViews.get(i);
                return;
            } else {
                moveTag = tag;
                positionView = mertoItemViews.get(tag);
            }
        }
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
                PhotoPickerIntent.setColumn(intent, 6);
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
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
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
                    OffDrop(currentTag);
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
                        OffDrop(currentTag);
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

                        OffDrop(currentTag);
                        bm = BitmapHelper.readBitMap(new File(photos.get(0)), false);
                        bm = BitmapHelper.createScaledBitmap(bm, mertoItemViews.get(currentTag).getWidth(), mertoItemViews.get(currentTag).getHeight(), "CROP");
                        String loc = WriteFileImgLoc(bm, currentTag);
                        bd = new BitmapDrawable(bm);
                        mertoItemViews.get(currentTag).setIcon(bd);
                        mertoBeans.get(currentTag).setIconId(bd);
                        mertoBeans.get(currentTag).setLocUrl(loc);
                        mertoBeans.get(currentTag).setIsFinish(15);
                        mertoItemViews.get(currentTag).setIsFinish(15);
                        mertoItemViews.get(currentTag).setLocUrl(loc);
                    } else if (photos.size() > 1) {
                        for (int size_index = 0; size_index < photos.size(); size_index++) {
                            bm = BitmapHelper.readBitMap(new File(photos.get(size_index)), false);
                            bm = BitmapHelper.createScaledBitmap(bm, mertoItemViews.get(size_index).getWidth(), mertoItemViews.get(size_index).getHeight(), "CROP");
                            String loc = WriteFileImgLoc(bm, size_index);
                            bd = new BitmapDrawable(bm);
                            mertoItemViews.get(size_index).setIcon(bd);
                            mertoBeans.get(size_index).setIconId(bd);
                            mertoBeans.get(size_index).setIsFinish(15);
                            mertoBeans.get(size_index).setLocUrl(loc);
                            mertoItemViews.get(size_index).setIsFinish(15);
                            mertoItemViews.get(size_index).setLocUrl(loc);
                            OffDrop(size_index);
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
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);// (0 -// 100)压缩文件
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
            //window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
            window.setContentView(R.layout.res_list_inner);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.9f;
            //window.setAttributes(lp);
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

    private void OffDrop(int size) {
        if (size == 0) {
            ((DropImageView) imageView1).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 1) {
            ((DropImageView) imageView2).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 2) {
            ((DropImageView) imageView3).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 3) {
            ((DropImageView) imageView4).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 4) {
            ((DropImageView) imageView5).setOnDropImageViewListener(onMertoItemViewListener);
        }

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
                        //get Info
                        ArtistCardInfo();
                    } else {

                        ll_per_info.setTag("1");
                        if (cp != null && cp.isShowing())
                            cp.dismiss();

                        ll_per_info.setVisibility(View.VISIBLE);
                        mertoBeans.clear();
                        CommonUtil.setGuidImage(RegPicListActivity.this, R.id.r1, R.drawable.click_pic, "first1", new ApiCallback() {

                            @Override
                            public void onSuccess(Object data) {
                                if (data.equals("no")) {
                                    initImageView("0");
                                } else {
                                    CommonUtil.setGuidImage(RegPicListActivity.this, R.id.r1, R.drawable.drop_pic, "first2", new ApiCallback() {

                                        @Override
                                        public void onSuccess(Object data) {
                                            initImageView("0");
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

    private void initImageView(String key) {
        for (int i = 0; i < 5; i++)
            addData(key);
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
                    //bm = BitmapHelper.readBitMap(new File(iv.getLocUrl()), false); //267,213
                    //bm = BitmapHelper.createScaledBitmap(bm, iv.getWidth(), iv.getHeight(), "CROP");
                    loc = iv.getLocUrl(); //WriteFileImgLoc(BitmapHelper.resizeBitmap(bm, bm.getWidth(), bm.getHeight()), Integer.parseInt(iv.getTag().toString()));
                    imgUp.add(loc);
                    imgUpTag.add(iv.getTag().toString());

                }
                if (iv.getIsFinish() == 55 )
                    isUploadAll = true;

                //bm = BitmapHelper.readBitMap(new File(iv.getLocUrl()), false); //267,213
                //bm = BitmapHelper.createScaledBitmap(bm, iv.getWidth(), iv.getHeight(), "CROP");
                loc =  iv.getLocUrl(); //WriteFileImgLoc(BitmapHelper.resizeBitmap(bm, bm.getWidth(), bm.getHeight()), Integer.parseInt(iv.getTag().toString()));
                imgurl.add(loc);
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }

        final List<String> pics = new ArrayList<String>();

        if (isUploadAll) {
            resetImgLoc(imgurl);
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
        App.saveBodyLocPic(user);
    }

    private void resetImgLoc(ArrayList<String> imgurl) {

        user.setImgLoc1(imgurl.get(0));

        user.setImgLoc2(imgurl.get(1));

        user.setImgLoc3(imgurl.get(2));

        user.setImgLoc4(imgurl.get(3));

        user.setImgLoc5(imgurl.get(4));
    }

    private void sendDB(final List<String> pics, final List<String> imgUpTag) {

        for (int index = 0; index < pics.size(); index++) {


             if(imgUpTag.size()>0) {
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
            else
             {
                 if(index == 0 )
                 {
                     user.setImg1(pics.get(index));
                 }
                 else if(index == 1)
                 {
                     user.setImg2(pics.get(index));
                 }else if(index == 2)
                 {
                     user.setImg3(pics.get(index));
                 }
                 else  if(index == 3)
                 {
                     user.setImg4(pics.get(index));
                 }
                 else  if(index == 4)
                 {
                     user.setImg5(pics.get(index));
                 }
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
        txtEdit.setVisibility(View.VISIBLE);
        txtssj.setVisibility(View.GONE);
        txtShare.setVisibility(View.VISIBLE);
        txtUpload.setVisibility(View.GONE);
        CommonHelper.showTip(RegPicListActivity.this, "保存成功");
    }

    private void upEveryPhoto(final String path, final ArrayList<String> imgurl, final List<String> pics, final ArrayList<String> imgUpTag) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        ImageUtils.upFileInfo(params, new ApiCallback<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
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

                            user.setDrawableImg1(bd1);

                            user.setDrawableImg2(bd2);

                            user.setDrawableImg3(bd3);

                            user.setDrawableImg4(bd4);

                            user.setDrawableImg5(bd5);

                            mertoBeans.clear();
                            initImageView("1");
                        }

                    } catch (Exception ex) {
                    }
                } else {
                    user = new User();
                    mertoBeans.clear();
                    initImageView("0");
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, User.class);

    }

    private boolean isExistsFile() {

        return !new File(App.getPreferencesValue("imgloc1")).exists() || !new File(App.getPreferencesValue("imgloc2")).exists() || !new File(App.getPreferencesValue("imgloc3")).exists() && !new File(App.getPreferencesValue("imgloc4")).exists() || !new File(App.getPreferencesValue("imgloc5")).exists();
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


                    user.setDrawableImg1(new BitmapDrawable((Bitmap) ss2.get(0)));

                    user.setDrawableImg2(new BitmapDrawable((Bitmap) ss2.get(1)));

                    user.setDrawableImg3(new BitmapDrawable((Bitmap) ss2.get(2)));

                    user.setDrawableImg4(new BitmapDrawable((Bitmap) ss2.get(3)));

                    user.setDrawableImg5(new BitmapDrawable((Bitmap) ss2.get(4)));

                    mertoBeans.clear();
                    initImageView("1");
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

    private Handler handler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message arg0) {
            if (arg0.what == 1 && isdbClick) {
                Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
                PhotoPickerIntent.setPhotoCount(intent, 1);
                PhotoPickerIntent.setColumn(intent,6);
                PhotoPickerIntent.setShowCamera(intent, true);
                startActivityForResult(intent, REQUEST_CODE);
            }
            return false;
        }
    });

    private Handler handlerTow = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message arg0) {
            if (arg0.what == 1 && isdbClick) {
                Intent intent = new Intent(RegPicListActivity.this, RegPicActivity.class);
                intent.putExtra("currentIndex", currentTag);
                startActivity(intent);
            }
            return false;
        }
    });

}