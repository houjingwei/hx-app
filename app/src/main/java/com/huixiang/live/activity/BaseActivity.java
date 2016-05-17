package com.huixiang.live.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.oneapm.agent.android.module.analysis.AnalysisModule;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onResume() {
        super.onResume();
        AnalysisModule.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        AnalysisModule.onPause();
    }


}
