package com.huixiangtv.live.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
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
import com.huixiangtv.live.message.GiftMessage;
import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.HashMap;
import java.util.Map;


public class AnimHelper {
    private Context context;
    private Handler animHandler;

    private final int ANIM_SHOW_GIFT=1;
    private final int ANIM_SHOW_MARKS=2;
    private final int ANIM_REMOVE_MARKS=3;
    private final int ANIM_SEND_FLOWER=4;
    private final int ANIM_BARRAGE=5;
    private final int ANIM_REMOVE_VIEW=6;

    //弹幕随机颜色
    private String[] barrageColors = {"#801EC9a3","#804ba1ff","#80985eff","#80ff7f00","#8072c049"};

    private static String TAG="com.hunantv.mangolive";

    public AnimHelper(Context context, Handler animHandler) {
        this.context=context;
        this.animHandler = animHandler;
    }

    /**
     * 送礼物动画
     * @param rootContainer
     * @param baseLocationView
     * @param barrageArea
     * @param gift
     */
    public void showSendGift(ViewGroup rootContainer, View baseLocationView, View barrageArea, Gift gift){

//        //缩放率
//        float rate =1.5f;
//
//        int[] locations=new int[2];
//        baseLocationView.getLocationOnScreen(locations);
//        int x = locations[0];
//        int y = locations[1];
//        int width = baseLocationView.getWidth();
//        int height = baseLocationView.getHeight();
//
//        ImageView imageView = new ImageView(context);
//        rootContainer.addView(imageView);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
//        layoutParams.setMargins(x, y, 0, 0);
//        imageView.setLayoutParams(layoutParams);
//        ImageUtils.displayAvator(imageView,gift.getIcon());
//
//
//        int animImageWidth = (int) (width*rate);
//        int animImageHeight = (int) (height*rate);
//
//        float toX = (App.screenWidth - animImageWidth)/2;
//        float toY = (App.screenHeight - animImageHeight)/2;

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
     * 送免费礼物动画(看妹子)
     * @param rootContainer
     * @param baseLocationView
     * @param barrageArea
     * @param gift
     */
    public void showSendFreeGift(ViewGroup rootContainer, View baseLocationView, View barrageArea, Gift gift){
        //自己送的礼物
        float expandRate =1.5f;
        int[] locations=new int[2];
        baseLocationView.getLocationOnScreen(locations);
        int heightOffset = App.statuBarHeight;

        int topw = barrageArea.getWidth();
        int toph = barrageArea.getHeight();
        float topx = barrageArea.getX();
        float topy = barrageArea.getY();

        int[] barrLocations=new int[2];
        barrageArea.getLocationOnScreen(barrLocations);

        int width = baseLocationView.getWidth();
        int height = baseLocationView.getHeight();
        int giftALeft = locations[0];
        int giftATop = locations[1];

        float giftDestALeft = barrLocations[0]*2+topx + topw / 2;
        float giftDestATop = barrLocations[1]+topy + toph / 2;

        float toXDelta=giftDestALeft - giftALeft - width*expandRate /2;
        float toYDelta=giftDestATop - giftATop-heightOffset;

        ImageView flowerAnim = new ImageView(context);
        RelativeLayout.LayoutParams giftLayout = new RelativeLayout.LayoutParams((int)(width*expandRate), (int)(height*expandRate));
        int giftAnimLeft = (int)(giftALeft-(expandRate-1)*width/2);
        int giftAnimTop = (int)(giftATop-(expandRate-1)*height/2);

        giftLayout.setMargins(giftAnimLeft, giftAnimTop, 0, 0);
        flowerAnim.setLayoutParams(giftLayout);
        ImageUtils.displayAvator(flowerAnim,gift.getPhoto());

        rootContainer.addView(flowerAnim);

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
        remarkView.setTextColor(context.getResources().getColor(R.color.orange));
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

    /**
     * 送免费礼物后在顶部视频区展示的分数(客户端动画)
     * @param rootContainer
     * @param barrageArea
     * @param marks
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showFreeGiftMarksAnim(ViewGroup rootContainer, View barrageArea, Long marks){
        if(null==marks||marks<=0){
            return;
        }
        int toph = barrageArea.getHeight();
        float topy = barrageArea.getY();
        int[] barrLocations=new int[2];
        barrageArea.getLocationOnScreen(barrLocations);
        float giftDestATop = barrLocations[1]+topy + toph / 2;

        final TextView remarkView = new TextView(context);
        remarkView.setText("+" + marks);
        remarkView.setTextSize(25);
        remarkView.setTextColor(context.getResources().getColor(R.color.orange));
        RelativeLayout.LayoutParams tipsLayout =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        remarkView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsLayout.setMargins(0, (int) giftDestATop - 100, 0, 0);
        remarkView.setLayoutParams(tipsLayout);
        rootContainer.addView(remarkView);


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

    /**
     * 在顶部播放器处展示弹幕
     * @param rootContainer
     * @param barrageArea
     * @param giftMsg
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showBarrageAnim(RelativeLayout rootContainer, View barrageArea, GiftMessage giftMsg){
        if(null==giftMsg.getProduct()){
            return;
        }

        //初始化弹幕提醒组件
        final View barrageView = View.inflate(context, R.layout.activity_video_gift_barrage, null);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,WidgetUtil.dip2px(context,36));
        layoutParams.setMargins(0, (int) (barrageArea.getY() + WidgetUtil.dip2px(context,50)), 0, 0);
        barrageView.setLayoutParams(layoutParams);

        int randomNum = (int) (Math.random()*barrageColors.length);
        int targetColor= Color.parseColor(barrageColors[randomNum]);
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(targetColor);
        gd.setCornerRadius(WidgetUtil.dip2px(context, 18));
        gd.setStroke(1, targetColor);

        //头像
        ImageView headImg = (ImageView)barrageView.findViewById(R.id.senderIcon);
        ImageUtils.displayAvator(headImg,giftMsg.getAvatar());

        TextView giftBarrageMsg = (TextView)barrageView.findViewById(R.id.giftBarrageMsg);
        giftBarrageMsg.setText(giftMsg.getTip());
        barrageView.setBackground(gd);

        rootContainer.addView(barrageView);

        //提示条-移动动画
        AnimationSet animSet = new AnimationSet(false);
        TranslateAnimation translateAnim =
            new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0.5f,
                    TranslateAnimation.RELATIVE_TO_PARENT,-1f,
                    TranslateAnimation.RELATIVE_TO_SELF,0f,
                    TranslateAnimation.RELATIVE_TO_SELF,0f
            );

        translateAnim.setDuration(5000);
        translateAnim.setRepeatMode(Animation.INFINITE);
        translateAnim.setRepeatCount(0);
        translateAnim.setFillBefore(false);
        translateAnim.setFillAfter(true);
        translateAnim.setInterpolator(context, android.R.anim.linear_interpolator);
        translateAnim.setAnimationListener(new LazyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                //移除组件
                Message msg = new Message();
                msg.what = ANIM_REMOVE_VIEW;
                Map<String,Object> data = new HashMap<String, Object>();
                data.put("targetView",barrageView);
                msg.obj=data;
                animHandler.sendMessage(msg);
            }
        });
        AlphaAnimation alphaAnim = new AlphaAnimation(0.9f,1f);
        alphaAnim.setDuration(300);
        translateAnim.setFillBefore(false);
        translateAnim.setFillAfter(true);

        animSet.addAnimation(alphaAnim);
        animSet.addAnimation(translateAnim);
        animSet.setFillAfter(true);
        barrageView.startAnimation(animSet);

    }

    /**
     * 有礼物送给当前房间的艺人后，在屏幕中间右边出现的提示信息
     * @param rootContainer
     * @param giftMsg
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showGiftRightTips(ViewGroup rootContainer , GiftMessage giftMsg){
        if(null==giftMsg.getProduct() || !"1".equals(giftMsg.getProduct().getAnimType())){
            return;
        }
        //初始化礼物提醒组件
        final View tipsView = View.inflate(context, R.layout.activity_video_gift_tips,null);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(App.screenWidth/2, WidgetUtil.dip2px(context,80));
        layoutParams.setMargins(App.screenWidth/2,App.screenHeight/2-layoutParams.height+200,0,0);
        tipsView.setLayoutParams(layoutParams);

        //送礼物的人名称
        TextView giftTipsSender = (TextView)tipsView.findViewById(R.id.gift_tips_sender);
        giftTipsSender.setText(giftMsg.getNickName());

        //送礼物的人头像
        ImageView headImg = (ImageView)tipsView.findViewById(R.id.senderIcon);
        ImageUtils.displayAvator(headImg,giftMsg.getAvatar());

        //送礼物描述
        TextView giftTipsRemark = (TextView)tipsView.findViewById(R.id.gift_tips_remark);
        giftTipsRemark.setText(giftMsg.getTip());

        //礼物icon
        ImageView giftIcon = (ImageView)tipsView.findViewById(R.id.giftIcon);
        ImageUtils.display(giftIcon,giftMsg.getProduct().getPhoto());

        //礼物数量
        TextView hotsGained = (TextView)tipsView.findViewById(R.id.hotsGained);
        hotsGained.setText("x"+giftMsg.getCount());

        rootContainer.addView(tipsView);

        //提示条-移动动画
        AnimationSet animSet = new AnimationSet(false);
        TranslateAnimation translateAnim =
            new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,1f,
                    TranslateAnimation.RELATIVE_TO_SELF,0f,
                    TranslateAnimation.RELATIVE_TO_SELF,0f,
                    TranslateAnimation.RELATIVE_TO_SELF,0f

            );

        translateAnim.setDuration(300);
        translateAnim.setRepeatMode(Animation.INFINITE);
        translateAnim.setRepeatCount(0);
        translateAnim.setFillBefore(false);
        translateAnim.setFillAfter(true);
        translateAnim.setInterpolator(context, android.R.anim.accelerate_interpolator);
        translateAnim.setAnimationListener(new LazyAnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet disappearSet = new AnimationSet(false);

                //向下移动
                TranslateAnimation disappearTrans =
                        new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0f,
                                TranslateAnimation.RELATIVE_TO_SELF,0f,
                                TranslateAnimation.RELATIVE_TO_SELF,0f,
                                TranslateAnimation.RELATIVE_TO_SELF,1f);
                disappearTrans.setDuration(600);
                disappearTrans.setRepeatMode(Animation.INFINITE);
                disappearTrans.setRepeatCount(0);
                disappearTrans.setFillBefore(false);
                disappearTrans.setFillAfter(true);
                disappearTrans.setInterpolator(context, android.R.anim.decelerate_interpolator);
                disappearSet.addAnimation(disappearTrans);

                //透明动画
                AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0f);
                alphaAnim.setDuration(600);
                alphaAnim.setRepeatMode(Animation.INFINITE);
                alphaAnim.setRepeatCount(0);
                disappearSet.addAnimation(alphaAnim);

                disappearSet.setStartOffset(1000);
                disappearSet.setFillAfter(true);
                disappearSet.setAnimationListener(new LazyAnimationListener(){
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Message msg = new Message();
                        msg.what=ANIM_REMOVE_VIEW;
                        Map<String,Object> data = new HashMap<String, Object>();
                        data.put("targetView",tipsView);
                        msg.obj=data;
                        animHandler.sendMessage(msg);
                    }
                });
                tipsView.startAnimation(disappearSet);


            }
        });
        AlphaAnimation remarkAnim = new AlphaAnimation(0.9f,1f);
        remarkAnim.setDuration(300);
        translateAnim.setFillBefore(false);
        translateAnim.setFillAfter(true);

        animSet.addAnimation(remarkAnim);
        animSet.addAnimation(translateAnim);
        animSet.setFillAfter(true);
        tipsView.startAnimation(animSet);

        //小头像动画
        AlphaAnimation headAnim = new AlphaAnimation(0.2f,1f);
        headAnim.setDuration(600);
        headAnim.setFillBefore(false);
        headAnim.setFillAfter(true);
        headAnim.setStartOffset(200);
        headImg.startAnimation(headAnim);

        //礼物大图动画
        AnimationSet giftIconAnimSet = new AnimationSet(false);
        TranslateAnimation giftAnim =
                new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,1f,
                        TranslateAnimation.RELATIVE_TO_PARENT,0.12f,
                        TranslateAnimation.RELATIVE_TO_SELF,0f,
                        TranslateAnimation.RELATIVE_TO_SELF,0f

                );
        giftAnim.setInterpolator(context, android.R.anim.accelerate_interpolator);
        giftIconAnimSet.addAnimation(giftAnim);

        ScaleAnimation giftScaleAnim  = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.65f);
        giftIconAnimSet.addAnimation(giftScaleAnim);

        giftIconAnimSet.setRepeatMode(Animation.INFINITE);
        giftIconAnimSet.setRepeatCount(0);
        giftIconAnimSet.setDuration(300);
        giftIconAnimSet.setFillBefore(false);
        giftIconAnimSet.setFillAfter(true);
        giftIconAnimSet.setStartOffset(500);
        giftIcon.startAnimation(giftIconAnimSet);

        //文字放大
        AnimationSet hotsAnimSet = new AnimationSet(false);
        ScaleAnimation hostScaleAnim = new ScaleAnimation(2.5f, 1f, 2.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hotsAnimSet.addAnimation(hostScaleAnim);

        AlphaAnimation hotsAlphaAnim = new AlphaAnimation(0f,1f);
        hotsAnimSet.addAnimation(hotsAlphaAnim);

        TranslateAnimation hotsTransAnim =
                new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,-0.5f,
                        TranslateAnimation.RELATIVE_TO_SELF,0f,
                        TranslateAnimation.RELATIVE_TO_SELF,0f,
                        TranslateAnimation.RELATIVE_TO_SELF,0f

                );
        hotsTransAnim.setInterpolator(context, android.R.anim.linear_interpolator);
        hotsAnimSet.addAnimation(hotsTransAnim);

        hotsAnimSet.setDuration(300);
        hotsAnimSet.setRepeatMode(Animation.INFINITE);
        hotsAnimSet.setRepeatCount(0);
        hotsAnimSet.setFillAfter(true);
        hotsAnimSet.setStartOffset(1000);

        hotsGained.startAnimation(hotsAnimSet);
    }



    /**
     * 动态,守护，粉丝内容从底部滑出
     * @param view
     * @param moveY
     */
    public static void viewUpToMiddle(View view, int moveY, long duration) {
        view.setVisibility(View.VISIBLE);
        AnimationSet animationUpSet = new AnimationSet(false);
        TranslateAnimation translateUpAnimation = new TranslateAnimation(0,0,moveY,0);
        translateUpAnimation.setDuration(500);
        translateUpAnimation.setFillAfter(true);
        animationUpSet.addAnimation(translateUpAnimation);
        view.startAnimation(animationUpSet);
    }

    /**
     * 动态,守护，粉丝内容从中间滑到底部
     */
    public static void viewDownToBottom(View view, int moveY, long duration) {
        view.setVisibility(View.GONE);
        AnimationSet animationDownSet = new AnimationSet(false);
        TranslateAnimation translateDownAnimation = new TranslateAnimation(0,0,0,moveY);
        translateDownAnimation.setDuration(500);
        translateDownAnimation.setFillAfter(true);
        animationDownSet.addAnimation(translateDownAnimation);
        view.startAnimation(animationDownSet);
    }


}
