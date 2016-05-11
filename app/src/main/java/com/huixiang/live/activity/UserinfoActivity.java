package com.huixiang.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiang.live.R;
import com.huixiang.live.common.CommonUtil;
import com.huixiang.live.pop.SelectPicWayWindow;
import com.huixiang.live.pop.UpdateSexWindow;
import com.huixiang.live.utils.ForwardUtils;
import com.huixiang.live.utils.image.ImageUtils;
import com.huixiang.live.utils.image.PictureHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserinfoActivity extends BaseBackActivity implements View.OnClickListener {


    @ViewInject(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    TagAdapter<String> adapter ;

    @ViewInject(R.id.title)
    TextView txTitle;
    @ViewInject(R.id.save)
    TextView tvSave;
    @ViewInject(R.id.back)
    ImageView ivBack;

    @ViewInject(R.id.tvSex)
    TextView tvSex;
    @ViewInject(R.id.etNickName)
    EditText etNickName;

    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;




    PictureHelper pictureHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        x.view().inject(this);
        initView();
        initTags();
    }

    private void initView() {
        txTitle.setText(R.string.upUserInfo);
        ivBack.setOnClickListener(this);
        tvSave.setVisibility(View.VISIBLE);

        //隐藏键盘
        CommonUtil.hideKeyBoard(this,etNickName);

        findViewById(R.id.rl_tag).setOnClickListener(this);
        findViewById(R.id.rl_sex).setOnClickListener(this);
        ivPhoto.setOnClickListener(this);


        pictureHelper = new PictureHelper(this);
        pictureHelper.setOnSelectPicListener(selectPictureListener);
        pictureHelper.needCropPicture(true);//需要对图片进行裁剪。
    }

    String[] tags = null;
    private void initTags() {
        tags = new String[6];
        for (int i = 0; i < 6; i++) {
            tags[i] = "标签"+(i+1);
        }
        final LayoutInflater mInflater = LayoutInflater.from(UserinfoActivity.this);
        mFlowLayout.setMaxSelectCount(5);
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };

        mFlowLayout.setAdapter(adapter);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_tag:
                ForwardUtils.target(UserinfoActivity.this,"huixiang://userTag");
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.rl_sex:
                upSex();
                break;
            case R.id.ivPhoto:
                upHeadPhoto();
                break;
            

        }
    }

    private void upHeadPhoto() {
        SelectPicWayWindow selectPicWayWindow = new SelectPicWayWindow(UserinfoActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPicWayWindow.showAtLocation(UserinfoActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    //选择性别。
    private void upSex() {
        UpdateSexWindow updateSexWindow = new UpdateSexWindow(UserinfoActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        updateSexWindow.showAtLocation(UserinfoActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        updateSexWindow.update();
        updateSexWindow.setListener(new UpdateSexWindow.SelectSexListener() {
            @Override
            public void select(final int sex) {
                tvSex.setText(0 == sex ? "女" : "男");
            }
        });
    }




    private PictureHelper.OnSelectPicListener selectPictureListener = new PictureHelper.OnSelectPicListener() {
        @Override
        public void onSelectPicture(final String picUri, boolean finished) {
            if (!finished) {
                return;
            }
            ImageUtils.display(ivPhoto,picUri);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        pictureHelper.onActivityResult(requestCode, resultCode, arg2);
    }
}
