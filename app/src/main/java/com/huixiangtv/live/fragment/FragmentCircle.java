package com.huixiangtv.live.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.adapter.FriendCircleAdapter;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCircle extends Fragment {

    private final String PAGESIZE = "10";
    View mRootView;
    private PullToRefreshListView refreshView;
    private FriendCircleAdapter adapter;
    int page = 1;
    private FrameLayout main;
    private LinearLayout commentLinear;
    private EditText commentEdit;		//评论输入框

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        return mRootView;
    }



    private void initView() {
        main = (FrameLayout) mRootView.findViewById(R.id.main);
        main.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });


        commentLinear = (LinearLayout) mRootView.findViewById(R.id.commentLinear);
        commentEdit = (EditText) mRootView.findViewById(R.id.commentEdit);
        refreshView = (PullToRefreshListView) mRootView.findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(getActivity()));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(getActivity()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);

        //点击进入自己的相册圈
        view.findViewById(R.id.ivPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.OWN_CIRCLE, null);
                }
            }
        });
        initHeadInfo(view);
        adapter = new FriendCircleAdapter(getActivity(),handler);
        refreshView.setAdapter(adapter);

        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        loadData();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        loadData();
                    }
                }, 1000);

            }
        });
        loadData();

    }

    private void loadData() {

        bindDynamicInfo();

    }


    private void initHeadInfo(View view) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        TextView tvNickName = (TextView) view.findViewById(R.id.tvNickName);
        if (null != App.getLoginUser()) {
            tvNickName.setText(App.getLoginUser().getNickName());
            ImageUtils.displayAvator(ivPhoto, App.getLoginUser().getPhoto());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    /**
     *  获取圈子列表
     */
    private void bindDynamicInfo() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", PAGESIZE);

        RequestUtils.sendPostRequest(Api.DYNAMIC_OWNDYNAMIC, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccessList(List<Dynamic> data) {
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }
                } else {
                    if (page == 1) {
                        CommonHelper.noData("暂无动态", refreshView.getRefreshableView(), getActivity(), 2);
                    }
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(getActivity(), "暂无动态:" + e.getMessage());
                refreshView.onRefreshComplete();

            }
        }, Dynamic.class);
    }



    private void onFocusChange(boolean hasFocus){
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        commentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
                }
            }
        }, 100);
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 10){
                commentLinear.setVisibility(View.VISIBLE);
                showChatInputView();// onFocusChange(true);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(commentLinear.getVisibility() == View.VISIBLE){
            commentLinear.setVisibility(View.GONE);
        }
    }



    // 状态栏的高度
    private int statusBarHeight = 0;

    private int myStautHeight() {
        Rect rectangle= new Rect();
        Window window= getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        return statusBarHeight;
    }


    // 软键盘的高度
    private int keyboardHeight = 0;
    // 软键盘的显示状态
    private boolean isShowKeyboard = false;

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            Rect r = new Rect();
            main.getWindowVisibleDisplayFrame(r);

            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = main.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            if(statusBarHeight==0) {
                statusBarHeight = myStautHeight();
            }
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight) {
                    isShowKeyboard = false;
                    onHideKeyboard();
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight) {
                    isShowKeyboard = true;
                    onShowKeyboard();
                }
            }
        }
    };



    private void onShowKeyboard() {
        commentLinear.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(commentLinear.getLayoutParams());
        //左上右下
        Log.i("rinima", App.screenHeight - keyboardHeight + "");
        int topY = App.screenHeight - keyboardHeight - WidgetUtil.dip2px(getActivity(), 15);
        margin.setMargins(0, topY, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        commentLinear.setLayoutParams(layoutParams);
        commentEdit.requestFocus();
    }


    private void onHideKeyboard() {
        commentLinear.setVisibility(View.GONE);
    }


    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(commentEdit, getActivity());
    }

    /**
     * 显示聊天区域
     */
    private void showChatInputView() {
        KeyBoardUtils.openKeybord(commentEdit, getActivity());
    }

}
