package com.huixiangtv.live.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.PhotoAdapter;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.DropImageModel;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.widget.DropImageView;

import android.widget.FrameLayout.LayoutParams;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

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

    private int mode = 0; // 用于标记模式
    private static final int DRAG = 1; // 拖动
    private static final int ZOOM = 2; // 放大
    private float startDis = 0;
    private PointF midPoint; // 中心点


    PhotoAdapter photoAdapter;

    ArrayList<String> selectedPhotos = new ArrayList<>();

    @ViewInject(R.id.txtUpload)
    TextView txtUpload;

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

    private ArrayList<DropImageModel> mertoBeans = new ArrayList<DropImageModel>();
    private ArrayList<DropImageModel> startBeans;// 保存itme变换前的内容
    private static ArrayList<DropImageView> mertoItemViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_list);
        x.view().inject(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imageView1.setTag(0);
        imageView2.setTag(1);
        imageView3.setTag(2);
        imageView4.setTag(3);
        imageView5.setTag(4);

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
        /**
         * 缩放
         */
//        imageView1.setOnTouchListener(new TouchListener(imageView1));
//        imageView2.setOnTouchListener(new TouchListener(imageView2));
//        imageView3.setOnTouchListener(new TouchListener(imageView3));
//        imageView4.setOnTouchListener(new TouchListener(imageView4));
//        imageView5.setOnTouchListener(new TouchListener(imageView5));


        for (int i = 0; i < 5; i++)
            addData();


        txtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.Button);
            }
        });



        //photoAdapter = new PhotoAdapter(getBaseContext(), selectedPhotos);

        //recycler_view.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        //recycler_view.setAdapter(photoAdapter);

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
                    break;
            }
            // 显示缩放后的图片
            // imageView.setImageMatrix(matrix);
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
            dropImageModel.setIconId(getDrawable(R.drawable.test1));
            dropImageModel.setName("test1");
        } else if (mertoBeans.size() == 1) {
            dropImageModel.setIconId(getDrawable(R.drawable.test1));
            dropImageModel.setName("test2");
        } else if (mertoBeans.size() == 2) {
            dropImageModel.setIconId(getDrawable(R.drawable.test1));
            dropImageModel.setName("test3");
        } else if (mertoBeans.size() == 3) {
            dropImageModel.setIconId(getDrawable(R.drawable.test1));
            dropImageModel.setName("test4");
        } else if (mertoBeans.size() == 4) {
            dropImageModel.setIconId(getDrawable(R.drawable.test1));
            dropImageModel.setName("test5");

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


    private DropImageView.DropImageViewListener onMertoItemViewListener = new DropImageView.DropImageViewListener() {

        @Override
        public void onClick(DropImageView v) {
            try {
                currentTag = Integer.parseInt(v.getTag().toString());
                Intent intent = new Intent(RegPicListActivity.this, PhotoPickerActivity.class);
                PhotoPickerIntent.setPhotoCount(intent, 1);
                PhotoPickerIntent.setShowCamera(intent, true);
                startActivityForResult(intent, REQUEST_CODE);
            }catch (Exception ex)
            {

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
            vibrator.vibrate(100);
            isMove = true;
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

            // Should we show an explanation?
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


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if(photos.size()>0 && photos.size()<2)
            {
                File file = new File(photos.get(currentTag));

                    Bitmap bm = BitmapHelper.zoomImg(BitmapHelper.readBitMap(file), App.screenWidth, App.screenHeight);
                    BitmapDrawable bd= new BitmapDrawable(bm);
                    mertoItemViews.get(currentTag).setIcon(bd); mertoBeans.get(currentTag).setIconId(bd);


            } else
            if(photos.size()>1)
            {
                File file ;
                for(int size = 0; size< photos.size();size++)
                {
                    file = new File(photos.get(size));

                    Bitmap bm = BitmapHelper.zoomImg(BitmapHelper.readBitMap(file), App.screenWidth, App.screenHeight);
                    BitmapDrawable bd= new BitmapDrawable(bm);
                    mertoItemViews.get(size).setIcon(bd);
                    mertoBeans.get(size).setIconId(bd);
                }
            }

            if (photos != null) {

                selectedPhotos.addAll(photos);
            }
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





    public void previewPhoto(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
    }
}
