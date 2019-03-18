package com.adazhdw.listfragment.utils;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerUtil {
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();
    }
    public static boolean isAlreadyBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        return !recyclerView.canScrollVertically(1);
    }
}
