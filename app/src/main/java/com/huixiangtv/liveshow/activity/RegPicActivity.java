package com.huixiangtv.liveshow.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.PhotoAdapter;
import com.huixiangtv.liveshow.utils.BitmapHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stone on 16/6/3.
 */
public class RegPicActivity extends BaseBackActivity {

    private PhotoAdapter photoAdapter;
    @ViewInject(R.id.vp_photos)
    ViewPager vp_photos;
    private ImageView[] mImageViews;
    ArrayList<Bitmap> mDatas;

    @ViewInject(R.id.img_back)
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = RegPicActivity.this.getWindow();
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_regpic);
        x.view().inject(this);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            mDatas = new ArrayList<>();

            mDatas.add(BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc1"))));

            mDatas.add(BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc2"))));

            mDatas.add(BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc3"))));

            mDatas.add(BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc4"))));

            mDatas.add(BitmapHelper.readBitMap(new File(App.getPreferencesValue("imgloc5"))));

            int currentIndex = getIntent().getIntExtra("currentIndex", 0);
            mImageViews = new ImageView[5];
            Bitmap bm;
            ImageView imageView;
            for (int i = 0; i < mDatas.size(); i++) {
                bm = mDatas.get(i);
                imageView = new ImageView(this);
                mImageViews[i] = imageView;
                imageView.setImageBitmap(bm);
            }
            photoAdapter = new PhotoAdapter(this, mImageViews);
            vp_photos.setAdapter(photoAdapter);
            vp_photos.setCurrentItem(currentIndex);
        } catch (Exception ex) {

        }

        //vp_photos.setPageTransformer(true,new RotateTransformer());
    }


}
