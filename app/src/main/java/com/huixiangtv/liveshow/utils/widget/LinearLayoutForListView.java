package com.huixiangtv.liveshow.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.huixiangtv.liveshow.R;
/**
 * Created by Stone on 16/5/14.
 */
public class LinearLayoutForListView<B> extends LinearLayout {
    private BaseAdapter adapter;
    private OnItemClickListener<B> onItemClickListener;
    private int devider;
    private int dirviderHeight;
    private Context context;
    public LinearLayoutForListView(Context context) {
        this(context,null);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LinearLayoutForListView, defStyleAttr, 0);
        devider=a.getColor(R.styleable.LinearLayoutForListView_mydivider, Color.BLACK);
//    	a.getDimensionPixelSize(
//    			R.styleable.LinearLayoutForListView_mydividerHeight, (int) TypedValue
//						.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//								dirviderHeight, getResources()
//										.getDisplayMetrics()));
        dirviderHeight=(int) a.getDimension(R.styleable.LinearLayoutForListView_mydividerHeight,0.0f);
        a.recycle();
    }
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        // setAdapter 时添加 view
        bindView();
    }

    public void setOnItemClickListener(OnItemClickListener<B> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    private View createDirvider(){
        View view=new View(context);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dirviderHeight));
        view.setBackgroundColor(devider);
        return view;
    }
    /**
     * 绑定 adapter 中所有的 view
     */
    public void bindView() {
        if (adapter == null) {
            return;
        }
       /// removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            final View v = adapter.getView(i,null,this);
            final int tmp = i;
            final B item = (B) adapter.getItem(i);

            // view 点击事件触发时回调我们自己的接口
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(v, item, tmp);
                    }
                }
            });
            addView(v);
            if(i<adapter.getCount()-1){
                addView(createDirvider());
            }
        }
    }

    /**
     *
     * 回调接口
     */
    public interface OnItemClickListener<B> {
        /**
         *
         * @param v
         *            点击的 view
         * @param
         *            点击的 view 所绑定的对象
         * @param position
         *            点击位置的 index
         */
        void onItemClicked(View v, B item, int position);
    }
}