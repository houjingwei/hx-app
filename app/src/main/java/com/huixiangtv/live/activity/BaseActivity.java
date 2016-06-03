package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.oneapm.agent.android.module.analysis.AnalysisModule;

import java.util.Calendar;

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
