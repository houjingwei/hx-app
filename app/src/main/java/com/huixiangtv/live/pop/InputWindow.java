package com.huixiangtv.live.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.KeyBoardUtils;


public class InputWindow extends BasePopupWindow implements OnClickListener {

    private static final String TAG = "InputWindow";
    private Activity context;
    private View view;
    RelativeLayout pl;

    EditText etChatMsg;





    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private View v;
    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        mHideAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
    }

    public InputWindow(Activity context, int width, int height) {
        this.context = context;
        this.v = view;
        super.setWidth(width);
        super.setHeight(height);
        initPopUpWindow();
        initAnimation();
        show();
    }

    private void show() {
        pl.setAnimation(mShowAnimation);
        pl.setVisibility(View.VISIBLE);
    }


    public void initPopUpWindow() {

        try {
            view = RelativeLayout.inflate(context, R.layout.pop_input, null);
            pl = (RelativeLayout) view.findViewById(R.id.pop_layout);
            pl.setOnClickListener(this);


            etChatMsg = (EditText) view.findViewById(R.id.etChatMsg);
            etChatMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    Log.i(TAG,i+"");
                    return false;
                }
            });
            super.setFocusable(true);
            super.setOutsideTouchable(true);
            super.setBackgroundDrawable(new BitmapDrawable());
            this.setContentView(view);
            this.setWidth(LayoutParams.FILL_PARENT);
            this.setHeight(LayoutParams.FILL_PARENT);
            this.setFocusable(true);
            this.setAnimationStyle(R.style.popupAnimation);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void showKeyBoard() {
        KeyBoardUtils.openKeybord(etChatMsg,context);
    }
}
