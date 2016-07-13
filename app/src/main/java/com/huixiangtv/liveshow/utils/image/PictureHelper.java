package com.huixiangtv.liveshow.utils.image;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;


/**
 * Created by andrewlu on 2015/11/11.
 * 可以从相机/相册中选取图片.通过回调函数获取到所选择的图片.
 */
public class PictureHelper {
    private Activity context;
    public static final int FROM_CAMERA = 0x1000;
    public static final int FROM_FILE = 0x1001;
    public static final int FROM_VIDEO = 0x1002;
    public static final int CMD_CROP = 0x1003;

    private File cameraTargetFile;

    public PictureHelper(Activity context) {
        this.context = context;
        if (context == null) {
            throw new RuntimeException("PictureHelper context must not be null!");
        }
    }

    public void selectFrom(int from) {
        switch (from) {
            case FROM_CAMERA: {


                cameraTargetFile = createImageFile();
                //跳转到拍照界面.
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraTargetFile));
                context.startActivityForResult(intent, FROM_CAMERA);
                break;
            }
            case FROM_FILE: {
                //跳转到相册界面.
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                context.startActivityForResult(intent, FROM_FILE);
                break;
            }
            case FROM_VIDEO: {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
                intent.setAction(Intent.ACTION_GET_CONTENT);
                context.startActivityForResult(intent, FROM_VIDEO);
            }
        }
    }

    private boolean isNeedCropPicture = false;

    //在selectPicture之前设置，标示是否需要经过图片剪裁。
    public void needCropPicture(boolean needed) {
        isNeedCropPicture = needed;
    }

    private String mFilePath = null;

    //必须在activity中调用此方法.否则功能不正常.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (l == null) return;
        try {
            if (resultCode == Activity.RESULT_CANCELED) {
                l.onSelectPicture(null, false);
            } else if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case FROM_CAMERA: {
                        mFilePath = cameraTargetFile.getAbsolutePath();
                        if (isNeedCropPicture) {
                            startCrop();
                        } else {
                            l.onSelectPicture(mFilePath, true);
                        }
                        break;
                    }

                    case FROM_FILE: {

                        Uri selectedImage = data.getData();
                        if ("content".equalsIgnoreCase(selectedImage.getScheme())) {
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = context.getContentResolver().query(selectedImage, null, null, null, null);
                            if (c.moveToFirst()) {
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                mFilePath = c.getString(columnIndex);
                                if (isNeedCropPicture) {
                                    cameraTargetFile = createImageFile();
                                    startCrop();
                                } else {
                                    l.onSelectPicture(mFilePath, true);
                                }
                            } else {
                                l.onSelectPicture(mFilePath, false);
                            }
                            c.close();

                        } else if ("file".equalsIgnoreCase(selectedImage.getScheme())) {
                            mFilePath = selectedImage.getPath();
                            if (l != null) {
                                l.onSelectPicture(mFilePath, true);
                            }
                        }
                    }
                    break;
                    case CMD_CROP: {
                        mFilePath = cameraTargetFile.getAbsolutePath();
                        l.onSelectPicture(mFilePath, true);
                        break;
                    }
                    case FROM_VIDEO: {
                        Uri selectedVideo = data.getData();
                        if ("content".equalsIgnoreCase(selectedVideo.getScheme())) {
                            String[] filePathColumns = {MediaStore.Video.VideoColumns.DATA};
                            Cursor c = context.getContentResolver().query(selectedVideo, null, null, null, null);
                            if (c.moveToFirst()) {

                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                mFilePath = c.getString(columnIndex);

                                l.onSelectPicture(mFilePath, true);
                            } else {
                                l.onSelectPicture(mFilePath, false);
                            }
                            c.close();
                        } else if ("file".equalsIgnoreCase(selectedVideo.getScheme())) {
                            mFilePath = selectedVideo.getPath();
                            if (l != null) {
                                l.onSelectPicture(mFilePath, true);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(mFilePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", 100);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraTargetFile));
        context.startActivityForResult(intent, CMD_CROP);
    }

    private final File createImageFile() {
        //File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File target = new File(dir, Math.random() + ".jpg");
        return target;
    }

    private OnSelectPicListener l = null;

    public void setOnSelectPicListener(OnSelectPicListener l) {
        this.l = l;
    }

    //当选择了图片后执行此功能.
    public interface OnSelectPicListener {
        void onSelectPicture(String picUri, boolean finished);
    }
}
