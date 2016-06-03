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

    public static Bitmap getBitmapFormRes(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inSampleSize = 1;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap getThumbnail(Context context, Uri uri, int size) {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();

            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            try {
                input.close();
                if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                    return null;
                }

                int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
                double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
                bitmapOptions.inDither = true;
                bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

                input = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                input.close();

                return bitmap;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
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

        /**
         * 广告位
         */
        public static final String BANNER="_720x360";

        /**
         * 用户头像
         */
        public static final String USER_HEAD="_86x86";

        /**
         * 图标（小）
         */
        public static final String COMMODITY_ICON_SMALL="_90x90";

        /**
         * 图标（中）
         */
        public static final String COMMODITY_ICON_MIDDLE="_154x156";

        /**
         * 图标（大）
         */
        public static final String COMMODITY_ICON_BIG="_359x210s";

        /**
         * 图标
         */
        public static final String COMMODITY_ICON_210="_210x210";

        public static final String FULL = "_800x600";
    }


    public static Bitmap readBitMap(File file) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;


        return BitmapFactory.decodeFile(file.getAbsolutePath(), opt);

    }

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
}
