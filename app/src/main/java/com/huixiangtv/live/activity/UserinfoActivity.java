package com.huixiangtv.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Upfeile;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.pop.SelectPicWayWindow;
import com.huixiangtv.live.pop.UpdateSexWindow;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.ShowUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.image.PictureHelper;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserinfoActivity extends BaseBackActivity implements View.OnClickListener {


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

    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;

    PictureHelper pictureHelper;

    String tagStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        x.view().inject(this);
        initView();
        if(null!=App.getLoginUser()){
            setUserInfo();
        }
    }

    private void setUserInfo() {
        User user = App.getLoginUser();
        ImageUtils.displayAvator(ivPhoto,user.getPhoto());
        etNickName.setText(user.getNickName());
        tvSex.setText(user.getSex());
        String userTags = user.getTags();
        userTags = "小清新,校花,大美女";
        if(StringUtil.isNotEmpty(userTags)){
            initTags(userTags.split(","));

            getTag();
        }


    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText("修改个人资料");
        commonTitle.saveShow(View.VISIBLE);
        tvSave = commonTitle.getSave();
        tvSave.setOnClickListener(this);

        //隐藏键盘
        KeyBoardUtils.closeKeybord(etNickName,UserinfoActivity.this);

        findViewById(R.id.rl_tag).setOnClickListener(this);
        findViewById(R.id.rl_sex).setOnClickListener(this);
        ivPhoto.setOnClickListener(this);


        pictureHelper = new PictureHelper(this);
        pictureHelper.setOnSelectPicListener(selectPictureListener);
        pictureHelper.needCropPicture(true);//需要对图片进行裁剪。
    }

    private void initTags(String[] tags) {
        final LayoutInflater mInflater = LayoutInflater.from(UserinfoActivity.this);
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
                ForwardUtils.target(UserinfoActivity.this,"huixiang://userTag",tags);
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
    private void getTag(){
        StringBuffer sb = new StringBuffer("");
        int tagCount = mFlowLayout.getAdapter().getCount();
        for (int i=0;i<tagCount;i++){
            String tag = (String) mFlowLayout.getAdapter().getItem(i);
            sb.append(tag+",");
        }
        if(sb.length()>0){
            tagStr = sb.substring(0,sb.length()-1);
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo() {
        if(TextUtils.isEmpty(etNickName.getText().toString())){
            ShowUtils.showTip(UserinfoActivity.this, "请设置昵称~");
        }
        Map<String,String> params = new HashMap<String,String>();
        params.put("tags",tagStr);
        params.put("photo","photo");
        params.put("nickName",etNickName.getText().toString());
        params.put("sex",tvSex.getText().toString());
        params.put("signature","");
        RequestUtils.sendPostRequest(Api.SAVE_USER, params, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);
                App.saveLoginUser(data);
                CommonHelper.showTip(UserinfoActivity.this,"用户信息保存成功");
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(UserinfoActivity.this,e.getMessage());
            }
        },User.class);


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
            upFileInfo(new ApiCallback<Upfeile>() {
                @Override
                public void onSuccess(Upfeile data) {
                    upFile(data,picUri);
                }
            });
        }
    };

    private void upFileInfo( final ApiCallback<Upfeile> apiCallback) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("type","1");
        RequestUtils.sendPostRequest(Api.UPLOAD_FILE_INFO, params, new ResponseCallBack<Upfeile>() {
            @Override
            public void onSuccess(Upfeile data) {
                super.onSuccess(data);
                if(null!=data){
                    apiCallback.onSuccess(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        },Upfeile.class);
//        ImageUtils.display(ivPhoto,picUri);
    }


    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private void upFile(Upfeile data, String picUri) {

        UploadManager photoUploadMgr  = new UploadManager(UserinfoActivity.this, data.getAppId(), Const.FileType.Photo, data.getPersistenceId());
        PhotoUploadTask task = new PhotoUploadTask(picUri, new IUploadTaskListener() {

            @Override
            public void onUploadSucceed(final FileInfo result) {

                mMainHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        Log.i("xxx", "upload succeed: " + result.url);

                    }
                });

            }

            @Override
            public void onUploadStateChange(ITask.TaskState state) {


            }

            @Override
            public void onUploadProgress(final long totalSize, final long sendSize) {

            }

            @Override
            public void onUploadFailed(final int errorCode, final String errorMsg) {

            }
        });
        task.setBucket(data.getBucket());  // 设置Bucket(命名空间)
        task.setFileId("test_fileId_" + UUID.randomUUID()); // 可以为图片自定义FileID(可选)
        task.setAuth(data.getSig());
        photoUploadMgr.upload(task);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        pictureHelper.onActivityResult(requestCode, resultCode, arg2);
    }
}
