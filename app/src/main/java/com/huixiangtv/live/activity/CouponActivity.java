package com.huixiangtv.live.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import simbest.com.sharelib.ShareModel;

/**
 * Created by Stone on 16/6/30.
 */
public class CouponActivity  extends BaseBackActivity{


    @ViewInject(R.id.rbPhone)
    RadioButton rbPhone;


    @ViewInject(R.id.rbQq)
    RadioButton rbQq;


    //要分享的平台
    public int platform = 0;
    //是否定位
    public int local = 1;



    @ViewInject(R.id.et_code)
    EditText et_code;

    @ViewInject(R.id.rbQzone)
    RadioButton rbQzone;


    @ViewInject(R.id.rbWx)
    RadioButton rbWx;

    LinkedList<RadioButton> buttons;


    @ViewInject(R.id.rbCircle)
    RadioButton rbCircle;


    @ViewInject(R.id.rbSina)
    RadioButton rbSina;


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvSave)
    TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        x.view().inject(this);
        initView();
        setListener();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.coupon_title));
        commonTitle.imgShow(View.VISIBLE);
        ImageView commonTitleImg = commonTitle.getImg();
        commonTitleImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_left_pic));
        commonTitleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CouponActivity.this, "此功能暂未开通", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void setListener(){


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_code.getText()) && et_code.getText().toString().trim().length() > 0) {
                    bindShareSave(et_code.getText().toString());
                } else {
                    CommonHelper.showTip(getApplication(), "请输入优惠码");
                }
            }
        });



       rbPhone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               shareTo(1);
           }
       });


        rbQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo(2);
            }
        });




        rbQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo(3);
            }
        });




        rbWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo(4);
            }
        });




        rbCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo(5);
            }
        });


        rbSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo(6);
            }
        });




    }



    private void shareTo(int flag) {

        buttons = new LinkedList<RadioButton>();
        RadioButton[] btns = new RadioButton[]{rbPhone,rbQq,rbQzone,rbWx,rbCircle,rbSina};
        for (int i=0;i<btns.length;i++){
            buttons.add(i,btns[i]);
        }
        platform = flag;
        if(platform==1){
            setSharePlatformStyle(buttons,0);
        }else if(platform==2){
            setSharePlatformStyle(buttons,1);
        }else if(platform==3){
            setSharePlatformStyle(buttons,2);
        }else if(platform==4){
            setSharePlatformStyle(buttons, 3);
        }else if(platform==5){
            setSharePlatformStyle(buttons,4);
        }else if(platform==6){
            setSharePlatformStyle(buttons,5);
        }
    }






    /**
     * 设置radiobutton 选中或者不选中
     * @param buttons
     * @param flag
     */
    private void setSharePlatformStyle(List<RadioButton> buttons, int flag) {
        buttons.get(flag).setSelected(true);
        buttons.remove(flag);
        for (RadioButton rb : buttons) {
            rb.setSelected(false);
        }
    }

    private void bindShareSave(String shareCode) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("shareCode", shareCode);

        RequestUtils.sendPostRequest(Api.SHAREBINDSHARECODE, paramsMap, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String info) {
                if (info != null && info.toString().trim().length() > 0) {
                    popupShareNotice(CouponActivity.this,info);
                } else {
                    CommonHelper.showTip(getApplication(), "返回:" + info);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(getApplication(), "优惠码领取有误:" + e.getMessage());

            }
        }, String.class);
    }


    public static void popupShareNotice(final Context context,String info) {

        final AlertDialog dlg = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        dlg.show();
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width  = (int) (App.screenWidth * 0.75);
        window.setAttributes(lp);

        window.setContentView(R.layout.coupon_alert);
        window.findViewById(R.id.tvCLose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        window.findViewById(R.id.tvSeeLove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
            }
        });

        ((TextView)window.findViewById(R.id.tvInfo)).setText(info);
        dlg.show();

    }


    private void shareInfo(final SHARE_MEDIA platform, Activity activity, ShareModel model){

        if (platform.equals(SHARE_MEDIA.QZONE)) {

            CommonHelper.share(activity, model, SHARE_MEDIA.QZONE, 1, null);
        } else if (platform.equals(SHARE_MEDIA.QQ)) {
            CommonHelper.share(activity, model, SHARE_MEDIA.QQ, 1, null);

        } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
            CommonHelper.share(activity, model, SHARE_MEDIA.WEIXIN, 1, null);

        } else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {

            CommonHelper.share(activity, model, SHARE_MEDIA.WEIXIN_CIRCLE, 1, null);

        } else if (platform.equals(SHARE_MEDIA.SINA)) {
            CommonHelper.share(activity, model, SHARE_MEDIA.SINA, 1, null);
        }
    }
}
