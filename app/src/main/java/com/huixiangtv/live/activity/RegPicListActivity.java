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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.PhotoAdapter;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.DropImageModel;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.TokenChecker;
import com.huixiangtv.live.utils.widget.DropImageView;
import com.huixiangtv.live.utils.widget.MySeekBar;

import android.widget.FrameLayout.LayoutParams;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Stone on 16/5/30.
 */
public class RegPicListActivity extends Activity {

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

    @ViewInject(R.id.tvH_1)
    TextView tvH_1;
    @ViewInject(R.id.tvH_2)
    TextView tvH_2;
    @ViewInject(R.id.tvH_3)
    TextView tvH_3;
    @ViewInject(R.id.tvH_4)
    TextView tvH_4;
    @ViewInject(R.id.tvH_5)
    TextView tvH_5;

    @ViewInject(R.id.txtOpen)
    TextView txtOpen;

    @ViewInject(R.id.ll_per_info)
    LinearLayout ll_per_info;


    @ViewInject(R.id.imageView1)
    ImageView imageView1;

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


    @ViewInject(R.id.txtSave)
    TextView txtSave;

    @ViewInject(R.id.txtUpload)
    TextView txtUpload;

    @ViewInject(R.id.txtSF)
    TextView txtSF;

    private ArrayList<DropImageModel> mertoBeans = new ArrayList<DropImageModel>();
    private ArrayList<DropImageModel> startBeans;// 保存itme变换前的内容
    private static ArrayList<DropImageView> mertoItemViews;
    private ArrayList<String> UrlLoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_list);
        x.view().inject(this);
        ArtistCardInfoStatus();
        drop_index = true;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imageView1.setTag(0);
        imageView2.setTag(1);
        imageView3.setTag(2);
        imageView4.setTag(3);
        imageView5.setTag(4);

        UrlLoc.add("");
        UrlLoc.add("");
        UrlLoc.add("");
        UrlLoc.add("");
        UrlLoc.add("");
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

        for (int i = 0; i < 5; i++)
            addData();


        txtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.Button);
            }
        });
        showRegAlert(RegPicListActivity.this);


        ll_per_info.getBackground().setAlpha(100);


        ll_per_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset setting

                ll_per_info.setVisibility(View.GONE);
                txtUpload.setVisibility(View.VISIBLE);
                txtSave.setTag("1");
                txtSave.setPadding(10, 10, 10, 10);
                txtSave.setText("保存");
                txtSave.setCompoundDrawables(null, null, null, null);
                txtSave.setBackground(getResources().getDrawable(R.drawable.reg_pic_bg));
                txtOpen.setVisibility(View.VISIBLE);
                startOnMertoItemViewListener();

            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first validation
                if (v.getTag().toString().equals("1")) {
                    for (DropImageView dropImageView : mertoItemViews) {

                        if (!isMoveAll()) {
                            Toast.makeText(getBaseContext(), "您需要选择上传“＋＋”张完整清晰个人照片", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    ll_per_info.setVisibility(View.VISIBLE);
                    txtUpload.setVisibility(View.GONE);
                    txtSF.setVisibility(View.GONE);
                    txtOpen.setVisibility(View.GONE);

                    Drawable nav_up = getResources().getDrawable(R.drawable.txt_share);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    txtSave.setCompoundDrawables(null, null, nav_up, null);
                    txtSave.setText("");
                    txtSave.setTag("0");
                    resetOnMertoItemViewListener();
                    txtSave.setBackground(null);
                } else {

                    showShareAlert(RegPicListActivity.this);
                }
            }
        });


        txtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegAlert(RegPicListActivity.this);
            }
        });

        txtSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DropImageView) imageView2).setIsZoom(false);
            }
        });
        lastClickTime = 0;

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
            // 显示缩放后的图片
            imageView.setImageMatrix(matrix);
            return true;
        }

    }


    /**
     * 计算两点之间的距离
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
     * 计算两点之间的中间点
     *
     * @param event
     * @return
     */
    public static PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }


    private void addData() {
        DropImageModel dropImageModel = new DropImageModel();
        if (mertoBeans.size() == 0) {
            dropImageModel.setIconId(getDrawable(R.drawable.pic1));
            dropImageModel.setName("test1");
            dropImageModel.setIsFinish(0);
        } else if (mertoBeans.size() == 1) {
            dropImageModel.setIconId(getDrawable(R.drawable.pic2));
            dropImageModel.setName("test2");
            dropImageModel.setIsFinish(0);
        } else if (mertoBeans.size() == 2) {
            dropImageModel.setIconId(getDrawable(R.drawable.pic3));
            dropImageModel.setName("test3");
            dropImageModel.setIsFinish(0);
        } else if (mertoBeans.size() == 3) {
            dropImageModel.setIconId(getDrawable(R.drawable.pic4));
            dropImageModel.setName("test4");
            dropImageModel.setIsFinish(0);
        } else if (mertoBeans.size() == 4) {
            dropImageModel.setIconId(getDrawable(R.drawable.pic5));
            dropImageModel.setName("test5");
            dropImageModel.setIsFinish(0);

        } else {
            return;
        }
        mertoBeans.add(dropImageModel);
        setView();
    }


    private void setView() {
        for (int i = 0; i < mertoItemViews.size(); i++) {
            if (mertoBeans.size() > i) {
                if (mertoBeans.size() <= 5) {
                    mertoItemViews.get(i).setVisibility(View.VISIBLE);
                    mertoItemViews.get(i)
                            .setIcon(mertoBeans.get(i).getIconId());
                    mertoItemViews.get(i).setIsFinish(mertoBeans.get(i).getIsFinish());
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

    private boolean isMoveAll() {
        if (!drop_index || tvH_1.getText().toString().trim() == "" || tvH_2.getText().toString().trim() == "" || tvH_3.getText().toString().trim() == "" || tvH_4.getText().toString().trim() == "" || tvH_5.getText().toString().trim() == "" || tvH_1.getText().toString().trim() == "") {
            return false;
        }
        return true;
    }

    boolean isdbClick = false;
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
                        Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
                        PhotoPickerIntent.setPhotoCount(intent, 1);
                        PhotoPickerIntent.setShowCamera(intent, true);
                        startActivityForResult(intent, REQUEST_CODE_ALL);
                    } else {
                        isdbClick = true;
                        Message message=new Message();
                        message.what=1;
                        message.arg1=10;
                        handler.sendMessageDelayed(message, 500);
                    }
                    lastClickTime = System.currentTimeMillis();

                } else {
                    Intent intent = new Intent(RegPicListActivity.this, RegPicActivity.class);
                    intent.putExtra("images", (ArrayList<String>) UrlLoc);
                    intent.putExtra("currentIndex", currentTag);
                    startActivity(intent);
                }

            } catch (Exception ex) {

            }
        }

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

    private void changeData(int x, int y) {
        if (positionView != null) {
            positionView.setVisibility(View.VISIBLE);
        }
        changeTag(x, y);
        if (tag != -1 && moveTag != -1) {
            mertoBeans = new ArrayList<DropImageModel>(startBeans);
            if (mertoBeans.size() <= 5) {
                DropImageModel mertoBean = new DropImageModel(mertoBeans.get(tag));
                mertoBeans.set(tag, mertoBeans.get(moveTag));
                mertoBeans.set(moveTag, mertoBean);
            } else {
                DropImageModel mertoBean = new DropImageModel(mertoBeans.get(tag - 1));
                mertoBeans.set(tag - 1, mertoBeans.get(moveTag - 1));
                mertoBeans.set(moveTag - 1, mertoBean);
            }
            setView();
            positionView.setVisibility(View.INVISIBLE);
        }
    }

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
                PhotoPickerIntent.setColumn(intent, 4);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {

            List<String> photos = null;
            BitmapDrawable bd;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if (requestCode == REQUEST_CODE_ALL) {
                File file = new File(photos.get((0)));
                current_corp_img = file.getAbsolutePath();
                Uri uri = Uri.fromFile(file);
                startPhotoZoom(uri);

            }

            if (requestCode == REQUEST_CODE_CAT) {
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    bm = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //图片名称 时间命名
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    String filename = format.format(date);
                    //创建File对象用于存储拍照的图片 SD卡根目录
                    //File outputImage = new File(Environment.getExternalStorageDirectory(),test.jpg);
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    outputImage = new File(path, "szj_" + filename + ".jpg");

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
                    UrlLoc.set(currentTag, local);
                    OffDrop(currentTag);
                    bd = new BitmapDrawable(bm);
                    mertoItemViews.get(currentTag).setIcon(bd);
                    mertoBeans.get(currentTag).setIconId(bd);
                    mertoBeans.get(currentTag).setIsFinish(5);
                    mertoItemViews.get(currentTag).setIsFinish(5);

                } else {
                    //record first
                    if (current_corp_img.length() != 0) {


                        UrlLoc.set(currentTag, current_corp_img);
                        OffDrop(currentTag);
                        bm = BitmapHelper.copressImage(current_corp_img);
                        bd = new BitmapDrawable(bm);
                        mertoItemViews.get(currentTag).setIcon(bd);
                        mertoBeans.get(currentTag).setIconId(bd);
                        mertoBeans.get(currentTag).setIsFinish(5);
                        mertoItemViews.get(currentTag).setIsFinish(5);
                        current_corp_img = "";
                    }
                }


            } else {

                if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {


                    if (photos.size() > 0 && photos.size() < 2) {

                        UrlLoc.set(currentTag, photos.get(0));
                        OffDrop(currentTag);
                        bm = BitmapHelper.copressImage(photos.get(0));
                        bd = new BitmapDrawable(bm);
                        mertoItemViews.get(currentTag).setIcon(bd);
                        mertoBeans.get(currentTag).setIconId(bd);
                        mertoBeans.get(currentTag).setIsFinish(5);
                        mertoItemViews.get(currentTag).setIsFinish(5);


                    } else if (photos.size() > 1) {
                        for (int size = 0; size < photos.size(); size++) {
                            UrlLoc.set(size, photos.get(size));
                            bm = BitmapHelper.copressImage(photos.get(size));
                            bd = new BitmapDrawable(bm);
                            mertoItemViews.get(size).setIcon(bd);
                            mertoBeans.get(size).setIconId(bd);
                            mertoBeans.get(size).setIsFinish(5);
                            mertoItemViews.get(size).setIsFinish(5);
                            OffDrop(size);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(RegPicListActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();

        }
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

    public void previewPhoto(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
    }


    public static void showShareAlert(final Context context) {

        final AlertDialog dlg = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        dlg.show();
        dlg.setCancelable(false);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.open_share);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.9f;
        window.setAttributes(lp);

        window.findViewById(R.id.rlqq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to qq", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

        window.findViewById(R.id.rlzone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to zone", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

        window.findViewById(R.id.rlwx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to wechat", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });


        window.findViewById(R.id.rlpyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to pyq", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

        window.findViewById(R.id.rlwb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to sina", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

        window.findViewById(R.id.rlcopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share to copy", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });

    }

    public static void showRegAlert(final Context context) {

        try {
            final AlertDialog dlg = new AlertDialog.Builder(context).create();
            dlg.show();
            dlg.setCancelable(false);
            Window window = dlg.getWindow();
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
//
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

            tvSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    User user = new User();
                    user.setHeight(ms_height.getProcess());
                    user.setBust(ms_bust.getProcess());
                    user.setHip(ms_hip.getProcess());
                    user.setWeight(ms_weight.getProcess());
                    user.setWaist(ms_waist.getProcess());
                    App.saveBodyInfo(user);
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
        if (size == 0 && tvH_1.getText().toString().length() <= 0) {
            tvH_1.setText("1");
            ((DropImageView) imageView1).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 1 && tvH_2.getText().toString().length() <= 0) {
            tvH_2.setText("1");
            ((DropImageView) imageView2).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 2 && tvH_3.getText().toString().length() <= 0) {
            tvH_3.setText("1");
            ((DropImageView) imageView3).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 3 && tvH_4.getText().toString().length() <= 0) {
            tvH_4.setText("1");
            ((DropImageView) imageView4).setOnDropImageViewListener(onMertoItemViewListener);
        } else if (size == 4 && tvH_5.getText().toString().length() <= 0) {
            tvH_5.setText("1");
            ((DropImageView) imageView5).setOnDropImageViewListener(onMertoItemViewListener);
        }

    }


    private void ArtistCardInfoStatus() {

        Map<String, String> paramsMap = new HashMap<>();

        String token = App.getPreferencesValue("token");
        String uid = App.getPreferencesValue("uid");
        TokenChecker.checkToken(RegPicListActivity.this);
        paramsMap.put("token", token);
        paramsMap.put("uid", uid);
        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<User>() {
            @Override
            public void onSuccessList(List<User> data) {


                if (data != null && data.size() > 0) {


                    user = data.get(0);

                }


            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, User.class);

    }


    private Handler handler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message arg0) {
            // TODO Auto-generated method stub
            if (arg0.what == 1 && isdbClick) {
                Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
                PhotoPickerIntent.setPhotoCount(intent, 1);
                PhotoPickerIntent.setShowCamera(intent, true);
                startActivityForResult(intent, REQUEST_CODE);
            }
            return false;
        }
    });

}
