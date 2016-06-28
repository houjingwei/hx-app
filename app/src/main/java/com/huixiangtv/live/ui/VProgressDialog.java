package com.huixiangtv.live.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.activity.SplashActivity;
import com.huixiangtv.live.common.CommonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stone on 16/5/23.
 */
public class VProgressDialog extends Dialog {
    private ProgressBar pb;
    private int state=0;
    private Context mContext;
    // 返回的安装包url
    private String apkUrl = "";
    private String uplog="";
    /* 下载包安装路径 */
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private int progress;
    private TextView tv_progress,tv_durrent,tv_log,tvLevelEv;
    private ImageView ivClose;
    private RelativeLayout rlLevelInfo;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final String savePath ="/sdcard/huixiangdata/";
    private static final String saveFileName = savePath + "HuiXiang.apk";
    public VProgressDialog(final Activity activity,final Context context,String apkUrl,String uplog,final String status) {
        super(context, R.style.Dialog);
        setContentView(R.layout.process_version_new);
        this.mContext=context;
        this.apkUrl=apkUrl;
        this.uplog=uplog;
        this.setCanceledOnTouchOutside(false);
        tvLevelEv = (TextView) findViewById(R.id.tvLevelEv);
//        rlLevelInfo = (RelativeLayout) findViewById(R.id.rlLevelInfo);
        tvLevelEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLevelEv.setClickable(false);
                ivClose.setVisibility(View.GONE);
                tvLevelEv.setText("正在升级");
                downLoadThread = new Thread(mdownApkRunnable);
                downLoadThread.start();
            }
        });

        this.setCancelable(false);
        ivClose = (ImageView) findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equals("1")){
                     dimiss1();
                    ((MainActivity)activity).updateClose();
                    App.setUpdateInDate(context, App.sdf.format(new Date()));

                }
                else
                {
                    App.setUpdateInDate(context, App.sdf.format(new Date()));
                    dimiss1();
                }
            }
        });

        tv_log=(TextView) findViewById(R.id.txtLog);
        String logSp[];
        if(uplog.length()>0)
        {
            logSp =  uplog.split("\\|");
            uplog="";
            for(String str :logSp)
            {
                uplog += str+"\n";
            }

        }

        tv_log.setText(uplog);

        pb=(ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
//        if(status.equals("1"))
//        {
//            tvLevelEv.setClickable(false);
//            ivClose.setVisibility(View.GONE);
//            tvLevelEv.setText("正在升级");
//            downLoadThread = new Thread(mdownApkRunnable);
//            downLoadThread.start();
//        }
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
    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    pb.setProgress(progress);
                    break;
                case DOWN_OVER:
                    dimiss1();
                    installApk();
                    break;
                default:
                    break;
            }
        }

    };


    private void dimiss1(){
        this.dismiss();
    }
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
