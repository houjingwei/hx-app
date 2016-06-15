package com.huixiangtv.live.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.live.App;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by apple on 16/5/13.
 */
public class BitmapHelper {
    private static BitmapHelper instance;

    private DisplayImageOptions displayImageOptionsSmall;
    private DisplayImageOptions displayImageOptionsBig;

    private BitmapHelper() {
        displayImageOptionsSmall = new DisplayImageOptions.Builder()
                //.showImageForEmptyUri(R.drawable.blank_default200)
                //.showImageOnFail(R.drawable.blank_default200)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();

        displayImageOptionsBig = new DisplayImageOptions.Builder()
                //.showImageForEmptyUri(R.drawable.blank_default)
                //.showImageOnFail(R.drawable.blank_default)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
    }

    private static Context mContext;

    public static BitmapHelper getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new BitmapHelper();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .build();
            ImageLoader.getInstance().init(config);
        }
        return instance;

    }


    /**
     * @param container
     * @param uri
     * @param imageSize {@link ImageSize}
     */
    public <T extends View> void display(T container, final String uri, final String imageSize, DefaultSize defaultSize) {
        if (container instanceof ImageView) {
            if (uri != null) {
                String url = uri + imageSize;
                DisplayImageOptions options = (defaultSize == DefaultSize.SMALL ?
                        displayImageOptionsSmall : displayImageOptionsBig);
                ImageLoader.getInstance().displayImage(url, (ImageView) container, options);
            }
        }
    }

    public <T extends View> void display(T container, final String uri) {
        if (container instanceof ImageView) {
            if (uri != null) {
                //String url = uri + ImageSize.ZI_XUN;

                //ImageLoader.getInstance().displayImage(url, (ImageView) container, displayImageOptionsSmall);
            }
        }
    }

    public static enum DefaultSize {
        BIG, SMALL
    }

    /**
     * @author stone
     * @descrption 各位置图片裁剪尺寸
     * @date 2015-6-17
     */
    public static class ImageSize {


    }

    static Bitmap bmap;

    public static Bitmap copressImage(String imgPath, int detWidth, int detHeight) {
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if (bmap != null) {
            //ivwCouponImage.setImageBitmap(bmap);
            return createScaleBitmap(bmap, detWidth, detHeight, bitmapFactoryOptions.inSampleSize);
        }
        return null;
    }

    public static Bitmap copressImage(String imgPath) {
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if (bmap != null) {
            //ivwCouponImage.setImageBitmap(bmap);
            return bmap;
        }
        return null;
    }


    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
        //如果inSampleSize是2的倍数，也就说这个src已经是我们想要的缩略图了，直接返回即可。
        if (inSampleSize % 2 == 0) {
            return src;
        }
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    public static Bitmap readBitMap(File file) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;


        return BitmapFactory.decodeFile(file.getAbsolutePath(), opt);

    }

    //
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // float scaleHeight = (((float)height/newHeight)*height)/newHeight;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片 www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }


    public static Bitmap setDrawable(Bitmap backGroundMap,int widthP,int heightP) {
        int width = widthP; //App.screenWidth;
        int height =heightP;// App.screenHeight;

        int widthDrawable = backGroundMap.getWidth();
        int heightDrawable = backGroundMap.getHeight();//获取背景图片的宽和高
        float scaleWidth = (float) width / widthDrawable;
        float scaleHeight = (float) height / heightDrawable;//宽高比

        Bitmap resizeBmp;
        Matrix matrix = new Matrix();
        if (scaleWidth < scaleHeight) {
            float scale = scaleHeight;//取大的
            matrix.postScale(scale, scale);//缩放比例
            int xStart = (int) (widthDrawable - widthDrawable / scale) / 2;


            resizeBmp = Bitmap.createBitmap(backGroundMap, xStart, 0, (int) (widthDrawable / scale),
                    heightDrawable, matrix, true);
        } else {
            float scale = scaleWidth;
            matrix.postScale(scale, scale);
            int yStart = (int) (scaleHeight - scaleHeight / scale) / 2;
            resizeBmp = Bitmap.createBitmap(backGroundMap, 0, yStart, widthDrawable,
                    (int) (heightDrawable / scale), matrix, true);
        }
        return resizeBmp;

    }
}
