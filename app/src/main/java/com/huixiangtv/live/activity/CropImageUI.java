package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.image.CropImageView;

/**
 * Created by Stone on 16/6/13.
 */
public class CropImageUI extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width_int = 300;
        int height_int = 300;
        try {
            width_int = getIntent().getIntExtra("width",300);
            height_int = getIntent().getIntExtra("height",300);

        }
        catch (Exception ex)
        {

        }

        String path = getIntent().getStringExtra("path");
        cropImage1(path,width_int,height_int);
    }

    private void cropImage1(final String filePath,final int width,final int height) {
        BitmapDrawable bd = new BitmapDrawable(BitmapHelper.copressImage(filePath));


        setContentView(R.layout.fragment_cropimage);
        final CropImageView mCropImage = (CropImageView) findViewById(R.id.cropImg);
        mCropImage.setDrawable(bd, width, height);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Intent mIntent=new Intent();
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


}
