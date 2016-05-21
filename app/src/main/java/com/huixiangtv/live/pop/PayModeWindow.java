package com.huixiangtv.live.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.AccountActivity;
import com.huixiangtv.live.adapter.PayModeAdapter;
import com.huixiangtv.live.model.Coin;
import com.huixiangtv.live.model.PayMode;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.List;

/**
 * Created by hjw on 2016/5/21.
 */
public class PayModeWindow extends BasePopupWindow implements View.OnClickListener {

    private ListView listView;

    private Activity context;
    private View view;
    private Coin coin;
    ScrollView bottom;
    TextView tvClose;
    TextView tvTip1;
    TextView tvTip2;
    SelectPayModeListener listener;
    private List<PayMode> modeData;

    private Animation mShowAnimation;

    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
    }

    public PayModeWindow(Activity context, int width, int height, List<PayMode> data, Coin coin) {
        this.context = context;
        this.modeData = data;
        this.coin = coin;
        super.setWidth(width);
        super.setHeight(height);
        initPopUpWindow();
        initAnimation();
        show();
    }

    private void show() {
        bottom.setAnimation(mShowAnimation);
        bottom.setVisibility(View.VISIBLE);
    }



    public void initPopUpWindow() {
        view = RelativeLayout.inflate(context, R.layout.pop_pay_mode, null);
        listView = (ListView) view.findViewById(R.id.list_view);


        tvTip1= (TextView) view.findViewById(R.id.tvTip1);
        tvTip1.setText("充值"+coin.getName());
        tvTip2 = (TextView) view.findViewById(R.id.tvTip2);
        tvTip2.setText("支付"+coin.getPrice()+"元");
        tvClose = (TextView) view.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
        loadData();
        bottom = (ScrollView) view.findViewById(R.id.bottom);
        view.findViewById(R.id.pop_layout).setOnClickListener(this);
        super.setFocusable(true);
        super.setOutsideTouchable(true);
        super.setBackgroundDrawable(new BitmapDrawable());
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popupAnimation);
    }


    PayModeAdapter apapter ;
    private void loadData() {
        apapter = new PayModeAdapter(context);
        apapter.addList(modeData);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        params.height = WidgetUtil.dip2px(context, 50 * modeData.size());
        listView.setLayoutParams(params);
        listView.setAdapter(apapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayMode payMode = (PayMode) apapter.getItem(position);
                listener.select(payMode);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            case R.id.tvClose:
                dismiss();
                break;
            default:
                break;
        }
    }


    public static abstract class SelectPayModeListener {
        public void select(PayMode mode) {

        }
    }


    public SelectPayModeListener getListener() {
        return listener;
    }

    public void setListener(SelectPayModeListener listener) {
        this.listener = listener;
    }
}
