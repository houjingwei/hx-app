package com.huixiangtv.liveshow;

import java.util.UUID;

/**
 * Created by hjw on 16/5/5.
 */
public class Constant {

    public static final String TAG = "reponseStr";
    public static final String ERROR_TAG = "requestError";


    public static final String BASE_PRE = "huixiang://huixiang/";
    public static final String ACCOUNT = BASE_PRE+"account";
    public static final String MY_INFO = BASE_PRE+"myinfo";
    public static final String USER_INFO = BASE_PRE+"userinfo";
    public static final String USERTAG = BASE_PRE+"tag";
    public static final String LOGIN = BASE_PRE+"login";
    public static final String START_LIVE = BASE_PRE+"push";
    public static final String LIVE_TOPIC = BASE_PRE+"topic";
    public static final String SETINT = BASE_PRE+"set";
    public static final String LIVE = BASE_PRE+"live";
    public static final String LIVERECORD_FINISH =  BASE_PRE+"liveRecordFinish";
    public static final String SEARCH = BASE_PRE +"search";
    public static final String REG_LIVE = BASE_PRE+"reg";
    public static final String REG_LIVE_NEXT = BASE_PRE+"next";
    public static final String REG_LIVE_MAIN = BASE_PRE+"main";
    public static final String REG_LIVE_DES = BASE_PRE+"details";
    public static final String BINNER_URL = BASE_PRE+"banner";
    public static final String FRIEND = BASE_PRE+"friend";

    public static final String HELP = "http://119.29.94.122:8888/h5/help.html";
    public static final String GYWM = "http://119.29.94.122:8888/h5/about.html";
    public static final String FEEDBACK = "http://119.29.94.122:8888/h5/feedback.html";
    public static final String ORDER = "http://119.29.94.122:8888/h5/rechargeRecord.html?title=充值记录";
    public static final String PAY_PRO = "http://119.29.94.122:8888/h5/payAgreeMent.html";
    public static final String REG_PRO = "http://119.29.94.122:8888/h5/registAgreeMent.html";

    public static final String FANS = BASE_PRE+"fans";


    public static final String PHONE_BIND = BASE_PRE+"phone_bind";


    public static final String MY_FANS = BASE_PRE+"my_fans";
    public static final String FANED_ME = BASE_PRE+"faned_me";

    public static final String POPULARITY = BASE_PRE+"popularity";
    public static final String MY_HOTS = BASE_PRE+"hots";
    public static final String OWN_CIRCLE = BASE_PRE+"own_circle";
    public static final String PUSH_DYNAMIC = BASE_PRE+"push_dynamic";

    public static final String COUPON =BASE_PRE+"coupon";
    public static final String DYNAMIC_DETAIL = BASE_PRE+"dynamic_detial";

    public static String MY_LOVES = BASE_PRE+"my_loves";







    public static final int IO_BUFFER_SIZE = 256;

    public final static String LOGIN_INFO = "login_info";

    //礼物类型
    public static final String GIFT_TYPE_NORMAL = "1";//普通礼物
    public static final String GIFT_TYPE_SHOUT = "2";//喊话礼物
    public static final String GIFT_TYPE_GUARD = "3";//守护礼物

    public static final String PLATFORM = "Android";



    public static final String PAGE_SIZE = "10";


    /**
     * 消息类型
     */
    public static final String MSG_TYPE_BASE = "base";
    public static final String MSG_TYPE_ENTER = "enter";
    public static final String MSG_TYPE_BARRAGE = "barrage";
    public static final String MSG_TYPE_GIFT = "gift";
    public static final String MSG_TYPE_LOVE = "love";
    public static final String LIVING_CLOSE = "liveStop";


    public static final String PIC_LIST = BASE_PRE +"pic_list";


    public static final String APP_KEY = "2088b26127ebe67";
    public static final String APP_SECRET = "b898e3010acf4299a7a5d3ec6e99bb2c";
    public static String accessToken;//accessToken 通过调用授权接口得到
    public static final String LIVE_URL="http://huixiangtv.s.qupai.me";
    public static final String SPACE = UUID.randomUUID().toString().replace("-",""); //存储目录 建议使用uid cid之类的信息



}
