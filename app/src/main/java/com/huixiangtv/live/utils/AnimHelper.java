package com.huixiangtv.live.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnimHelper {







    //喊话弹幕
    private final int BARRAGE = 11;
    private final int BARRAGE_FINISH = 12;

    //礼物接收
    private final int GIFT_ANIM = 13;
    private final int GIFT_FINISH =14;






    private Context context;
    private Handler animHandler;


    private final int ANIM_SHOW_MARKS=2;
    private final int ANIM_REMOVE_MARKS=3;



    private static String TAG="com.hunantv.mangolive";


    public AnimHelper(Context context, Handler animHandler) {
        this.context=context;
        this.animHandler = animHandler;
    }




    public static void viewUpToMiddle(View view, int moveY, long duration) {
        view.setVisibility(View.VISIBLE);
        AnimationSet animationUpSet = new AnimationSet(false);
        TranslateAnimation translateUpAnimation = new TranslateAnimation(0,0,moveY,0);
        translateUpAnimation.setDuration(500);
        translateUpAnimation.setFillAfter(true);
        animationUpSet.addAnimation(translateUpAnimation);
        view.startAnimation(animationUpSet);
    }

    public static void viewDownToBottom(View view, int moveY, long duration) {
        view.setVisibility(View.GONE);
        AnimationSet animationDownSet = new AnimationSet(false);
        TranslateAnimation translateDownAnimation = new TranslateAnimation(0,0,0,moveY);
        translateDownAnimation.setDuration(500);
        translateDownAnimation.setFillAfter(true);
        animationDownSet.addAnimation(translateDownAnimation);
        view.startAnimation(animationDownSet);
    }



    /**
     * 送礼物动画
     * @param rootContainer
     * @param baseLocationView
     * @param barrageArea
     * @param gift
     */
    public void showSendGift(ViewGroup rootContainer, View baseLocationView, View barrageArea, Gift gift){

        //自己送的礼物
        float rate =1.5f;
        int[] locations=new int[2];
        baseLocationView.getLocationOnScreen(locations);

        int heightOffset = App.statuBarHeight;



        //x坐标
        float topx = barrageArea.getX();
        //宽度
        int topw = barrageArea.getWidth();
        //y坐标
        float topy = barrageArea.getY();
        //高度
        int toph = barrageArea.getHeight();


        //原始图片宽度
        int oriImageWidth = baseLocationView.getWidth();
        //原始图片高度
        int oriImageHeight = baseLocationView.getHeight();
        int oriImageX = locations[0];
        int oriImageY = locations[1];

        //目标距离左边距离
        float destCenterX = topx + topw / 2;
        //目标距离上边距离
        float destCenterY = topy + toph / 2;


        //目标图片X距离左边的距离
        float toXDelta=destCenterX - oriImageX - oriImageWidth*rate/2;
        float toYDelta=destCenterY - oriImageY-oriImageHeight*rate/2;

        ImageView flowerAnim = new ImageView(context);
        rootContainer.addView(flowerAnim);
        FrameLayout.LayoutParams giftLayout = new FrameLayout.LayoutParams((int)(oriImageWidth*rate), (int)(oriImageHeight*rate));
        int giftAnimLeft = (int)(oriImageX-(rate-1)*oriImageWidth/2);
        int giftAnimTop = (int)(oriImageY-(rate-1)*oriImageWidth/2);
        giftLayout.setMargins(giftAnimLeft, giftAnimTop, 0, 0);
        flowerAnim.setLayoutParams(giftLayout);
        ImageUtils.displayAvator(flowerAnim,gift.getIcon());

        this.showTransiteAnim(flowerAnim, toXDelta, toYDelta, 1.2f, gift,barrageArea);
    }



    /**
     * 展示ImageView移动动画
     * @param animView
     * @param toXDelta
     * @param toYDelta
     * @param scaleRate
     * @param gift
     */
    public void showTransiteAnim(final ImageView animView, float toXDelta, float toYDelta, float scaleRate, final Gift gift, final View barrageArea){
        int duration = 1000;
        AnimationSet animSet = new AnimationSet(false);
        Animation.AnimationListener listener = new LazyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                Message msg = new Message();
                msg.what = ANIM_SHOW_MARKS;
                Map<String,Object> data = new HashMap<String,Object>();
                data.put("animView",animView);
                data.put("remarks",null==gift.getPrice()?0: Long.parseLong(gift.getPrice()));
                data.put("barrageArea",barrageArea);
                msg.obj = data;
                animHandler.sendMessage(msg);
            }
        };

        //移动动画
        TranslateAnimation translateAnim = new TranslateAnimation(0, toXDelta, 0, toYDelta);
        translateAnim.setDuration(duration);
        translateAnim.setRepeatMode(Animation.INFINITE);
        translateAnim.setRepeatCount(0);
        translateAnim.setFillAfter(true);
        translateAnim.setInterpolator(context, android.R.anim.accelerate_decelerate_interpolator);
        translateAnim.setAnimationListener(listener);
        animSet.addAnimation(translateAnim);

        //缩放动画
        ScaleAnimation scaleAnimX = new ScaleAnimation(1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimX.setDuration(duration);
        scaleAnimX.setRepeatMode(Animation.INFINITE);
        scaleAnimX.setRepeatCount(0);
        animSet.addAnimation(scaleAnimX);

        //透明动画
        AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        alphaAnim.setDuration(duration - 200);
        alphaAnim.setRepeatMode(Animation.INFINITE);
        alphaAnim.setRepeatCount(0);
        animSet.addAnimation(alphaAnim);

        //启动动画
        animView.startAnimation(animSet);
    }

    /**
     * 送礼物后在顶部视频区展示的分数(客户端动画)
     * @param rootContainer
     * @param barrageArea
     * @param marks
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showGiftMarksAnim(ViewGroup rootContainer, View barrageArea, Long marks){

        int toph = barrageArea.getHeight();
        float topy = barrageArea.getY();
        float giftDestATop = topy + toph / 2;

        final TextView remarkView = new TextView(context);
        rootContainer.addView(remarkView);
        remarkView.setText("+" + marks);
        remarkView.setTextSize(25);
        remarkView.setTextColor(context.getResources().getColor(R.color.mainColor));
        FrameLayout.LayoutParams tipsLayout =new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        remarkView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsLayout.setMargins(0, (int) giftDestATop - 100, 0, 0);
        remarkView.setLayoutParams(tipsLayout);



        AnimationSet tipsAnimSet = new AnimationSet(false);
        //缩放动画
        ScaleAnimation tipScaleAnimX = new ScaleAnimation(1f, 4f, 1f, 4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        tipScaleAnimX.setDuration(500);
        tipScaleAnimX.setRepeatMode(Animation.INFINITE);
        tipScaleAnimX.setRepeatCount(0);
        tipsAnimSet.setFillAfter(false);
        tipScaleAnimX.setAnimationListener(new LazyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                Message msg = new Message();
                msg.what = ANIM_REMOVE_MARKS;
                Map<String,Object> data = new HashMap<String,Object>();
                data.put("animView",remarkView);
                msg.obj = data;
                animHandler.sendMessage(msg);
            }
        });
        tipsAnimSet.addAnimation(tipScaleAnimX);

        //透明动画
        AlphaAnimation tipsAlphaAnim = new AlphaAnimation(1.0f, 0.0f);
        tipsAlphaAnim.setDuration(500);
        tipsAlphaAnim.setRepeatMode(Animation.INFINITE);
        tipsAlphaAnim.setRepeatCount(0);
        tipsAlphaAnim.setFillAfter(false);
        tipsAnimSet.addAnimation(tipsAlphaAnim);

        remarkView.startAnimation(tipsAnimSet);
    }




    static int barrangeFreRoad = 0;
    public static void showBarrageAni(Activity activity, final FrameLayout flLive, final LiveMsg msg, int road, final Handler liveHandler) {
        int minY = App.screenHeight/3;
        int randomY = 0;
        if(road==1){
            barrangeFreRoad = 2;
            randomY =  App.screenHeight-minY;
        }else if(road==2){
            barrangeFreRoad = 1;
            randomY =  App.screenHeight-(minY+minY/5);
        }

        final View barrageView = View.inflate(activity, R.layout.shout_barrage, null);
        flLive.addView(barrageView);
        TextView tvNickName = (TextView) barrageView.findViewById(R.id.tvNickName);
        TextView tvMsg = (TextView) barrageView.findViewById(R.id.tvMsg);
        ImageView ivPhoto = (ImageView) barrageView.findViewById(R.id.ivPhoto);
        tvMsg.setText(msg.getContent());
        tvNickName.setText(msg.getNickName());
        if(StringUtil.isNotEmpty(msg.getPhoto())){
            ImageUtils.displayAvator(ivPhoto,msg.getPhoto());
        }


        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        barrageView.measure(w, h);
        int height = barrageView.getMeasuredHeight();
        int width = barrageView.getMeasuredWidth();



        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = randomY;
        barrageView.setLayoutParams(params);

        ObjectAnimator animator = ObjectAnimator.ofFloat(barrageView, "translationX",App.screenWidth,-width);
        animator.setDuration(10000);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                animator.cancel();
                if(null!=barrageView){
                    flLive.removeView(barrageView);
                }
                Message barrageMsg = new Message();
                barrageMsg.what= 12;
                barrageMsg.obj = barrangeFreRoad;
                liveHandler.sendMessage(barrageMsg);
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

    }




    static int giftFreeRoad = 0;
    public static void showGiftAni(final Activity activity, final FrameLayout flLive, final List<LiveMsg> ls, final Handler liveHandler, int road) {
        final LiveMsg msg = ls.get(0);
        int randomY = 0;
        if(road==1){
            giftFreeRoad = 2;
            randomY =  App.screenHeight/2+WidgetUtil.dip2px(activity,25);
        }else if(road==2){
            giftFreeRoad = 1;
            randomY =  App.screenHeight/2-WidgetUtil.dip2px(activity,25);
        }


        final View barrageView = View.inflate(activity, R.layout.gift_barrage, null);
        flLive.addView(barrageView);
        TextView tvNickName = (TextView) barrageView.findViewById(R.id.tvNickName);
        TextView tvMsg = (TextView) barrageView.findViewById(R.id.tvMsg);
        final ImageView ivPhoto = (ImageView) barrageView.findViewById(R.id.ivPhoto);
        final ImageView ivGift = (ImageView) barrageView.findViewById(R.id.ivGift);


        tvNickName.setText(msg.getNickName());
        if(StringUtil.isNotEmpty(msg.getPhoto())){
            ImageUtils.displayAvator(ivPhoto,msg.getPhoto());
        }
        if(null!=App.giftMap){
            if(null!=App.giftMap.get(msg.getGid())){
                ImageUtils.displayAvator(ivGift,App.giftMap.get(msg.getGid()).getIcon());
                tvMsg.setText(msg.getNickName()+"打赏了"+App.giftMap.get(msg.getGid()).getName());

            }

        }else{
            App.loadFreeGiftList(new ApiCallback() {
                @Override
                public void onSuccess(Object data) {
                    if(null!=App.giftMap.get(msg.getGid())){
                        ImageUtils.displayAvator(ivGift,App.giftMap.get(msg.getGid()).getIcon());
                    }

                }
            });
        }


        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        barrageView.measure(w, h);
        final int height = barrageView.getMeasuredHeight();
        final int width = barrageView.getMeasuredWidth();

        int startX = -width;
        final int offsetX = WidgetUtil.dip2px(activity,10);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = randomY;
        barrageView.setLayoutParams(params);

        ObjectAnimator animator = ObjectAnimator.ofFloat(barrageView, "translationX",startX,offsetX);
        animator.setDuration(600);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();


        final int numX = width;
        final int numY = randomY;
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {

                showGiftCount(numX,numY,flLive,activity,barrageView,ls.size(),1,liveHandler,giftFreeRoad);


            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }



    private static void animGiftNum(Object[] objs) {
        int index = (int)objs[6]+1;
        showGiftCount((int)objs[0],(int)objs[1],(FrameLayout)objs[2],(Activity)objs[3],(View)objs[4],(int)objs[5],index,(Handler)objs[7],(int)objs[8]);
    }

    static AnimatorSet animSet =null;
    private static void showGiftCount(int numX, int numY, final FrameLayout flLive, Activity activity, final View barrageView, final int size, final int index,final Handler liveHandler, final int giftFreeRoad) {

        final Object objs[] = new Object[]{numX,numY,flLive,activity,barrageView,size,index,liveHandler,giftFreeRoad};

        final View numView = View.inflate(activity, R.layout.gift_num, null);
        ImageView iv = (ImageView) numView.findViewById(R.id.ivGift);
        if(index==1){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_1));
        }else if(index==2){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_2));
        }else if(index==3){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_3));
        }else if(index==4){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_4));
        }else if(index==5){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_5));
        }else if(index==6){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_6));
        }else if(index==7){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_7));
        }else if(index==8){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_8));
        }else if(index==9){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_9));
        }else if(index==10){
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.num_10));
        }
        flLive.addView(numView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = numY;
        params.leftMargin = numX;
        numView.setLayoutParams(params);


        numView.setVisibility(View.VISIBLE);
        animSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(numView, "scaleX",1.2f,1.5f,1.0f);
        animator.setDuration(300);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(numView, "scaleY",1.2f,1.5f,1.0f);
        animator2.setDuration(300);


        animSet.play(animator).with(animator2);
        animSet.setInterpolator(new AccelerateInterpolator());
        animSet.start();

        final int finalI =index;

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (null != numView) {
                    if (finalI < size) {
                        flLive.removeView(numView);

                        Message barrageMsg = new Message();
                        barrageMsg.what = 30;
                        barrageMsg.obj = objs;
                        aniHandler.sendMessage(barrageMsg);
                    }else{
                        flLive.removeView(numView);

                    }
                }
                if (finalI == size) {

                    if (null != barrageView) {
                        hideAndRemoveBarrageView(flLive, barrageView);
                    }
                    Message barrageMsg = new Message();
                    barrageMsg.what = 14;
                    barrageMsg.obj = giftFreeRoad;

                    liveHandler.sendMessage(barrageMsg);
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

    private static void hideAndRemoveBarrageView(final FrameLayout flLive, final View barrageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(barrageView, "alpha",1.0f,0.1f);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                flLive.removeView(barrageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }



    private static Handler aniHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 30:
                    Object[] objs = (Object[]) message.obj;
                    animGiftNum(objs);
                    break;
                default:
                    break;
            }
        }
    };




}
