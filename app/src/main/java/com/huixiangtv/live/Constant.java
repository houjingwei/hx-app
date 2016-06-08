package com.huixiangtv.live;

import java.util.UUID;

/**
 * Created by hjw on 16/5/5.
 */
public class Constant {

    public static final String TAG = "reponseStr";
    public static final String ERROR_TAG = "requestError";


    public static final String BASE_PRE = "huixiang://huixiang/";
    public static final String ACCOUNT = BASE_PRE+"account";
    public static final String USERINFO = BASE_PRE+"userinfo";
    public static final String USERTAG = BASE_PRE+"tag";
    public static final String LOGIN = BASE_PRE+"login";
    public static final String START_LIVE = BASE_PRE+"push";
    public static final String LIVE_TOPIC = BASE_PRE+"topic";
    public static final String SETINT = BASE_PRE+"set";
    public static final String LIVE = BASE_PRE+"live";
    public static final String SEARCH = BASE_PRE +"search";
    public static final String REG_LIVE = BASE_PRE+"reg";
    public static final String REG_LIVE_NEXT = BASE_PRE+"next";
    public static final String REG_LIVE_MAIN = BASE_PRE+"main";
    public static final String REG_LIVE_DES = BASE_PRE+"details";
    public static final String HELP = BASE_PRE +"help";
    public static final String FANS = BASE_PRE+"fans";


    public static final String PHONE_BIND = BASE_PRE+"phone_bind";


    public static final String MY_FANS = BASE_PRE+"my_fans";
    public static final String FANED_ME = BASE_PRE+"faned_me";

    public static final String POPULARITY = BASE_PRE+"popularity";

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

    public static final String PIC_LIST = BASE_PRE +"pic_list";


    public static final String APP_KEY = "2088b26127ebe67";
    public static final String APP_SECRET = "b898e3010acf4299a7a5d3ec6e99bb2c";
    public static String accessToken;//accessToken 通过调用授权接口得到
    public static final String LIVE_URL="http://huixiangtv.s.qupai.me";
    public static final String SPACE = UUID.randomUUID().toString().replace("-",""); //存储目录 建议使用uid cid之类的信息
}
