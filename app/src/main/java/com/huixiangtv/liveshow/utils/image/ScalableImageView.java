package com.huixiangtv.liveshow.utils.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.utils.BitmapHelper;

import java.io.File;

/**
 * Created by Stone on 16/6/23.
 */
public class ScalableImageView extends ImageView {


    private static final float MIN_POINT_DISTINCT = 10F;
    private Matrix matrix;
    private Matrix cacheMatrix;  //缓存的matrix ，同时记录上一次滑动的位置
    private float mPointDistinct = 1f;
    private float mDegree;
    private float rotate = 0F;// 旋转的角度


    enum Mode {
        NONE, DOWN, MOVE
    }

    private Mode mode; //当前mode
    private Context mContext;
    private int icon_src;
    private PointF mStart = new PointF();
    private PointF mEnd = new PointF();

    public ScalableImageView(Context context) {
        this(context, null);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScalableImageView);
        icon_src = a.getInteger(R.styleable.ScalableImageView_icon_src,1);
        //init(icon_src);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }

    public void init(String url) {
        matrix = new Matrix();
        cacheMatrix = new Matrix();
        mode = Mode.NONE;

        Bitmap bitmap =  BitmapHelper.readBitMap(new File(url), true);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                cacheMatrix.set(matrix); //先拷贝一份到缓存
                mode = Mode.DOWN;
                mStart.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointDistinct = calSpacing(event);
                if (mPointDistinct > MIN_POINT_DISTINCT) {
                    cacheMatrix.set(matrix); //先拷贝一份到缓存
                    calPoint(mEnd, event);
                    mode = Mode.MOVE;
                }
                mDegree = calRotation(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //单点触控的时候
                if (mode == Mode.DOWN) {
                    matrix.set(cacheMatrix);
                    matrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
                } else if (mode == Mode.MOVE && event.getPointerCount() == 2) {  //只能2只手
                    matrix.set(cacheMatrix);
                    float move = calSpacing(event);
                    if (move > MIN_POINT_DISTINCT) {
                        float scale = move / mPointDistinct;
                        matrix.postScale(scale, scale, mEnd.x, mEnd.y);

                    }
                    rotate = calRotation(event);
                    float r = rotate - mDegree;
                    matrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = Mode.NONE;
                break;

        }

        setImageMatrix(matrix);
        return true;
    }


    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void calPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float calRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }

    public void reset() {

        matrix.reset();
        cacheMatrix.reset();
        setImageMatrix(matrix);
        invalidate();
    }


}

