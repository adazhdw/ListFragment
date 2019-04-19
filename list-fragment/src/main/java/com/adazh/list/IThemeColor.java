package com.adazh.list;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

public interface IThemeColor {
    /**
     *  use setColorSchemeColors or setColorSchemeResources set color
     * @param mHeader
     */
    void onListHeader(SwipeRefreshLayout mHeader);
    void onListFooter(InternalAbstract mFooter);
}
