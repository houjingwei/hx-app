package com.huixiangtv.live.utils.widget;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.huixiangtv.live.R;

/**
 * Created by Stone on 16/5/31.
 */
public class DropImageView extends ImageView implements OnTouchListener,
        OnClickListener, OnLongClickListener {

    public interface DropImageViewListener {

        public void onClick(DropImageView v);

        public boolean onMove(DropImageView v, MotionEvent e1, MotionEvent e2);

        public void onLongClick(DropImageView v);

        public void onUp(DropImageView v);

    }

    private DropImageViewListener onDropImageViewListener = new DropImageViewListener() {

        @Override
        public void onClick(DropImageView v) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onMove(DropImageView v, MotionEvent e1, MotionEvent e2) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onLongClick(DropImageView v) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUp(DropImageView v) {
            // TODO Auto-generated method stub

        }

    };

    public void setOnDropImageViewListener(
            DropImageViewListener onDropImageViewListener) {
        this.onDropImageViewListener = onDropImageViewListener;
    }

    private MotionEvent event1;
    private MotionEvent event2;
    private int textColor = Color.WHITE;
    private float textSize = 30;
    private String text = "";
    private Drawable icon;

    public DropImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setOnTouchListener(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @SuppressLint("Recycle")
    public DropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.DropImageView);
        textColor = typedArray.getColor(R.styleable.DropImageView_textDraColor,
                Color.WHITE);
        textSize = typedArray.getDimension(R.styleable.DropImageView_textDraSize,
                30);
        text = typedArray.getString(R.styleable.DropImageView_textDra);
        if (StringIsEmpty(text)) {
            text = "";
        }
        icon = typedArray.getDrawable(R.styleable.DropImageView_iconDra);
        setOnTouchListener(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        invalidate();
    }

    public void setResource(Drawable drawable)
    {
        super.setImageDrawable(drawable);

    }



    public void setIcon(int icon) {
        this.icon = getResources().getDrawable(icon);
        invalidate();
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        StaticLayout layout = null;
        layout = new StaticLayout(text, textPaint, width ,
                Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        if (icon != null) {
            Paint paint = new Paint();
            float iconWidth = 0;
            float iconHeight = 0;
            if (width > height - layout.getHeight()) {
                iconHeight = height;
                iconWidth = width;
            } else {
                iconWidth = width;
                iconHeight = height;
            }
            float iconX = (width - iconWidth) / 2;
            float iconY = (height - layout.getHeight() - iconHeight) / 2;
            Rect iconRect = new Rect((int) iconX, (int) iconY,
                    (int) (iconX + iconWidth), (int) (iconY + iconHeight));
            canvas.translate(0, 0);
            canvas.drawBitmap(((BitmapDrawable) icon).getBitmap(), null,
                    iconRect, paint);
            paint.reset();
        }
        canvas.translate((width - layout.getWidth()) / 2,
                height   );
        layout.draw(canvas);
        textPaint.reset();
    }

    private boolean StringIsEmpty(String string) {
        if (string == null || ("").equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        onDropImageViewListener.onLongClick(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        onDropImageViewListener.onClick(this);
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
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                event1 = MotionEvent.obtain(event);


                mode = DRAG; // 拖拽
                currentMaritx.set(getImageMatrix()); // 记录ImageView当前移动位置
                startPoint.set(event.getX(), event.getY()); // 开始点


                return false;
            case MotionEvent.ACTION_MOVE:
                event2 = MotionEvent.obtain(event);
                Log.i("Log", event1.equals(event2) + "");
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
                return onDropImageViewListener.onMove(this, event1, event2);
            case MotionEvent.ACTION_UP:
                mode = 0;
                onDropImageViewListener.onUp(this);
                return false;
            // 有手指离开屏幕，但屏幕还有触点（手指）
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                return false;
            // 当屏幕上已经有触点（手指），再有一个手指压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                startDis = distance(event);
                if (startDis > 10f) { // 避免手指上有两个
                    midPoint = mid(event);
                    currentMaritx.set(getImageMatrix()); // 记录当前的缩放倍数
                }
                return false;
            default:
                return false;
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

}

