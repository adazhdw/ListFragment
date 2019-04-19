package com.adazhdw.list.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class InterceptLayoutEx : FrameLayout {
    private val TAG = "InterceptFrameLayout"


    private var mOnSlipActionListener: OnMotionEventListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (mOnSlipActionListener != null) {
            mOnSlipActionListener!!.OnSlipAction(event)
        }
        return super.onInterceptTouchEvent(event)
    }

    fun setOnMotionEventListener(listener: OnMotionEventListener) {
        this.mOnSlipActionListener = listener
    }

    interface OnMotionEventListener {
        fun OnSlipAction(event: MotionEvent)
    }
}
