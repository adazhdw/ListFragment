package com.adazh.list.utils

import androidx.recyclerview.widget.RecyclerView

object RecyclerUtil {
    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        return if (recyclerView == null) false else recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }

    fun isAlreadyBottom(recyclerView: RecyclerView?): Boolean {
        return if (recyclerView == null) false else !recyclerView.canScrollVertically(1)
    }
}
