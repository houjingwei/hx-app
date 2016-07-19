package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Upfile;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.pop.SelectPicWayWindow;
import com.huixiangtv.liveshow.pop.UpdateSexWindow;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.KeyBoardUtils;
import com.huixiangtv.liveshow.utils.ShowUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.image.PictureHelper;
import com.tencent.upload.task.data.FileInfo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class MyInfoActivity extends BaseBackActivity implements View.OnClickListener {


    @ViewInject(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    TagAdapter<String> adapter ;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    private TextView tvSave;

    @ViewInject(R.id.tvSex)
    TextView tvSex;
    @ViewInject(R.id.etNickName)
    EditText etNickName;
    @ViewInject(R.id.etSignature)
    EditText etSignature;

    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;



    PictureHelper pictureHelper;

    String tagStr = "";
    String photo = null;

    ColaProgress cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        x.view().inject(this);
        initView();

        if(null!=App.getLoginUser()){
            setUserInfo(null,1);
        }
    }

    private void setUserInfo(String tags, int flag) {
        User user = App.getLoginUser();
        if(flag==1){
            photo = user.getPhoto();
            ImageUtils.displayAvator(ivPhoto, user.getPhoto());
        }
        etNickName.setText(user.getNickName());
        etSignature.setText(user.getSignature());
        tvSex.setText(user.getSex().equals("1")?"男":"女");
        String userTags = null;
        if(tags!=null){
            userTags = tags;
        }else{
            userTags  = user.getTags();
        }

        if(StringUtil.isNotEmpty(userTags)){
            initTags(userTags.split(","));
            adapter.notifyDataChanged();
            userTag();
        }
    }


    private void setUserTags(String tags) {
        if(StringUtil.isNotEmpty(tags)){
            initTags(tags.split(","));
            adapter.notifyDataChanged();
            userTag();
        }
    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText("修改个人资料");
        commonTitle.saveShow(View.VISIBLE);
        tvSave = commonTitle.getSave();
        tvSave.setOnClickListener(this);

        //隐藏键盘
        KeyBoardUtils.closeKeybord(etNickName,MyInfoActivity.this);

        findViewById(R.id.rl_tag).setOnClickListener(this);
        findViewById(R.id.rl_sex).setOnClickListener(this);
        ivPhoto.setOnClickListener(this);


        pictureHelper = new PictureHelper(this);
        pictureHelper.setOnSelectPicListener(selectPictureListener);
        pictureHelper.needCropPicture(true);//需要对图片进行裁剪。
    }

    private void initTags(String[] tags) {
        final LayoutInflater mInflater = LayoutInflater.from(MyInfoActivity.this);
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.my_tag,mFlowLayout, false);
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
                Map<String,String> tags = new HashMap<String,String>();
                tags.put("tags",tagStr);
                ForwardUtils.target(MyInfoActivity.this, Constant.USERTAG,tags);
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
            case R.id.save:
                saveUserInfo();
                break;


        }
    }

    /**
     * 用户标签字符串
     */
    private void userTag(){
        StringBuffer sb = new StringBuffer("");
        int tagCount = mFlowLayout.getAdapter().getCount();
        for (int i=0;i<tagCount;i++){
            String tag = (String) mFlowLayout.getAdapter().getItem(i);
            sb.append(tag+",");
        }
        if(sb.length()>0){
            tagStr = sb.substring(0,sb.length()-1);
        }
        Log.i("tagStr",tagStr);
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo() {
        if(TextUtils.isEmpty(etNickName.getText().toString())){
            ShowUtils.showTip(MyInfoActivity.this, "请设置昵称~");
        }
        cp = ColaProgress.show(MyInfoActivity.this, "正在保存", false, true, null);
        Map<String,String> params = new HashMap<String,String>();
        params.put("photo",photo);
        params.put("nickName",etNickName.getText().toString());
        params.put("sex",tvSex.getText().toString().equals("女")?0+"":1+"");
        params.put("signature", etSignature.getText().toString());
        params.put("tags", tagStr);
        RequestUtils.sendPostRequest(Api.SAVE_USER, params, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);
                if(null!=cp){
                    cp.dismiss();
                }
                App.saveLoginUser(data);
                CommonHelper.showTip(MyInfoActivity.this, "用户信息保存成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(MyInfoActivity.this, e.getMessage());
                if(null!=cp){
                    cp.dismiss();
                }
            }
        }, User.class);


    }

    private void upHeadPhoto() {
        SelectPicWayWindow selectPicWayWindow = new SelectPicWayWindow(MyInfoActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPicWayWindow.showAtLocation(MyInfoActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectPicWayWindow.update();
        selectPicWayWindow.setListener(new SelectPicWayWindow.SelectPicListener() {
            @Override
            public void select(final int select) {
                if (select == 0) {
                    pictureHelper.selectFrom(PictureHelper.FROM_FILE);
                } else {
                    pictureHelper.selectFrom(PictureHelper.FROM_CAMERA);

                    if ((ContextCompat.checkSelfPermission(MyInfoActivity.this, "android.permission.CAMERA"))!= PackageManager.PERMISSION_GRANTED ){
                        ActivityCompat.requestPermissions(MyInfoActivity.this,new String[]{"android.permission.CAMERA"},5);
                    }else{
                        pictureHelper.selectFrom(PictureHelper.FROM_CAMERA);
                    }


                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5:
                pictureHelper.selectFrom(PictureHelper.FROM_CAMERA);
                break;

        }
    }

    //选择性别。
    private void upSex() {
        UpdateSexWindow updateSexWindow = new UpdateSexWindow(MyInfoActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        updateSexWindow.showAtLocation(MyInfoActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            cp = ColaProgress.show(MyInfoActivity.this, "头像上传中", false, true, null);
            Map<String,String> params = new HashMap<String,String>();
            params.put("type","1");
            ImageUtils.upFileInfo(params,new ApiCallback<Upfile>() {
                @Override
                public void onSuccess(Upfile data) {
                    ImageUtils.upFile(MyInfoActivity.this,data, picUri, new ApiCallback<FileInfo>() {
                        @Override
                        public void onSuccess(FileInfo file) {
                            photo = file.url;
                            if(null!=cp){
                                cp.dismiss();
                            }
                            CommonHelper.showTip(MyInfoActivity.this,"头像上传成功");
                        }
                    });
                }
            });
        }
    };








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        pictureHelper.onActivityResult(requestCode, resultCode, arg2);

        if (resultCode == RESULT_OK && requestCode==108) {
            String userTags= arg2.getStringExtra("topic");
            setUserTags(userTags);
        }


    }




}
