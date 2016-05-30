package com.huixiangtv.live.utils.image;

import android.graphics.Bitmap;

/**
 * Created by Stone on 16/5/30.
 */
public class ImageBlur {
    public static native void blurIntArray(int[] pImg, int w, int h, int r);

    public static native void blurBitMap(Bitmap bitmap, int r);

    static {
        System.loadLibrary("JNI_ImageBlur");
    }
}
