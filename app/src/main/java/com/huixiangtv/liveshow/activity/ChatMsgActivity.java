package com.huixiangtv.liveshow.activity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.ChatMsgAdapter;
import com.huixiangtv.liveshow.model.ChatMessage;
import com.huixiangtv.liveshow.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ChatMsgActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.refreshView)
    PullToRefreshListView refreshView;

    ChatMsgAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        x.view().inject(this);
        initView();
        loadData(); 
    }

    private void loadData() {
        List<ChatMessage> ls = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("myFans.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            ls = JSON.parseArray(stringBuilder.toString(),ChatMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }



        adapter.addList(ls);
    }

    private void initView() {
        adapter = new ChatMsgAdapter(ChatMsgActivity.this);
        refreshView.setMode(PullToRefreshBase.Mode.DISABLED);
        refreshView.setAdapter(adapter);
    }
}
