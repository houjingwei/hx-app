package com.chanven.lib.cptr;

import android.content.Context;
import android.util.AttributeSet;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    private CustomHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new CustomHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);

    }

    public CustomHeader getHeader() {
        return mPtrClassicHeader;
    }


}
