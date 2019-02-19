package com.grantgz.listfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InterceptFrameLayout extends FrameLayout {
    private String TAG = "InterceptFrameLayout";
    public InterceptFrameLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mOnSlipActionListener!=null) {
            mOnSlipActionListener.OnSlipAction(event);
        }
        return super.onInterceptTouchEvent(event);
    }


    private OnMotionEventListener mOnSlipActionListener;
    public void setOnSlipActionListener(OnMotionEventListener listener){
        this.mOnSlipActionListener = listener;
    }
    public interface OnMotionEventListener {
        void OnSlipAction(MotionEvent event);
    }
}
