package com.huixiangtv.liveshow.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.callback.CodeCallBack;
import com.huixiangtv.liveshow.common.CommonUtil;
import com.huixiangtv.liveshow.model.AuthInfo;
import com.huixiangtv.liveshow.model.Upfile;
import com.huixiangtv.liveshow.pop.SelectPicWayWindow;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.image.PictureHelper;
import com.tencent.upload.task.data.FileInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stone on 16/5/18.
 */
public class RegLiveMainActivity extends BaseBackActivity {


    private ColaProgress cp;

    @ViewInject(R.id.tvRegTitle)
    TextView tvRegTitle;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvName)
    EditText tvName;

    @ViewInject(R.id.txtHqyzm)
    TextView hqyzm;

    @ViewInject(R.id.txtPhone)
    TextView phone;

    @ViewInject(R.id.etNum)
    EditText etNum;

    @ViewInject(R.id.etCode)
    EditText etCode;

    @ViewInject(R.id.tvNext)
    TextView tvNext;

    @ViewInject(R.id.txtPhone)
    EditText txtPhone;

    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;

    @ViewInject(R.id.llPhone)
    LinearLayout llPhone;


    private PictureHelper pictureHelper;

    @ViewInject(R.id.rlcanvers)
    RelativeLayout rlcanvers;

    private boolean isyzm = true;
    private MyCount mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_main);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.txt_authentication));
        Drawable drawable = getResources().getDrawable(R.drawable.res_ioc);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvRegTitle.setCompoundDrawables(drawable, null, null, null);

        tvNext.setOnClickListener(this);
        hqyzm.setOnClickListener(this);
        rlcanvers.setOnClickListener(this);

        pictureHelper = new PictureHelper(this);
        pictureHelper.setOnSelectPicListener(selectPictureListener);
        pictureHelper.needCropPicture(false);//不需要对图片进行裁剪。
        setListener();
    }

    private void setListener(){

        phone.addTextChangedListener(new EditChangedListener());
    }

    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(s.toString().trim().length()==0)
            {
                hqyzm.setEnabled(false);
                hqyzm.setBackgroundResource(R.drawable.button_radius_no);
            }
            else {
                hqyzm.setEnabled(true);
                hqyzm.setBackgroundResource(R.drawable.button_radius);
            }

        }
    }

    private void upHeadPhoto() {
        SelectPicWayWindow selectPicWayWindow = new SelectPicWayWindow(RegLiveMainActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPicWayWindow.showAtLocation(RegLiveMainActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            ivPhoto.setTag(picUri);
            ImageUtils.display(ivPhoto, picUri);
        }
    };
    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {


            case R.id.tvNext:

                if(null==ivPhoto.getTag() || TextUtils.isEmpty(ivPhoto.getTag().toString())){
                    CommonHelper.showTip(getApplication(), "请上传证件图片");
                    return ;
                }


                if(TextUtils.isEmpty(tvName.getText().toString())){
                    CommonHelper.showTip(getApplication(), "请填写姓名");
                    return ;
                }


                if(TextUtils.isEmpty(etNum.getText().toString())){
                    CommonHelper.showTip(getApplication(), "请填写身份证号码");
                    return ;
                }


                if(TextUtils.isEmpty(txtPhone.getText().toString())){
                    CommonHelper.showTip(getApplication(), "请填写手机号");
                    return ;
                }



                if(TextUtils.isEmpty(etCode.getText().toString())){
                    CommonHelper.showTip(getApplication(), "请填写验证码");
                    return ;
                }

                AuthInfo authInfo = new AuthInfo();
                authInfo.setName(tvName.getText().toString());
                authInfo.setAuthNum(etNum.getText().toString());
                authInfo.setAuthType("1");
                authInfo.setImage(ivPhoto.getTag().toString());
                authInfo.setCode(etCode.getText().toString());
                authInfo.setPhoto(txtPhone.getText().toString());
                cp = ColaProgress.show(RegLiveMainActivity.this, "正在保存...", true, false, null);
                upEveryPhoto(authInfo);

                break;

            case R.id.txtHqyzm:
                if (CommonUtil.isMobileNum(phone.getText().toString()) && isyzm) {
                    isyzm = false;
                    CommonUtil.getMsgCode(phone.getText().toString(),new CodeCallBack(){
                                @Override
                                public void sendSuccess() {

                                }
                                @Override
                                public void sendError(String msg) {

                                    Toast.makeText(getBaseContext(), "验证码发送失败，失败原因：" + msg,Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                    hqyzm.setEnabled(false);
                    if (mc == null) {
                        // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
                        mc = new MyCount(60 * 1000, 1000);
                    }
                    mc.start();
                }
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





    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            hqyzm.setText("重新获取");
            hqyzm.setTextColor(Color.parseColor("#FFFFFF"));
            hqyzm.setBackgroundResource(R.drawable.button_radius);
            isyzm = true;
            hqyzm.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int i = (int) millisUntilFinished / 1000;
            hqyzm.setText("重新获取 (" + i + ")秒");
            hqyzm.setEnabled(false);
            hqyzm.setBackgroundResource(R.drawable.button_radius_no);

        }
    }


//    private void startWxPay(String appId, String prepayId, String nonceStr, String partnerId,
//                            String timeStamp, String packageValue, String paySignature) {
//        WeiChatConstants.APP_ID = appId;
//        WxPayUtils.pay(RegLiveMainActivity.this, appId, prepayId, nonceStr, partnerId, timeStamp, packageValue, paySignature);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        pictureHelper.onActivityResult(requestCode, resultCode, arg2);
    }


    public static void popupLoginNotice(final Context context) {

        final AlertDialog dlg = new AlertDialog.Builder(context,R.style.Theme_Dialog_From_Bottom).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.card_alert, null);
        dlg.setView(layout);
        dlg.show();
        dlg.setCancelable(false);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.card_alert);
        window.findViewById(R.id.tvGoSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target((Activity)context, Constant.PIC_LIST, null);
            }
        });
        window.findViewById(R.id.tvCLose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                ((Activity)context).finish();
            }
        });




    }

    private void setAuthInfo(AuthInfo authInfo,String image) {
        authInfo.setImage(image);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("name", authInfo.getName());
        paramsMap.put("authType", authInfo.getAuthType());
        paramsMap.put("authNum", authInfo.getAuthNum());
        paramsMap.put("phone", authInfo.getPhoto());
        paramsMap.put("image", authInfo.getImage());
        paramsMap.put("code", authInfo.getCode());

        RequestUtils.sendPostRequest(Api.SETAUTHINFO, paramsMap, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if(cp.isShowing())
                    cp.dismiss();
                popupLoginNotice(RegLiveMainActivity.this);


            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(cp.isShowing())
                    cp.dismiss();
                CommonHelper.showTip(getApplication(), e.getMessage());

            }
        }, String.class);
    }


    private void upEveryPhoto(final AuthInfo authInfo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        ImageUtils.upFileInfo(params, new ApiCallback<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
                ImageUtils.upFile(RegLiveMainActivity.this, data, authInfo.getImage(), new ApiCallback<FileInfo>() {
                    @Override
                    public void onSuccess(FileInfo file) {

                        setAuthInfo(authInfo,file.url);
                    }
                });
            }
        });
    }


}
