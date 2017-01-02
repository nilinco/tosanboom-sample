package co.nilin.tosanboomsample.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Navid on 9/18/16.
 */
public class MyLightTextView extends TextView {
    public MyLightTextView(Context context) {
        super(context);
        init();
    }

    public MyLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode())
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), "IRANSansMobile(FaNum)_Light.ttf"));
    }
}
