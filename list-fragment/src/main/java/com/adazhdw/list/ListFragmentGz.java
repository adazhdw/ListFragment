package com.adazhdw.list;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.adazhdw.list.base.BaseFragment;
import com.adazhdw.list.layout.InterceptLayoutEx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import java.util.ArrayList;
import java.util.List;
/**
 * ViewModel 模式并不适合目前的ListFragment的加载模式
 *
 * ViewModel mode isn't suitable for the current Loading data mode of the ListFragment
 */
public abstract class ListFragmentGz<M, VH extends RecyclerView.ViewHolder, A extends RecyclerView.Adapter<VH>> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IThemeColor {
    protected String TAG = getClass().getSimpleName();
    private List<M> mList = new ArrayList<>();
    private boolean mLoading = false;
    private SwipeRefreshLayout mSwipe;
    private SmartRefreshLayout mSmartRefreshLayout;
    private InternalAbstract mFooterEx;
    private A mListAdapter = onAdapter();
    private RecyclerView mListView;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mEventRunnable = new Runnable() {
        @Override
        public void run() {
            mSmartRefreshLayout.setEnableLoadMore(!mSwipe.isRefreshing());
            mSwipe.setEnabled(mSmartRefreshLayout.getState() != RefreshState.ReleaseToLoad &&
                    mSmartRefreshLayout.getState() != RefreshState.LoadReleased &&
                    mSmartRefreshLayout.getState() != RefreshState.Loading);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InterceptLayoutEx rootLayout = view.findViewById(R.id.rootLayout);
        mSwipe = view.findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(this);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        mFooterEx = view.findViewById(R.id.mMaterialHeader);
        mListView = view.findViewById(R.id.listRecyclerView);

        onListHeader(mSwipe);
        onListFooter(mFooterEx);
        mListView.setItemAnimator(onItemAnimator());
        mListView.setLayoutManager(onLayoutManager());
        mListView.setAdapter(mListAdapter);

        mSmartRefreshLayout.setEnableRefresh(false);
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                nextPage();
            }
        });

        rootLayout.setOnMotionEventListener(new InterceptLayoutEx.OnMotionEventListener() {
            @Override
            public void OnSlipAction(MotionEvent event) {
                mEventRunnable.run();
                mHandler.post(mEventRunnable);
            }
        });

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

    /**
     * theme color set method
     * @param mFooter
     */
    @Override
    public void onListFooter(InternalAbstract mFooter) {

    }

    @Override
    public void onListHeader(SwipeRefreshLayout mHeader) {

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
        if (refresh) {
            mSmartRefreshLayout.setEnableLoadMore(false);
        } else {
            mSwipe.setEnabled(false);
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
                mSmartRefreshLayout.finishLoadMore();
                mSmartRefreshLayout.setEnableLoadMore(true);
            }

            @Override
            public void onSuccessLoad(List<M> list) {
                if (!list.isEmpty()) {
                    int start = mList.size();
                    mList.addAll(list);
//                    mListAdapter.notifyDataSetChanged();
                    mListAdapter.notifyItemRangeInserted(start, mList.size());
                } else {
                    Toast.makeText(getContext(), noDataTip(), Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "currPage--------------" + currPage);
            }
        });
    }
    public abstract class LoadCallback {
        /**
         * must invoke this method to end load progress
         */
        public abstract void onResult();

        /**
         * Call this method means load data success
         * thi method must after {@link #onResult()}，and between two method mustn't have {@link ListFragmentGz#nextPage()} call
         */
        public abstract void onSuccessLoad(List<M> list);
    }

}
