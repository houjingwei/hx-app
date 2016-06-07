package com.huixiangtv.live.utils.widget.pullView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.R;

/**
 * Created by Stone on 16/6/7.
 */
public class RefreshLoadMoreListView extends PullToRefreshListView {
    public RefreshLoadMoreListView(Context context) {
        super(context);
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLoadMoreListView(Context context, Mode mode) {
        super(context, mode);
    }

    public RefreshLoadMoreListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    public static RefreshLoadMoreListView newInstance(Context context) {

        RefreshLoadMoreListView refreshLoadMoreListView =
                (RefreshLoadMoreListView) LayoutInflater.from(context).
                        inflate(R.layout.layout_widget_refresh_load_more_view, null, false);
        return refreshLoadMoreListView;
    }
    public void onRefreshCompleteDelay(){
        onRefreshCompleteDelay(200);
    }
    public void onRefreshCompleteDelay(long delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshLoadMoreListView.this.onRefreshComplete();
            }
        },200);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setMode(Mode.BOTH);
        // 下拉刷新时的提示文本设置
        getLoadingLayoutProxy(true, false).setLastUpdatedLabel("下拉刷新");
        getLoadingLayoutProxy(true, false).setPullLabel("");
        getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
        // 上拉加载更多时的提示文本设置
        getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        getLoadingLayoutProxy(false, true).setPullLabel("");
        getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
    }

    public boolean isHeaderShown() {
        return getHeaderLayout().isShown();
    }

    public boolean isFooterShown() {
        return getFooterLayout().isShown();
    }
}

