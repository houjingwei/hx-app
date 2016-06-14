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

        String path = getIntent().getStringExtra("path");cropImage1(path);
    }

    private void cropImage1(final String filePath)
    {
        BitmapDrawable bd = new BitmapDrawable(BitmapHelper.copressImage(filePath));


        setContentView(R.layout.fragment_cropimage);
        final CropImageView mCropImage=(CropImageView)findViewById(R.id.cropImg);
        mCropImage.setDrawable(bd,300,300);

//        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        //FileUtil.writeImage(mCropImage.getCropImage(), filePath, 100);
//                        Intent mIntent=new Intent();
//                        Bundle b = new Bundle();
//                        b.putParcelable("data", mCropImage.getCropImage());
//                        mIntent.putExtra("bundle", b);
//                        setResult(12, mIntent);
//                        finish();
//                    }
//                }).start();
//            }
//        });
    }


}
