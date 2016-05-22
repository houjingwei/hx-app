package com.huixiangtv.live.utils;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Stone on 16/5/19.
 */
public class ShareSdk {


    private UMImage image;
    public ShareSdk() {
        //configPlatforms();
    }

    /**
     * 分享通用
     * @param title
     * @param content,
     * @param url
     * @param imgUrl
     */
    public static void startShare(Activity activity, String title, String content, SHARE_MEDIA targetPlatform, String imgUrl,UMShareListener umShareListener) {
        if (TokenChecker.checkToken(activity)) {


            UMImage image = new UMImage(activity, imgUrl);
            new ShareAction(activity).setPlatform(targetPlatform).setCallback(umShareListener)
                    .withMedia(image)
                            //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                    .withText(content)
                            .withTitle(title)
                            //.withTargetUrl(url)
                    .share();

        }
    }

    public static void startShare(Activity activity, String title, String content,  SHARE_MEDIA targetPlatform, String imgUrl) {
        if (TokenChecker.checkToken(activity)) {


            UMImage image = new UMImage(activity, imgUrl);
            new ShareAction(activity).setPlatform(targetPlatform)
                    .withMedia(image)
                            //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                    .withText(content)
                    .withTitle(title)
                            //.withTargetUrl(url)
                    .share();

        }
    }


}
