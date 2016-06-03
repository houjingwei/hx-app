package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.callback.CodeCallBack;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.pay.weichat.WeiChatConstants;
import com.huixiangtv.live.pay.weichat.WxPayUtils;
import com.huixiangtv.live.pop.SelectPicWayWindow;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.image.PictureHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/5/18.
 */
public class RegLiveMainActivity extends BaseBackActivity {



    @ViewInject(R.id.tvRegTitle)
    TextView tvRegTitle;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @ViewInject(R.id.txtHqyzm)
    TextView hqyzm;

    @ViewInject(R.id.txtPhone)
    TextView phone;

    @ViewInject(R.id.etJob)
    EditText etJob;


    @ViewInject(R.id.tvNext)
    TextView tvNext;


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
            ImageUtils.display(ivPhoto, picUri);
        }
    };
    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {


            case R.id.tvNext:
                popupLoginNotice(RegLiveMainActivity.this, RegLiveMainActivity.this);
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
            hqyzm.setText("(" + i + "秒)");
            hqyzm.setTextColor(Color.parseColor("#bcbcbc"));
            hqyzm.setBackgroundColor(getResources().getColor(R.color.white));

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


    public static void popupLoginNotice(final Activity activity, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("").setMessage(R.string.request_auth)
                .setPositiveButton("去设置艺人卡", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ForwardUtils.target(activity, Constant.PIC_LIST,null);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();

        alertDialog.show();
    }

}
