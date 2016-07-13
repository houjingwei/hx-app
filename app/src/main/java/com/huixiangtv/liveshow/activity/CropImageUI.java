package com.huixiangtv.liveshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.utils.BitmapHelper;
import com.huixiangtv.liveshow.utils.image.CropImageView;

import java.io.File;

/**
 * Created by Stone on 16/6/13.
 */
public class CropImageUI extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = CropImageUI.this.getWindow();
        window.setFlags(flag, flag);
        int width_int = 300;
        int currentTag = 0;
        int height_int = 300;
        try {
            width_int = getIntent().getIntExtra("width", App.screenWidth);
            height_int = getIntent().getIntExtra("height", App.screenHeight);
            currentTag = getIntent().getIntExtra("currentTag",0);

        } catch (Exception ex) {

        }
        cropImage1(currentTag, width_int, height_int);
    }

    private void cropImage1(int currentTag, final int width, final int height) {
        BitmapDrawable bd = showLocalPic(currentTag);
        setContentView(R.layout.fragment_cropimage);
        final CropImageView mCropImage = (CropImageView) findViewById(R.id.cropImg);
        mCropImage.setDrawable(bd, width, height);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent mIntent = new Intent();
                        Bundle b = new Bundle();
                        b.putParcelable("data", mCropImage.getCropImage());
                        mIntent.putExtra("bundle", b);
                        setResult(12, mIntent);
                        finish();
                    }
                }).start();
            }
        });
    }


    private BitmapDrawable showLocalPic(int currentTag) {

        Bitmap bm = null;
        try {

            if (currentTag == 0) {
                bm = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc1")), false);
            } else if (currentTag == 1) {
                bm = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc2")), false);
            } else if (currentTag == 2) {
                bm = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc3")), false);
            } else if (currentTag == 3) {
                bm = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc4")), false);
            } else if (currentTag == 4) {
                bm = BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc5")), false);
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return new BitmapDrawable(bm);


    }


}
