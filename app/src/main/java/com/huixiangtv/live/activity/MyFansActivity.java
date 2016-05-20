package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.MyFans;
import com.huixiangtv.live.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MyFansActivity extends BaseBackActivity {

    List<MyFans> fanses;
    MyFans myFans;
    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        x.view().inject(this);
        initview();
        loaddata();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myfans));
    }

    public void loaddata() {
       fanses = new ArrayList<MyFans>();
        for (int i=0;i<10;i++){
            myFans=new MyFans();
            myFans.setNickname("忘情水");
            myFans.setFansnumber("1234"+i);
            fanses.add(myFans);
        }
    }
}
