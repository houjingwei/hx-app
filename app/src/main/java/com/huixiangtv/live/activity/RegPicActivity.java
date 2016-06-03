package com.huixiangtv.live.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.PhotoAdapter;
import com.huixiangtv.live.model.DropImageModel;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.widget.SwitchPageControlView;
import com.huixiangtv.live.utils.widget.SwitchScrollLayout;

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

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regpic);
        x.view().inject(this);

        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.my_pic));

        try {
            ArrayList<String> mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
            int currentIndex = getIntent().getIntExtra("currentIndex",0);
            File file;
            mImageViews = new ImageView[5];
            for (int i = 0; i < mDatas.size(); i++) {
                file = new File(mDatas.get(i));
                Bitmap bm = BitmapHelper.zoomImg(BitmapHelper.readBitMap(file), App.screenWidth, App.screenHeight);
                ImageView imageView = new ImageView(this);
                mImageViews[i] = imageView;
                imageView.setImageBitmap(bm);

            }

            photoAdapter = new PhotoAdapter(this, mImageViews);

            vp_photos.setAdapter(photoAdapter);
            vp_photos.setCurrentItem(currentIndex);
        } catch (Exception ex) {

        }
    }


}
