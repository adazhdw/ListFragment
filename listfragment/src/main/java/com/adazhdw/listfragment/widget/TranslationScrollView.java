package com.adazhdw.listfragment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.widget.NestedScrollView;
import com.adazhdw.listfragment.R;

public class TranslationScrollView extends NestedScrollView {

    private final String TAG = "TranslationScrollView";
    private boolean mEnableTopRebound = true;
    private boolean mEnableBottomRebound = true;
    private OnReboundEndListener mOnReboundEndListener;
    private ViewGroup mContentView;
    private Rect mRect = new Rect();

    private int mLoadingHeight = 200;
    private int lastY;
    private boolean rebound = false;
    private int reboundDirection = 0; //<0 表示下部回弹 >0 表示上部回弹 0表示不回弹
    private long reboundDuration = 300;

    private int showDistance = 200;
    private View mLoadingView;
    private double scrollOffset = 0.30;
    private float oldLoadingTransY = 0;
    private float mLastOffset = 0;

    public TranslationScrollView(Context context) {
        super(context,null);
    }

    public TranslationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TranslationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TranslationScrollView);
        mEnableTopRebound = mTypedArray.getBoolean(R.styleable.TranslationScrollView_tsv_top_rebound_enabled,true);
        mEnableBottomRebound = mTypedArray.getBoolean(R.styleable.TranslationScrollView_tsv_bottom_rebound_enabled,true);
        scrollOffset = mTypedArray.getFloat(R.styleable.TranslationScrollView_tsv_scroll_offset, (float) 0.30);
        reboundDuration = mTypedArray.getInteger(R.styleable.TranslationScrollView_tsv_rebound_duration, 300);
        showDistance = (int) mTypedArray.getDimension(R.styleable.TranslationScrollView_tsv_show_distance, 200);
        mLoadingHeight = (int) mTypedArray.getDimension(R.styleable.TranslationScrollView_tsv_loading_height, 200);
        mTypedArray.recycle();
    }

    /**
     * after inflating view, we can get the width and height of view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View childView = getChildAt(0);
        if (childView == null) {
            return;
        }
        if (childView instanceof ViewGroup) {
            mContentView = (ViewGroup) childView;
        }
    }

    public void setLoadingView(View view) {

        mLoadingView = view;
        mLoadingView.setTranslationY(200);
        if (mLoadingView != null) {
            oldLoadingTransY = mLoadingView.getTranslationY();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mContentView == null) return;
        // to remember the location of mContentView
        mRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
    }

    public TranslationScrollView setOnReboundEndListener(OnReboundEndListener onReboundEndListener) {
        this.mOnReboundEndListener = onReboundEndListener;
        return this;

    }

    public TranslationScrollView setEnableTopRebound(boolean enableTopRebound) {
        this.mEnableTopRebound = enableTopRebound;
        return this;
    }

    public TranslationScrollView setEnableBottomRebound(boolean mEnableBottomRebound) {
        this.mEnableBottomRebound = mEnableBottomRebound;
        return this;
    }

    public void setShowDistance(int showDistance) {
        this.showDistance = showDistance;
    }

    public void setReboundDuration(long reboundDuration) {
        this.reboundDuration = reboundDuration;
    }

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public void setLoadingHeight(int mLoadingHeight) {
        this.mLoadingHeight = mLoadingHeight;
    }

    public void resetAnim() {
        if (mLoadingView == null) return;
        mLoadingView.animate()
                .translationY(oldLoadingTransY)
                .setDuration(reboundDuration)
                .start();
        mContentView.animate()
                .translationY(0)
                .setDuration(reboundDuration)
                .start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mContentView == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isScrollToTop() && !isScrollToBottom()) {
                    lastY = (int) ev.getY();
                    mLastOffset = 0;
                    break;
                }
                //处于顶部或者底部
                int deltaY = (int) (ev.getY() - lastY);
                //deltaY > 0 下拉 deltaY < 0 上拉

                //disable top or bottom rebound
                if ((!mEnableTopRebound && deltaY > 0) || (!mEnableBottomRebound && deltaY < 0)) {
                    break;
                }

                int offset = (int) (deltaY * scrollOffset);
                if (mContentView == null) break;
                // 移动布局
                mContentView.setTranslationY(mRect.top + offset);
                rebound = true;
                mLastOffset = offset;
                break;

            case MotionEvent.ACTION_UP:
                if (!rebound) break;
                if (mContentView == null || mLoadingView == null) break;
                reboundDirection = (int) (mContentView.getTranslationY() - mRect.top);

                /*mLoadingView.animate()
                        .translationY(0)
                        .setDuration(reboundDuration)
                        .start();*/
                // 移动布局
                int needY = 0;
                if (Math.abs(mLastOffset) > showDistance) {
                    needY = -mLoadingHeight;
                }
                final int finalNeedY = needY;
                mContentView.animate()
                        .translationY(finalNeedY)
                        .setDuration(reboundDuration)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                if (mOnReboundEndListener != null && finalNeedY != 0) {
                                    if (reboundDirection > 0) {
                                        mOnReboundEndListener.onReboundTopComplete();
                                    }
                                    if (reboundDirection < 0) {
                                        mOnReboundEndListener.onReboundBottomComplete();
                                    }
                                    mLoadingView.setTranslationY(0);
                                    reboundDirection = 0;
                                }
                            }
                        })
                        .start();
                rebound = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setFillViewport(boolean fillViewport) {
        super.setFillViewport(true); //默认是填充ScrollView 或者再XML布局文件中设置fillViewport属性
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return true;
    }

    /**
     * 判断当前ScrollView是否处于顶部
     */
    private boolean isScrollToTop() {
        return getScrollY() == 0;
    }

    /**
     * 判断当前ScrollView是否已滑到底部
     */
    private boolean isScrollToBottom() {
        return mContentView.getMeasuredHeight() <= getHeight() + getScrollY();
    }

    public interface OnReboundEndListener{
        void onReboundTopComplete();
        void onReboundBottomComplete();
    }

}