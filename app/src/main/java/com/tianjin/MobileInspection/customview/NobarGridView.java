package com.tianjin.MobileInspection.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by wuchang on 2016-12-13.
 */
public class NobarGridView extends GridView {

    public NobarGridView(Context context) {
        super(context);
    }

    public NobarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NobarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
