package com.huixiang.live.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huixiang.live.R;

/**
 * Created by Stone on 16/5/17.
 */
public class MySeekBar extends LinearLayout{
    public MySeekBar(Context context) {
        super(context);
    }

    SeekBar seekBar;
    TextView tvHeightVal,tvName;
    private CharSequence textName,txtHeightVal,unit;
    private Integer processVal,changeVal;
    private Context context;
    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar);
        textName = a.getText(R.styleable.MySeekBar_textName);
        txtHeightVal = a.getText(R.styleable.MySeekBar_textHight);
        unit = a.getText(R.styleable.MySeekBar_unit);
        processVal = a.getInteger(R.styleable.MySeekBar_processVal, 0);
        changeVal = a.getInteger(R.styleable.MySeekBar_changeVal,1);
        initView(context);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seekbar_item_edit, this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvHeightVal = (TextView) findViewById(R.id.tvHeightVal);
        seekBar.setProgress(processVal);
        tvHeightVal.setText(txtHeightVal);
        tvName = (TextView) findViewById(R.id.tvName);
        seekBar.setOnSeekBarChangeListener(seekListener);
        tvName.setText(textName);
    }



    public void setTvNameVal(String text) {
        tvName.setText(text);
    }

    public void setTvHeightVal(String text) {
        tvHeightVal.setText(text);
    }

    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            tvHeightVal.setText("" + (progress*changeVal)+unit);

        }
    };
}