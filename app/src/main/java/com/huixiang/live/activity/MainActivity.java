package com.huixiang.live.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiang.live.R;
import com.huixiang.live.fragment.FragmentTabOne;
import com.huixiang.live.fragment.FragmentTabThree;
import com.huixiang.live.fragment.FragmentTabTwo;
import com.huixiang.live.utils.widget.WidgetUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends BaseActivity {


    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    // 定义一个布局
    private LayoutInflater mLayoutInflater;

    // 定义数组来存放Fragment界面
    private Class mFragmentArray[] = { FragmentTabOne.class,
            FragmentTabTwo.class, FragmentTabThree.class};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = { R.drawable.main_item_tab1,
            R.drawable.main_item_tab2, R.drawable.main_item_tab3 };

    // Tab选项卡的文字
    private String mTextviewArray[] = { "tab1", "tab2", "tab3" };

    TextView txTitle;
    ImageView ivBack;
    LinearLayout llTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWindow();

        initView();
    }

    private void initWindow() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//           setTranslucentStatus(true);
//        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 初始化组件
     */
    private void initView() {
        llTitle = (LinearLayout) findViewById(R.id.llTitle);
        txTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);

        // 实例化布局对象
        mLayoutInflater = LayoutInflater.from(this);
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 得到fragment的个数
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            //去掉分割线
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        if(index==1){
            params.height = WidgetUtil.dip2px(this,40);
        }else{
            params.height = WidgetUtil.dip2px(this,24);
        }
        imageView.setLayoutParams(params);

        return view;
    }

    public void setTitleBar(String title){
        txTitle.setText(title);
        ivBack.setVisibility(View.GONE);
    }


    private long lastTipTimeMills = 0l;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTipTimeMills > 1000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTipTimeMills = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void hideTitle(boolean bool) {
        Window window = getWindow();
        if(bool){
            llTitle.setVisibility(View.GONE);
            window.setStatusBarColor(getResources().getColor(R.color.mainColor));
        }else{
            llTitle.setVisibility(View.VISIBLE);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
