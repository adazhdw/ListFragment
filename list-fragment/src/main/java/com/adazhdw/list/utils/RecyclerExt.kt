package com.adazhdw.list.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isSlideToBottom(): Boolean {
    return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
}

fun RecyclerView.isAlreadyBottom(): Boolean {
    return !canScrollVertically(1)
}
