package com.huixiangtv.live.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Stone on 16/5/23.
 */
public class UpdateApp {

    private Context mContext;
    // 返回的安装包url
    private String apkUrl = "";
    private String uplog = "";
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/huixiangdata/";
    private static final String saveFileName = savePath + "HuiXiang.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private ProgressDialog pd;
    private int progress;
    private boolean interceptFlag = false;

    public UpdateApp(Context context) {
        this.mContext = context;
    }

    public boolean judgeVersion(String tab, String url,
                                String uplog) {

        apkUrl = url;
        this.uplog = uplog;
        if (tab.equals("-1")) {// -1.选择更新;1,强制更新
            showNoticeLayoutDialog();
            return true;
        } else if (tab.equals("1")) {
            showDownloadDialog("1");
            return true;
        }
        else
        {
            return false;
        }
    }


    private void showNoticeLayoutDialog()
    {

        final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
        dlg.show();
        dlg.setCancelable(false);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.process_version_new);
        ImageView btnClosed = (ImageView) window.findViewById(R.id.btnClosed);
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });


        TextView tvLevelEv = (TextView) window.findViewById(R.id.tvLevelEv);
        tvLevelEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadDialog("2");  //开始更新ing
            }
        });

    }


    protected void showDownloadDialog(String status) {
        new VProgressDialog(mContext, apkUrl, uplog, status).show();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    pd.setProgress(progress);
                    break;
                case DOWN_OVER:
                    pd.dismiss();
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    // 安装apk
    protected void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}