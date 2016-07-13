package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.pop.SelectPicWayWindow;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.image.PictureHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/5/17.
 */
public class RegLiveNextActivity extends BaseBackActivity  {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;

    @ViewInject(R.id.llPhone)
    LinearLayout llPhone;


    @ViewInject(R.id.rlcanvers)
    RelativeLayout rlcanvers;

    @ViewInject(R.id.tvNext)
    TextView tvNext;


    private PictureHelper pictureHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_live_next);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.txt_authentication));

        rlcanvers.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        pictureHelper = new PictureHelper(this);
        pictureHelper.setOnSelectPicListener(selectPictureListener);
        pictureHelper.needCropPicture(false);//不需要对图片进行裁剪。
    }

    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {
            case R.id.tvNext:
                ForwardUtils.target(RegLiveNextActivity.this, Constant.REG_LIVE_DES, null);
                break;

            case R.id.rlcanvers:
                upHeadPhoto();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    private void upHeadPhoto() {
        SelectPicWayWindow selectPicWayWindow = new SelectPicWayWindow(RegLiveNextActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPicWayWindow.showAtLocation(RegLiveNextActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectPicWayWindow.update();
        selectPicWayWindow.setListener(new SelectPicWayWindow.SelectPicListener() {
            @Override
            public void select(final int select) {
                if (select == 0) {
                    pictureHelper.selectFrom(PictureHelper.FROM_FILE);
                } else {
                    pictureHelper.selectFrom(PictureHelper.FROM_CAMERA);
                }
            }
        });
    }

    private PictureHelper.OnSelectPicListener selectPictureListener = new PictureHelper.OnSelectPicListener() {
        @Override
        public void onSelectPicture(final String picUri, boolean finished) {
            if (!finished) {
                return;
            }
            llPhone.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.VISIBLE);
            ImageUtils.display(ivPhoto, picUri);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        pictureHelper.onActivityResult(requestCode, resultCode, arg2);
    }
}
