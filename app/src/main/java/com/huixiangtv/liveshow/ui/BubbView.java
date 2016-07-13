package com.huixiangtv.liveshow.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;

/**
 * Created by hjw on 16/5/20.
 */
public class BubbView {

    int baseX = 0;
    int baseY = 0;
    int baseWidth = 0;
    int baseHeight = 0;
    private FrameLayout rootView;

    private Context context;


    private ObjectAnimator animator1;
    private ObjectAnimator animator2;
    private ObjectAnimator animator3;
    private ObjectAnimator animator4;
    private ObjectAnimator animator5;

    private AnimatorSet animSet;


    int[] resources = new int[]{R.mipmap.b1,R.mipmap.b2,R.mipmap.b3,R.mipmap.b5,R.mipmap.b6};

    /**
     * 传进来上边要冒气泡的view
     * @param baseView
     */
    public BubbView(Context context, View baseView, FrameLayout rv,boolean b) {
        this.context = context;
        rootView = rv;
        int[] locations=new int[2];
        baseView.getLocationInWindow(locations);
        baseX = locations[0];
        baseY = locations[1];
        baseWidth = baseView.getWidth();
        baseHeight = baseView.getHeight();

    }

    public void oneBubble(){
        View view = createOneBubbleView();
        initOneAnimator(view);
        startAni(view);
    }

    public void bubble(){
        handler.postDelayed(runnable, 300);
    }



    private View createOneBubbleView() {
        int imageWidth = WidgetUtil.dip2px(context,20);
        int randomWidth = getRandomNum(baseWidth,imageWidth);
        int baseViewMarginImageTop = WidgetUtil.dip2px(context,20);

        ImageView flowerAnim = new ImageView(context);
        rootView.addView(flowerAnim);
        FrameLayout.LayoutParams giftLayout = new FrameLayout.LayoutParams(imageWidth,imageWidth);
        int marginLeft = baseX+randomWidth;
        int marginTop = baseY - baseViewMarginImageTop;
        giftLayout.setMargins(marginLeft, marginTop, 0, 0);
        flowerAnim.setLayoutParams(giftLayout);
        int resourceId = resources[0];
        flowerAnim.setImageResource(resourceId);
        return flowerAnim;
    }


    private void initOneAnimator(View view) {
        animator1 = ObjectAnimator.ofFloat(view, "alpha",0.3f);
        animator2 = ObjectAnimator.ofFloat(view, "scaleX",0.9f,1.2f);
        animator5 = ObjectAnimator.ofFloat(view, "scaleY",0.9f,1.2f);
        setAnimator4(view);
        animator3 = ObjectAnimator.ofFloat(view, "translationY", 0f, -500f);
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, 300);
            startBubble();
        }
    };




    public void startBubble(){
        int num = bubbleNumView();
        for (int i=0;i<num;i++){
            View view = createBubbleView();
            initAnimator(view);
            startAni(view);
        }

    }

    private int bubbleNumView() {
        int index=(int)(Math.random()*2);
        return index;
    }

    private void startAni(final View view) {
        //将前面的动画集合到一起~
        animSet = new AnimatorSet();
        animSet.play(animator4).with(animator3).with(animator2).with(animator1).with(animator5);
        animSet.setDuration(1500l);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(null!=view){
                    rootView.removeView(view);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private View createBubbleView() {
        int imageWidth = WidgetUtil.dip2px(context,20);
        int randomWidth = getRandomNum(baseWidth,imageWidth);
        int baseViewMarginImageTop = WidgetUtil.dip2px(context,20);

        ImageView flowerAnim = new ImageView(context);
        rootView.addView(flowerAnim);
        FrameLayout.LayoutParams giftLayout = new FrameLayout.LayoutParams(imageWidth,imageWidth);
        int marginLeft = baseX+randomWidth;
        int marginTop = baseY - baseViewMarginImageTop;
        giftLayout.setMargins(marginLeft, marginTop, 0, 0);
        flowerAnim.setLayoutParams(giftLayout);
        int resourceId = getResourceId();
        flowerAnim.setImageResource(resourceId);
        return flowerAnim;
    }

    private int getRandomNum(int baseWidth, int imageWidth) {
        int num  = baseWidth - imageWidth;
        java.util.Random random=new java.util.Random();
        int result=random.nextInt(num);
        return result;
    }


    private void initAnimator(View view) {
        animator1 = ObjectAnimator.ofFloat(view, "alpha",0.9f,0.0f);
        animator2 = ObjectAnimator.ofFloat(view, "scaleX",1.1f,1.2f);
        animator5 = ObjectAnimator.ofFloat(view, "scaleY",1.1f,1.2f);
        setAnimator4(view);
        animator3 = ObjectAnimator.ofFloat(view, "translationY", 0f, -1200f);
    }

    private void setAnimator4(View view) {
        int index=(int)(Math.random()*4);
        if(index==1){
            animator4 = ObjectAnimator.ofFloat(view, "translationX",0.f,-5.f,-10.f,-10.f,-20.f,-35.f,-50.f,-70.f,-90.f,-100.f);
        }else if(index==2){
            animator4 = ObjectAnimator.ofFloat(view, "translationX",0.f,5.f,10.f,25.f,40.f,60.f,75.f,90.f,1100.f,120.f);
        }else if(index==3){
            animator4 = ObjectAnimator.ofFloat(view, "translationX",0.f,5.f,10.f,15.f,25.f,40.f,55.f,80.f,95.f,110.f);
        }else{
            animator4 = ObjectAnimator.ofFloat(view, "translationX",-10.f,-20.f,-35.f,-45.f,-60.f,-73.f,-85.f,-90.f,-105.f,-120.f);
        }

    }

    public int getResourceId() {
        int index=(int)(Math.random()*resources.length);
        int resourceId = resources[index];
        return resourceId;
    }

}