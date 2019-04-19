package com.adazh.list.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class NestScrollViewEx :NestedScrollView {

    constructor(context: Context) : super(context,null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isCanScroll) {
            super.onInterceptTouchEvent(ev)
        }else{
            true
        }
    }

    var isCanScroll:Boolean = true
}