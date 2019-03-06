package com.adazhdw.listfragment;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.scwang.smartrefresh.header.MaterialHeader;

public interface IThemeColor {
    /**
     * 使用setColorSchemeColors或者setColorSchemeResources设置颜色
     * @param mHeader
     */
    void onListHeader(SwipeRefreshLayout mHeader);
    void onListFooter(MaterialHeader mFooter);
}
