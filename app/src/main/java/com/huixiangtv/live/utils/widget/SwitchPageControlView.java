package com.huixiangtv.live.utils.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/**
 * Created by Stone on 16/5/27.
 */
public class SwitchPageControlView extends LinearLayout {
    private Context context;

    private int count;

    public void bindScrollViewGroup(SwitchScrollLayout scrollViewGroup) {
        this.count=scrollViewGroup.getChildCount();
        generatePageControl(scrollViewGroup.getCurrentScreenIndex());

        scrollViewGroup.setOnScreenChangeListener(new SwitchScrollLayout.OnScreenChangeListener() {

            public void onScreenChange(int currentIndex) {
                generatePageControl(currentIndex);
            }
        });
    }

    public SwitchPageControlView(Context context) {
        super(context);
        this.init(context);
    }
    public SwitchPageControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.context=context;
    }

    private void generatePageControl(int currentIndex) {
        this.removeAllViews();

        int pageNum = 6;
        int pageNo = currentIndex+1;
        int pageSum = this.count;


        if(pageSum>1){
            int currentNum = (pageNo % pageNum == 0 ? (pageNo / pageNum) - 1
                    : (int) (pageNo / pageNum))
                    * pageNum;

            if (currentNum < 0)
                currentNum = 0;

            if (pageNo > pageNum){
            }



            for (int i = 0; i < pageNum; i++) {
                if ((currentNum + i + 1) > pageSum || pageSum < 2)
                    break;
            }

            if (pageSum > (currentNum + pageNum)) {
            }
        }
    }
}

