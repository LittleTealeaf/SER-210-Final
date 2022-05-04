package edu.quinnipiac.ser210.githubchat.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Basically a ListView that resizes itself in order to fit all of its children.
 * @author Thomas Kwashnak
 */
public class DynamicListView extends ListView {

    public DynamicListView(Context context) {
        super(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*
    Solution from https://stackoverflow.com/a/33961825/12206859
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
