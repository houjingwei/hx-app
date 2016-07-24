package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.GridViewGroupInfoAdapter;
import com.huixiangtv.liveshow.model.Dynamic;
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.widget.GridViewCircle;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stone on 16/7/18.
 */
public class GroupChatInfoActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.gv_group_info)
    GridViewCircle gridViewCircle;

    @ViewInject(R.id.rlAllMember)
    RelativeLayout rlAllMember;



    private GridViewGroupInfoAdapter adapter;
    private int page = 1;
    private String pageSize = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat_info);
        x.view().inject(this);
        initView();
        initData();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.group_chat_info));

        rlAllMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForwardUtils.target(GroupChatInfoActivity.this, Constant.GROUP_MEMBER_LIST_ACTIVITY, null);

            }
        });
    }



    protected void initData() {

        List<DynamicImage> list = new ArrayList<>();

        DynamicImage image1 = new DynamicImage();
        image1.setSmall("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1468833980&di=f493c3a5df641f1993e90d49f4ffe642&src=http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");

        DynamicImage image2 = new DynamicImage();
        image2.setSmall("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1468833980&di=f493c3a5df641f1993e90d49f4ffe642&src=http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");

        DynamicImage image3 = new DynamicImage();
        image3.setSmall("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1468833980&di=f493c3a5df641f1993e90d49f4ffe642&src=http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");

        DynamicImage image36 = new DynamicImage();
        image36.setSmall("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1468833980&di=f493c3a5df641f1993e90d49f4ffe642&src=http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");

        list.add(image1);

        list.add(image2);

        list.add(image3);

        list.add(image1);

        list.add(image2);

        list.add(image3);

        list.add(image36);


        adapter = new GridViewGroupInfoAdapter(GroupChatInfoActivity.this,list);
        gridViewCircle.setAdapter(adapter);

    }

}
