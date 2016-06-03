package com.huixiangtv.live.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.huixiangtv.live.App;
import com.tencent.upload.Const.FileType;
import com.tencent.upload.Const.ServerEnv;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.CommandTask;
import com.tencent.upload.task.UploadTask;

public class BizService {

    private static class DefaultCfg {
        private static final String APPID = "299201";
        private static final String FILE_BUCKET = "bb";
        private static final String PHOTO_BUCKET = "open";
        private static final String VIDEO_BUCKET = "vincentsu";

        // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
        private static final String DEV_SECRETID = "AKIDiMQx9mmdnVmVjaEj94VYyvSTw1fgZS8L";
        private static final String NORMAL_SECRETID = "AKIDAsqjH35AoJNmzjB3lfVUIHLDMB18cXG8";
    }
	
	/*******************以下参数需要根据业务修改**************************/

    public static String APPID = DefaultCfg.APPID;

    public static String FILE_SIGN = "";
    // 业务请自行修改成自己配置的bucket
    public static String FILE_BUCKET = DefaultCfg.FILE_BUCKET;
    // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
	public static String FILE_SECRETID = DefaultCfg.NORMAL_SECRETID;

    public static String PHOTO_SIGN = "";
    // 业务请自行修改成自己配置的bucket
    public static String PHOTO_BUCKET = DefaultCfg.PHOTO_BUCKET;
    // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
    public static String PHOTO_SECRETID = DefaultCfg.NORMAL_SECRETID;

    public static String VIDEO_SIGN = "";
    // 业务请自行修改成自己配置的bucket
    public static String VIDEO_BUCKET = DefaultCfg.VIDEO_BUCKET;
    // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
    public static String VIDEO_SECRETID = DefaultCfg.NORMAL_SECRETID;
	/*****************************************************************/

	public static String APP_VERSION = "1.1.3";
	public static ServerEnv ENVIRONMENT;

	private static BizService sIntance = null;
    private static final byte[] INSTANCE_LOCK = new byte[0];
    
    private UploadManager mFileUploadManager;
    private UploadManager mPhotoUploadManager;
    private UploadManager mVideoUploadManager;
    
    private SharedPreferences mSharedPreferences;

