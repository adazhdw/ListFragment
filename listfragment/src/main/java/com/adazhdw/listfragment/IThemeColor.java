package com.adazhdw.listfragment;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

public interface IThemeColor {
    /**
     * 使用setColorSchemeColors或者setColorSchemeResources设置颜色
     * @param mHeader
     */
    void onListHeader(SwipeRefreshLayout mHeader);
    void onListFooter(InternalAbstract mFooter);
}
