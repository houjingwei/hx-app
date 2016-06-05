package com.huixiangtv.live;

/**
 * Created by hjw on 16/5/13.
 */
public class Api {
//     public static final String BASE_URL = "http://192.168.0.168:9999/huixiang-web-api/";
//    public static final String BASE_URL = "http://119.29.94.122:8888/";
//    public static final String BASE_URL = "http://119.29.2.57:9999/api/";
    public static final String BASE_URL = "http://119.29.147.90:8080/api/";
//    public static final String BASE_URL = "http://119.29.94.122:8888/";
    public static final String TOPIC = BASE_URL+"live/getUserLiveTopics";
    public static final String LIVE_BANNER = BASE_URL+"content/getBanner";
    public static final String LIVE_LIST = BASE_URL+"live/getUserLive";
    public static final String GIFT_LIST = BASE_URL+"gift/getGifts";
    public static final String REG = BASE_URL+"auth/register";
    public static final String LOGIN = BASE_URL+"auth/login";
    public static final String AUTH_THIRDLOGIN = BASE_URL + "auth/thirdlogin";
    public static final String AUTH_GETACCOUNT_BINDINFO = BASE_URL + "auth/getAccountBindInfo";
    public static final String AUTH_ACCOUNTBIND = BASE_URL + "auth/accountBind";
    public static final String CONTENT_GET_BANNER = BASE_URL +"content/getBanner";
    public static final String POST = BASE_URL+"pay/giftPayment";



    public static final String COIN_LIST = BASE_URL+"pay/getCoinRule";
    public static final String PAY_MODE = BASE_URL+"pay/getPaymentMode";

    //申请个人直播
    public static final String LIVE_SHOW =BASE_URL+ "live/applyLiveshow";
    //通过ID获取直播详情
    public static final String LIVEINFO = BASE_URL+ "live/getLiveInfoByLid";
    //直播拉流地址
    public static final String PLAY_URL = BASE_URL+ "live/getPlayUrl";;
    //版本升级
    public static final String UPGRADE_LEVEL = BASE_URL + "config/getUpgradeInfo";

    //获取聊天室token
    public static final String CHAT_TOKEN = BASE_URL+"chat/getToken";
    public static final String SEND_MSG = BASE_URL+"chat/sendUgcMsg";
    public static final String HISTORY_MSG = BASE_URL+"chat/getLastMsg";
    public static final String ONLINE_USER = BASE_URL+"chat/getLastUser";


    //保存用户信息
    public static final String SAVE_USER = BASE_URL+"user/userInfo/save";
    //标签列表
    public static final String USER_TAG = BASE_URL+"tag/getHotTags";
    //打标签
    public static final String ADD_TAG = BASE_URL+"tag/addTag";


    //获取验证码
    public static final String MSG_CODE = BASE_URL+"auth/getVerifCode";
    public static final String UPLOAD_FILE_INFO = BASE_URL+"resource/getImageUploadInfo";

    //喊话
    public static final String GIFT_PAYMENT = BASE_URL+"pay/shoutGiftPayment";
    public static final String SHOUT_GIFT = BASE_URL+"gift/getShoutGiftInfo";


    public static String UP_PWD = BASE_URL+"auth/resetPaswd";

    //get user auth status
    public static String USER_GETAUTHSTATUS = BASE_URL + "user/getAuthStatus";

    //set Artist Card Info
    public static String SET_ARTIST_CARD_INFO = BASE_URL + "user/setArtistCardInfo";


    //set User info status by Artistcard
    public static String SET_USER_ARTISTCARD = BASE_URL+"user/getArtistCardStatus";


    //get User Info status by Artistcard
    public  static  String GET_USER_ARTISTCARD =BASE_URL+"user/getArtistCardInfo";


}
