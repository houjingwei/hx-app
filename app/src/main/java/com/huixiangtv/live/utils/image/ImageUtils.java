package com.huixiangtv.live.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.live.App;

import com.huixiangtv.live.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 使用UniversalImageLoader加载大量高清图片.带有缓存.可以快速重复显示图片.
 */
public final class ImageUtils {
    static {
        initImageLoader(App.getContext());
    }

    private static void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "cache/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(5) //线程池内线程的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 8 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();
        ImageLoader.getInstance().init(config);
    }

    // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
    private final static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.default_43) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.default_43) // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default
            .bitmapConfig(Bitmap.Config.RGB_565) // default
            .build(); // 构建完成

    //显示本地,网络图片.
    public static void display(ImageView imageView, String uri) {
        if (uri == null || uri.length() <= 0) return;
        if (uri.charAt(0) == '/') {
            uri = "file://" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    //带进度显示.
    public static void display(ImageView imageView, String uri, SimpleProgressListener l) {
        if (uri == null || uri.length() <= 0) return;
        if (uri.charAt(0) == '/') {
            uri = "file://" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageView), options,
                l, l);
    }

    //显示头像专用
    public static void displayAvator(ImageView imageView, String uri) {
        if (uri == null || uri.length() <= 0) return;
        if (uri.charAt(0) == '/') {
            uri = "file://" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void displaySize(ImageView imageView, String uri, int maxWidth, int maxHeigh) {
        if (uri == null || uri.length() <= 0) return;
        if (uri.charAt(0) == '/') {
            uri = "file://" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, new ImageSize(maxWidth, maxHeigh));
    }

    //显示本地,网络,R.drawable.xxx
    public static void display(ImageView imageView, int resId) {
        if (resId < 0) return;
        String url = "drawable://" + resId;
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    //显示assets目录文件.
    public static void displayAssert(ImageView imageView, String assertPath) {
        String url = "assets://" + assertPath;
        ImageLoader.getInstance().displayImage(url, imageView, options);

    }


    public static class SimpleProgressListener implements ImageLoadingListener, ImageLoadingProgressListener {

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }

        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {

        }
    }
}
