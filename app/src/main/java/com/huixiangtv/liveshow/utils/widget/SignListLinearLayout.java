package com.huixiangtv.liveshow.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;

/**
 * Created by Stone on 16/5/23.
 */
public class SignListLinearLayout extends LinearLayout {
    public SignListLinearLayout(Context context) {
        super(context);
    }

    private CharSequence valTitle;
    int valImage;
    private Context context;

    public SignListLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SignListLinearLayout);
        valTitle = a.getText(R.styleable.SignListLinearLayout_txtTitle);
        try {
            valImage =  a.getResourceId(R.styleable.SignListLinearLayout_valImage,-1);
        } catch (Exception ex) {
        }
        initView(context);
    }

    public SignListLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sign_list, this);

        TextView tvDayOne = (TextView) findViewById(R.id.tvDayOne);
        tvDayOne.setText(valTitle);

        ImageView ivsign = (ImageView) findViewById(R.id.ivsign);
        ivsign.setImageResource(valImage);


    }


}