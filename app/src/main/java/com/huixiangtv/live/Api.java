package com.huixiangtv.live;

/**
 * Created by hjw on 16/5/13.
 */
public class Api {

    public static final String BASE_URL = "http://119.29.94.122:8888/";


    public static final String TOPIC = BASE_URL+"live/getUserLiveTopics";
    public static final String LIVE_BANNER = BASE_URL+"content/getBanner";
    public static final String LIVE_LIST = BASE_URL+"live/getUserLive";


    public static final String GIFT_LIST = BASE_URL+"gift/getGifts";
    public static final String REG = BASE_URL+"auth/register";
    public static final String LOGIN = BASE_URL+"auth/login";
    //礼物消费
    public static final String POST = BASE_URL+"pay/giftPayment";



    public static final String COIN_LIST = BASE_URL+"pay/getCoinRule";
    public static final String PAY_MODE = BASE_URL+"pay/getPaymentMode";
}