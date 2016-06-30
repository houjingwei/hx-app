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

    //奉献爱心
    public static final String LOVEPAYMENT = BASE_URL+"pay/loveGiftPayment";

    //关注
    public static final String ATTENTION = BASE_URL+"fans/addFollow";
    //取消
    public static final String CANCEL_ATTENTION = BASE_URL+"fans/removeFollow";
    //关注状态
    public static final String ATTENTIOIN_STATUS = BASE_URL+"fans/isFollowed";
    //分享加爱心
    public static final String ADD_LOVE = BASE_URL+"promote/isShared";


    //我关注的人
    public static String GETCOLLECTARTIST = BASE_URL+"fans/getCollectArtist";
    //我的粉丝
    public  static String GETOWNFANS = BASE_URL+"fans/getOwnFans";
    //我的粉丝数量
    public  static String FANS_COUNT = BASE_URL+"fans/getOwnFansCount";
    //人气贡献榜
    public  static String MY_HOTS = BASE_URL+"fans/getPopularityRank";



    //进场消息
    public static final String INTO_ROOM = BASE_URL+"chat/sendEnterMsg";

    //关闭直播
    public static final String LIVEING_CLOSE = BASE_URL+"live/liveStatusNotify";


    public static String UP_PWD = BASE_URL+"auth/resetPaswd";

    //get user auth status
    public static String USER_GETAUTHSTATUS = BASE_URL + "user/getAuthStatus";

    //set Artist Card Info
    public static String SET_ARTIST_CARD_INFO = BASE_URL + "user/setArtistCardInfo";

    //获取用户资料
    public static String USER_INFO = BASE_URL + "user/userInfo/get";
    public static final String ACCOUNT =BASE_URL + "account/balance";




    //set User info status by Artistcard
    public static String GET_USER_ARTISTCARD_STATUS = BASE_URL+"user/getArtistCardStatus";


    //get User Info status by Artistcard
    public  static  String GET_USER_ARTISTCARD =BASE_URL+"user/getArtistCardInfo";






    public  static String GETPOPULARITYRANK = BASE_URL +"/fans/getPopularityRank";


    /**
     * 爱心明细
     */
    public static String LOVE_DETAIL = BASE_URL+"account/getLoveDetail";


    /**
     * 分享地址
     */
    public static final String SHARE_URL = "http://119.29.94.122:8888/h5/index.html?uid=&lid=";


    public static final String SHARE_INFO = BASE_URL+"/share/getShareInfo";

    public static final String GETGLOBALCONFIG =  BASE_URL+"/config/getGlobalConfig";

    public static final String SHAREBINDSHARECODE = BASE_URL+"/share/bindShareCode";


}
