package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.fragment.FragmentCircle;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        FragmentCircle.clearViode();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }






}
