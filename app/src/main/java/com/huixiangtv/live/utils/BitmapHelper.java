package com.huixiangtv.live.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

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
    public static BitmapHelper getInstance(Context context){
        if(instance==null){
            mContext=context;
            instance=new BitmapHelper();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .build();
            ImageLoader.getInstance().init(config);
        }
        return instance;

    }


    /**
     *
     * @param container
     * @param uri
     * @param imageSize {@link ImageSize}
     */
    public <T extends View> void display(T container,final String uri,final String imageSize,DefaultSize defaultSize) {
        if(container instanceof ImageView) {
            if(uri != null) {
                String url = uri + imageSize;
                DisplayImageOptions options = (defaultSize == DefaultSize.SMALL ?
                        displayImageOptionsSmall : displayImageOptionsBig);
                ImageLoader.getInstance().displayImage(url, (ImageView)container, options);
            }
        }
    }

    public <T extends View> void display(T container,final String uri) {
        if(container instanceof ImageView) {
            if(uri != null) {
                //String url = uri + ImageSize.ZI_XUN;

                //ImageLoader.getInstance().displayImage(url, (ImageView) container, displayImageOptionsSmall);
            }
        }
    }
    public static enum DefaultSize{
        BIG,SMALL
    }

    /**
     *
     * @descrption 各位置图片裁剪尺寸
     * @author stone
     * @date 2015-6-17
     */
    public static class ImageSize{


    }

    static Bitmap bmap;
    public static Bitmap copressImage(String imgPath){
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth  = bitmapFactoryOptions.outWidth;
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
        if(bmap != null){
            //ivwCouponImage.setImageBitmap(bmap);
            return bmap;
        }
        return null;
    }

//
//    public static Bitmap readBitMap(File file) {
//
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//
//        opt.inPreferredConfig = Bitmap.Config.RGB_565;
//
//        opt.inPurgeable = true;
//
//        opt.inInputShareable = true;
//
//
//        return BitmapFactory.decodeFile(file.getAbsolutePath(), opt);
//
//    }
//
//    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
//        // 获得图片的宽高
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        // 计算缩放比例
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // float scaleHeight = (((float)height/newHeight)*height)/newHeight;
//        // 取得想要缩放的matrix参数
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        // 得到新的图片 www.2cto.com
//        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
//                true);
//        return newbm;
//    }

    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) myFileUrl.openConnection();

            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }




}
