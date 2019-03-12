package com.adazhdw.listfragment.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InterceptLayoutEx extends FrameLayout {
    private String TAG = "InterceptFrameLayout";
    public InterceptLayoutEx(@NonNull Context context) {
        super(context);
    }

    public InterceptLayoutEx(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLayoutEx(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public void setOnMotionEventListener(OnMotionEventListener listener){
        this.mOnSlipActionListener = listener;
    }
    public interface OnMotionEventListener {
        void OnSlipAction(MotionEvent event);
    }
}