    public static synchronized BizService getInstance() {
        if (sIntance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sIntance == null) {
                    sIntance = new BizService();
                }
            }
        }
        return sIntance;
    }

    private BizService() {
        ENVIRONMENT = ServerEnv.NORMAL;
        FILE_SECRETID = DefaultCfg.NORMAL_SECRETID;
        VIDEO_SECRETID = DefaultCfg.NORMAL_SECRETID;
        PHOTO_SECRETID = DefaultCfg.NORMAL_SECRETID;
    }
    
    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences("cloud_sign", 0);
        loadSign();

        mFileUploadManager = new UploadManager(context, BizService.APPID, FileType.File, "qcloudfile");
        mPhotoUploadManager = new UploadManager(context, BizService.APPID, FileType.Photo, "qcloudphoto");
        mVideoUploadManager = new UploadManager(context, BizService.APPID, FileType.Video, "qcloudvideo");
    }

    public boolean upload(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.upload(task);

            case Photo:
                mPhotoUploadManager = new UploadManager(App.getContext(), BizService.APPID, FileType.Photo, "qcloudphoto");
                return mPhotoUploadManager.upload(task);

            case Video:
                return mVideoUploadManager.upload(task);
        }

        return false;
    }

    public boolean resume(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.resume(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.resume(task.getTaskId());

            case Video:
                return mVideoUploadManager.resume(task.getTaskId());
        }

        return false;
    }

    public boolean pause(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.pause(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.pause(task.getTaskId());

            case Video:
                return mVideoUploadManager.pause(task.getTaskId());
        }

        return false;
    }

    public boolean cancel(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.cancel(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.cancel(task.getTaskId());

            case Video:
                return mVideoUploadManager.cancel(task.getTaskId());
        }

        return false;
    }

    public boolean sendCommand(CommandTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.sendCommand(task);

            case Photo:
                return mPhotoUploadManager.sendCommand(task);

            case Video:
                return mVideoUploadManager.sendCommand(task);
        }

        return false;
    }

    public void uploadManagerClose(FileType fileType) {
        switch (fileType) {
            case File:
                mFileUploadManager.close();
                break;

            case Photo:
                mPhotoUploadManager.close();
                break;

            case Video:
                mVideoUploadManager.close();
                break;
        }
    }

    public boolean uploadManagerClear(FileType fileType) {
        switch (fileType) {
            case File:
                return mFileUploadManager.clear();

            case Photo:
                return mPhotoUploadManager.clear();

            case Video:
                return mVideoUploadManager.clear();
        }

        return false;
    }

    private String getDefaultSecretid() {
        if (ENVIRONMENT == ServerEnv.DEV) {
            return DefaultCfg.DEV_SECRETID;
        }

        return DefaultCfg.NORMAL_SECRETID;
    }

    private String getFileSecretid() {
        String secretid = DefaultCfg.DEV_SECRETID;
        if (ENVIRONMENT == ServerEnv.NORMAL) {
            secretid = DefaultCfg.NORMAL_SECRETID;
        }

        return secretid;
    }

    private String getPhotoSecretid() {
        return DefaultCfg.NORMAL_SECRETID;
    }

    private String getVideoSecretid() {
        String secretid = DefaultCfg.DEV_SECRETID;
        if (ENVIRONMENT == ServerEnv.NORMAL) {
            secretid = DefaultCfg.NORMAL_SECRETID;
        }

        return secretid;
    }

    public void changeUploadEnv(ServerEnv env) {
        ENVIRONMENT = env;
        FILE_SECRETID = getFileSecretid();
        PHOTO_SECRETID = getPhotoSecretid();
        VIDEO_SECRETID = getVideoSecretid();
        mFileUploadManager.setServerEnv(ENVIRONMENT);
        mPhotoUploadManager.setServerEnv(ENVIRONMENT);
        mVideoUploadManager.setServerEnv(ENVIRONMENT);
    }

    // 加载签名信息
    private void loadSign()
    {
    	APPID = mSharedPreferences.getString("appid", "");
        if (TextUtils.isEmpty(APPID)) {
            APPID = DefaultCfg.APPID;
        }

        FILE_BUCKET = mSharedPreferences.getString("file_bucket", "");
        if (TextUtils.isEmpty(FILE_BUCKET)) {
            FILE_BUCKET = DefaultCfg.FILE_BUCKET;
        }

        PHOTO_BUCKET = mSharedPreferences.getString("photo_bucket", "");
        if (TextUtils.isEmpty(PHOTO_BUCKET)) {
            PHOTO_BUCKET = DefaultCfg.PHOTO_BUCKET;
        }

        VIDEO_BUCKET = mSharedPreferences.getString("video_bucket", "");
        if (TextUtils.isEmpty(VIDEO_BUCKET)) {
            VIDEO_BUCKET = DefaultCfg.VIDEO_BUCKET;
        }

    	Log.i("Demo", "load appid=" + APPID + "file bucket=" + FILE_BUCKET);
    }
    
    // 保存签名信息
    private void saveSign()
    {
    	Editor edit = mSharedPreferences.edit();
        edit.putString("appid", APPID);
        edit.putString("file_bucket", FILE_BUCKET);
        edit.putString("photo_bucket", PHOTO_BUCKET);
        edit.putString("video_bucket", VIDEO_BUCKET);

    	edit.commit();
        Log.i("Demo", "save appid=" + APPID + "file bucket=" + FILE_BUCKET);
    }

    public void updateSign(String appid, FileType fileType, String bucket) {
        String sign = "";

        if (fileType == FileType.File) {
            sign = FILE_SIGN;
        }

        if (fileType == FileType.Photo) {
             sign = PHOTO_SIGN;
        }

        if (fileType == FileType.Video) {
            sign = VIDEO_SIGN;
        }

        updateSign(appid, fileType, bucket, sign);
    }

    public void updateSign(String appid, FileType fileType, String bucket, String sign)
    {
        if (fileType == FileType.File) {
            FILE_SIGN = sign;
            FILE_BUCKET = bucket;
        }

        if (fileType == FileType.Photo) {
            PHOTO_SIGN = sign;
            PHOTO_BUCKET = bucket;
        }

        if (fileType == FileType.Video) {
            VIDEO_SIGN = sign;
            VIDEO_BUCKET = bucket;
        }

        APPID = appid;
        saveSign();
    }
}
