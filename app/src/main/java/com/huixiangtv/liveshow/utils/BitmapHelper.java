package com.huixiangtv.liveshow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.liveshow.App;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
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

    public enum DefaultSize {
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

    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        // 获取资源图片

        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, opt);

    }



    public static Bitmap readBitMap(File file,boolean isSS) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        if(isSS)
            opt.inSampleSize = 2;

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;


        return BitmapFactory.decodeFile(file.getAbsolutePath(), opt);
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//       // if(isSS)
//        opt.inDither=false;
//        opt.inPurgeable=true;
//        opt.inTempStorage=new byte[12 * 1024];
//
//        //opt.inJustDecodeBounds = true;
//        //BitmapFactory.decodeFile(file.getAbsolutePath(), opt);
//
//        //opt.inSampleSize = computeSampleSize(opt, -1, 128*128);
//        //opt.inPreferredConfig = Bitmap.Config.RGB_565;
//        //opt.inJustDecodeBounds = false;
////        opt.inPurgeable = true;
////
////        opt.inInputShareable = true;
//
//
//
//        FileInputStream fs=null;
//        try {
//            fs = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Bitmap bmp = null;
//        if(fs != null)
//            try {
//                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opt);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally{
//                if(fs!=null) {
//                    try {
//                        fs.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        return bmp;
//       // return BitmapFactory.decodeFile(file.getAbsolutePath(), opt);

    }

    public static Bitmap zoomImg(Bitmap bm) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) App.screenWidth) / width;
        float scaleHeight = ((float) App.screenHeight) / height;
        // float scaleHeight = (((float)height/newHeight)*height)/newHeight;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片 www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }


    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

//
//    public static Bitmap readBitMap(File file,boolean isSS) {
//
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        if(isSS)
//            opt.inSampleSize = 2;
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

    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

        int originWidth  = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int width  = originWidth;
        int height = originHeight;

        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            width = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        // 若图片过长, 则从上端截取
        //if (height > maxHeight) {
            height = maxHeight;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
       // }

//        Log.i(TAG, width + " width");
//        Log.i(TAG, height + " height");

        return bitmap;
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, String scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));return scaledBitmap;
    }


    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, String scalingLogic) {
        if (scalingLogic .equals("CROP")) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }
    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, String scalingLogic) {
        if (scalingLogic == "FIT") {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {



        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

    public static Bitmap ReadBitmapById(Context context, int drawableId,int screenWidth, int screenHight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHight);
    }


    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        // scale = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }



    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }


}