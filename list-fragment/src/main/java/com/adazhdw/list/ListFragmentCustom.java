package com.adazhdw.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.adazhdw.list.base.BaseFragment;
import com.adazhdw.list.base.FooterStyle;
import com.adazhdw.list.widget.TranslationScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ListFragmentCustom<M, VH extends RecyclerView.ViewHolder, A extends RecyclerView.Adapter<VH>> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<M> mList = new ArrayList<>();
    private boolean mLoading = false;
    private SwipeRefreshLayout mSwipe;
    private FrameLayout mFooterFl;
    private ProgressBar mLoadingBar;
    private TranslationScrollView mTranslationScrollView;
    private A mListAdapter = onAdapter();
    private RecyclerView mListView;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_custom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipe = view.findViewById(R.id.swipe);
        mTranslationScrollView = view.findViewById(R.id.mTranslationScrollView);
        mFooterFl = view.findViewById(R.id.footerFl);
        mLoadingBar = view.findViewById(R.id.loadingBar);
        mSwipe.setOnRefreshListener(this);
        mListView = view.findViewById(R.id.listRecyclerView);

        onListHeader(mSwipe);
        onListFooter(mLoadingBar);
        mListView.setItemAnimator(onItemAnimator());
        mListView.setLayoutManager(onLayoutManager());
        mListView.setAdapter(mListAdapter);

        mTranslationScrollView.setEnableTopRebound(false);
        mTranslationScrollView.setEnableBottomRebound(true);
        mTranslationScrollView.setScrollOffset(0.5);
        mTranslationScrollView.setLoadingView(mFooterFl);
        mTranslationScrollView.setReboundDuration(300);
        mTranslationScrollView.setLoadingHeight(150);
        mTranslationScrollView.setShowDistance(300);
        mTranslationScrollView.setOnReboundEndListener(new TranslationScrollView.OnReboundEndListener() {

            @Override
            public void onReboundTopComplete() {

            }

            @Override
            public void onReboundBottomComplete() {
                nextPage();
            }
        });

        setFooterStyle(onListFooterStyle(FooterStyle.BLUE));
        mSwipe.setOnRefreshListener(this);
        customizeView(getContext(), view.<ViewGroup>findViewById(R.id.rooContentFl));
        refresh();
    }

    public final void refresh() {
        if (mSwipe != null) {
            mSwipe.setRefreshing(true);
            onRefresh();
        }
    }

    /**
     * The return value is not too small
     */
    protected int pageSize() {
        return 10;
    }

    protected int pageStartAt() {
        return 1;
    }

    protected void customizeView(Context context, ViewGroup rooContentFl) {
    }

    protected RecyclerView.LayoutManager onLayoutManager() {
        return new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
    }

    protected void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        if (mListView != null) {
            mListView.addItemDecoration(decor);
        }
    }

    protected RecyclerView.ItemAnimator onItemAnimator() {
        return new DefaultItemAnimator();
    }

    public void onListFooter(ProgressBar mProgressBar) {

    }

    /**
     * return ProgressBar'Style file
     *
     * @return
     */
    public int onListFooterStyle(FooterStyle footerStyle) {
        if (footerStyle==FooterStyle.BLUE) {
            return R.drawable.loading_bar_style_blue;
        }else if (footerStyle==FooterStyle.RED){
            return R.drawable.loading_bar_style_red;
        }else if (footerStyle==FooterStyle.YELLOW){
            return R.drawable.loading_bar_style_yellow;
        }else{
            return R.drawable.loading_bar_style_blue;
        }
    }

    public void onListHeader(SwipeRefreshLayout mHeader) {

    }

    /**
     * ProgressBar set Style
     *
     * @param res
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setFooterStyle(@DrawableRes int res) {
        Drawable drawable = Objects.requireNonNull(getContext()).getDrawable(res);
        mLoadingBar.setIndeterminateDrawable(drawable);
    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    protected abstract A onAdapter();

    protected abstract void onNextPage(int page, LoadCallback callback);

    protected int getListSize() {
        return mList.size();
    }

    protected M getListItem(int position) {
        return mList.get(position);
    }

    protected List<M> getList() {
        return new ArrayList<>(mList);
    }

    protected int getCurrPage() {
        return currPage;
    }

    protected String noDataTip() {
        return getString(R.string.no_more_data_text);
    }

    @Override
    public final void onRefresh() {
        nextPage();
    }

    private int currPage = 0;

    private void nextPage() {
        if (mLoading)
            return;
        mLoading = true;

        final boolean refresh = mSwipe.isRefreshing();
        currPage = pageStartAt() + (refresh ? 0 : currPage);
        if (!refresh) {
            mSwipe.setEnabled(false);
        } else {

        }
        onNextPage(currPage, new LoadCallback() {
            @Override
            public void onResult() {
                if (refresh) {
                    mList.clear();
                    mListAdapter.notifyDataSetChanged();
                }
                mLoading = false;
                mSwipe.setEnabled(true);
                mSwipe.setRefreshing(false);
            }

            @Override
            public void onSuccessLoad(List<M> list) {
                if (!list.isEmpty()) {
                    currPage++;
                    int start = mList.size();
                    mList.addAll(list);
                    mListAdapter.notifyItemRangeInserted(start, mList.size());
                } else {
                    Toast.makeText(getContext(), noDataTip(), Toast.LENGTH_SHORT).show();
                }
                mTranslationScrollView.resetAnim();
            }
        });
    }

    /**
     * ScrollView'Properties setting method
     * mTranslationScrollView.setLoadingView(mFooterFl);
     */

    public void setEnableTopRebound(boolean isEnable) {
        if (mTranslationScrollView != null) {
            mTranslationScrollView.setEnableTopRebound(isEnable);
        }
    }

    public void setEnableBottomRebound(boolean isEnable) {
        if (mTranslationScrollView != null) {
            mTranslationScrollView.setEnableBottomRebound(isEnable);
        }
    }

    public void setScrollOffset(float scrollOffset) {
        if (mTranslationScrollView != null) {
            mTranslationScrollView.setScrollOffset(scrollOffset);
        }
    }

    public void setReboundDuration(long duration) {
        mTranslationScrollView.setReboundDuration(duration);
    }

    public void setLoadingHeight(int loadingHeight) {
        mTranslationScrollView.setLoadingHeight(loadingHeight);
    }

    public void setShowDistance(int showDistance) {
        mTranslationScrollView.setShowDistance(showDistance);
    }

    public abstract class LoadCallback {
        /**
         * must invoke this method to end load progress
         */
        public abstract void onResult();

        /**
         * Call this method means load data success
         * thi method must after {@link #onResult()}，and between two method mustn't have {@link ListFragmentCustom#nextPage()} call
         */
        public abstract void onSuccessLoad(List<M> list);
    }

}
