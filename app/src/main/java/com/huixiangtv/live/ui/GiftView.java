package com.huixiangtv.live.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.GiftAdapter;
import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.utils.CommonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/19.
 */
public class GiftView extends RelativeLayout {


    Context ct;
    Activity activity;


    private int giftViewCount = 0;
    private ViewPager viewPager;
    private ArrayList<View> giftViews;
    private RelativeLayout[] pointRootViews;
    // 包裹滑动图片LinearLayout
    private ViewGroup main;
    // 包裹小圆点的LinearLayout
    private LinearLayout llPointView;

    private ArrayAdapter<View> adapter;

    private List<Gift> giftList = new ArrayList<Gift>();


    public GiftView(Context context) {
        super(context);
        ct = context;
        LayoutInflater.from(context).inflate(R.layout.live_gift_view, this);

    }

    public void initView() {
        if(null== App.giftList){
            App.loadFreeGiftList();
            CommonHelper.showTip(activity,"免费礼物加载失败");
            activity.onBackPressed();

        }
        giftList = App.giftList;
        giftViewCount = giftList.size()%5==0? giftList.size()/5: giftList.size()/5+1;

        int[] img = new int[]{R.drawable.default_error, R.drawable.default_error, R.drawable.default_error,R.drawable.default_error, R.drawable.default_error};
        giftViews = new ArrayList<View>();
        for (int i = 0; i < giftViewCount; i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.gift_list_view, null);
            GridView gvGiftView = (GridView) view.findViewById(R.id.gvGift);
            gvGiftView.setNumColumns(5);
            final GiftAdapter giftAdapter = new GiftAdapter(activity);
            gvGiftView.setAdapter(giftAdapter);
            gvGiftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Gift gift = (Gift) giftAdapter.getItem(position);
                    CommonHelper.showTip(activity,gift.getName());

                    sendConcreteGift(gift,view,null);
                }
            });
            giftAdapter.addList(getGiftPagerData(i,5));
            giftViews.add(view);

        }




        llPointView = (LinearLayout) findViewById(R.id.llPointView);
        viewPager = (ViewPager) findViewById(R.id.guidePages);


        /**
         * 有几张图片下面就显示几个小圆点
         */
        pointRootViews = new RelativeLayout[giftViews.size()];
        // 设置每个小圆点距离左边的间距
        for (int i = 0; i < giftViews.size(); i++) {
            RelativeLayout pointRootView = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.point_image_view,null);
            pointRootViews[i] = pointRootView;
            ImageView imageView = (ImageView) pointRootViews[i].findViewById(R.id.pointImage);
            if (i == 0) {
                // 默认选中第一张图片
                imageView.setImageResource(R.mipmap.point_check);
            } else {
                // 其他图片都设置未选中状态
                imageView.setImageResource(R.mipmap.point_uncheck);

            }

            llPointView.addView(pointRootView);

        }

        // 给viewpager设置适配器
        viewPager.setAdapter(new GuidePageAdapter());

        // 给viewpager设置监听事件
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }


    /**
     * 送礼
     * @param gift
     * @param giftView
     * @param barrageArea
     */
    private void sendConcreteGift(final Gift gift, View giftView, View  barrageArea) {
        final User loginUser = App.getLoginUser();
        if(null==loginUser){
            showLoginPopWin();
            return;
        }
        final ImageView icon = (ImageView) giftView.findViewById(R.id.ivIcon);

        //验证金币并发送礼物
        if(checkCoinsBalance(Integer.parseInt(gift.getPrice()))){
            sendGift(gift,icon,barrageArea,true,null, Constant.GIFT_TYPE_NORMAL,null);
        }

    }



    /**
     * 给艺人送礼物(快捷礼物和普通礼物)
     * @param gift
     */
    private void sendGift(final Gift gift, final View baseLocationView, final View barrageArea,final boolean needClientAnim,String tip,String giftType,final ApiCallback<String> callback){
        if(null==gift){
            Toast.makeText(activity, "礼物不存在!", Toast.LENGTH_SHORT).show();
            return;
        }
//        giftType=null==giftType?"1":giftType;
//        User loginUser = App.getLoginUser();
//        Map<String,String> params= new HashMap<String,String>();
//        params.put("gid",gift.getGid()+"");//礼物ID
//        params.put("count","1");//数量
//        params.put("gift",giftType);//礼物类型：1：普通礼物 2：喊话 3: 守护礼物
//        params.put("amount",gift.getPrice()+"");//总金币
//        params.put("buid",loginUser.getUid());//购买人
//        params.put("cuid",currentArtist.getUid());//使用人
//        params.put("token",loginUser.getToken());//令牌
//        params.put("tip",null==tip?(String.format("送给%s", currentArtist.getNickName())):tip);//提示信息
//        params.put("clientId",null==messageConnector.getChatToken()?"":messageConnector.getChatToken().getClientId());
//        params.put("flag",messageConnector.getFlag());
//        params.put("key",messageConnector.getKey());
//        //礼物消费接口
//        ApiService.giftPayment(params, new ResponseCallBack<String>() {
//            @Override
//            public void onSuccess(String data) {
//                if(needClientAnim){
//                    //启动客户端礼物特效动画
//                    AnimHelper animHelper = new AnimHelper(VideoActivity.this, videoHandler);
//                    animHelper.showSendGift(rootContainer, baseLocationView, barrageArea, gift);
//                }
//
//                //更新金币数量
//                App.userCoin-=gift.getPrice();
//                myCoins.setText(App.userCoin+"");
//                myCoinsFS.setText(App.userCoin+"");
//
//                //回调
//                if(null!=callback){
//                    callback.onSuccess(data);
//                }
//            }
//
//            @Override
//            public void onFailure(ServiceException e) {
//                Log.e(Constant.TAG,"消费礼物出错!",e);
//                Toast.makeText(VideoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                if(null!=callback){
//                    callback.onFailure(e);
//                }
//            }
//        });

    }

    //验证当前用户的金币余额
    private boolean checkCoinsBalance(long requiredNum){
        //验证金币
        if(requiredNum>App.userCoin){
            Toast.makeText(activity, "剩余金币不足，请充值!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


    /**
     * 显示登录面板
     */
    private void showLoginPopWin() {

    }


    private List<Gift> getGiftPagerData(int num, int numPerPage) {
        int minIndex = num*numPerPage;
        int maxIndex = (num+1)*numPerPage;
        if(maxIndex> giftList.size()){
            maxIndex = giftList.size();
        }
        return giftList.subList(minIndex,maxIndex);
    }



    // 指引页面数据适配器

    class GuidePageAdapter extends PagerAdapter {
        @Override

        public int getCount() {

            return giftViews.size();

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager)arg0).removeView(giftViews.get(arg1));
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager)arg0).addView(giftViews.get(arg1));
            return giftViews.get(arg1);
        }

    }



    // 指引页面更改事件监听器
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }



        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 遍历数组让当前选中图片下的小圆点设置颜色
            for (int i = 0; i < pointRootViews.length; i++) {
                ImageView imageView = (ImageView) pointRootViews[arg0].findViewById(R.id.pointImage);
                imageView.setImageResource(R.mipmap.point_check);
                if (arg0 != i) {
                    ImageView others = (ImageView) pointRootViews[i].findViewById(R.id.pointImage);
                    others.setImageResource(R.mipmap.point_uncheck);

                }

            }

        }

    }



    public GiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
