package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;

/**
 * Created by Stone on 16/5/16.
 */
public class SearchActivity extends BaseBackActivity implements View.OnClickListener{


    private TextView txTitle;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search);
        findView();
        initView();
    }

    private void findView(){
        txTitle = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
    }

    private void initView() {
        txTitle.setText(R.string.input_search);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

}
