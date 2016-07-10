package com.huixiangtv.live.utils.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Upfile;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
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
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.VideoAttr;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;
import com.tencent.upload.task.impl.VideoUploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import id.zelory.compressor.Compressor;

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



    public static void upFileInfo(Map<String,String> params ,final ApiCallback<Upfile> apiCallback) {

        RequestUtils.sendPostRequest(Api.UPLOAD_FILE_INFO, params, new ResponseCallBack<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
                super.onSuccess(data);
                if (null != data) {
                    apiCallback.onSuccess(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Upfile.class);
//        ImageUtils.display(ivPhoto,picUri);
    }



    public static void upVideoInfo(Map<String,String> params ,final ApiCallback<Upfile> apiCallback) {
        RequestUtils.sendPostRequest(Api.UPLOAD_VIDEO_INFO, params, new ResponseCallBack<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
                super.onSuccess(data);
                if (null != data) {
                    apiCallback.onSuccess(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Upfile.class);
//        ImageUtils.display(ivPhoto,picUri);
    }


    public static void upFile(Activity activity, Upfile data, String picUri, final ApiCallback callBack) {
        final String path = compressPath(activity,picUri);

        UploadManager fileUploadMgr = new UploadManager(activity,data.getAppId(), Const.FileType.Photo,data.getPersistenceId());
        PhotoUploadTask task = new PhotoUploadTask(path,new IUploadTaskListener() {

            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                File file = new File(path);
                if(file.exists()){
                    file.delete();
                    Log.i("imgPath","删除了");
                }
                callBack.onSuccess(fileInfo);

            }

            @Override
            public void onUploadFailed(int i, String s) {
                Log.i("upfile","onUploadFailed"+s);
            }

            @Override
            public void onUploadProgress(long l, long l1) {

            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {

            }
        });


        task.setBucket(data.getBucket());
        task.setFileId(data.getFileName()); // 为图片自定义FileID(可选)
        task.setAuth(data.getSig());
        fileUploadMgr.upload(task);

    }





    private static String compressPath(Activity activity,String picUri) {

        File file = new File(picUri);
        Log.i("dynamicType",file.length()+"");

        File  compressedImage = new Compressor.Builder(activity)
                .setMaxWidth(1024)
//                .setMaxHeight(800)
                .setQuality(80)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(new File(picUri));

        Log.i("dynamicType",compressedImage.getAbsolutePath()+"*****");
        //定义一个file，为压缩后的图片
        return compressedImage.getAbsolutePath();
    }


    public static void upVideo(Activity activity, Upfile data, String videoPath, final ApiCallback<FileInfo> callBack) {
        UploadManager fileUploadMgr = new UploadManager(activity,data.getAppId(), Const.FileType.Video,data.getPersistenceId());

        VideoAttr videoAttr = new VideoAttr(); videoAttr.isCheck = false;
        videoAttr.title = "";
        videoAttr.desc = "";
        videoAttr.coverUrl = ""; // 视频封面地址


        VideoUploadTask videoTask = new VideoUploadTask(data.getBucket(), videoPath,data.getFileName(), "", videoAttr,new IUploadTaskListener(){


            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                callBack.onSuccess(fileInfo);
            }

            @Override
            public void onUploadFailed(int i, String s) {

            }

            @Override
            public void onUploadProgress(long l, long l1) {
                Log.i("upLoad",l+"***"+l1);
            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {

            }
        });
        videoTask.setBucket(data.getBucket());
        videoTask.setAuth(data.getSig());
        fileUploadMgr.upload(videoTask);
    }









    /**
     * 获取和保存当前屏幕的截图
     */
    public static void catImage(final Activity activity)
    {

        //1.构建Bitmap
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap Bmp = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
        //2.获取屏幕
        View decorview = activity.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();
        String SavePath = getSDCardPath()+"/cutImage";
        //3.保存Bitmap
        try {
            File path = new File(SavePath);
            //文件
            String filepath = SavePath + "/"+ System.currentTimeMillis()+".png";
            File file = new File(filepath);
            if(!path.exists()){
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                CommonHelper.showTip(activity,"截屏文件已保存至"+file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private static String getSDCardPath(){
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdcardExist){
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }
}
