package com.adazhdw.list

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adazhdw.list.base.BaseFragment
import com.adazhdw.list.base.FooterStyle
import com.adazhdw.list.utils.RecyclerUtil
import kotlinx.android.synthetic.main.fragment_list_line.*

import java.util.ArrayList

abstract class ListFragmentLine<M, VH : RecyclerView.ViewHolder, A : BaseRvAdapter<M>> : BaseFragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val mListAdapter = onAdapter()
    protected val mHandler = Handler(Looper.getMainLooper())

    protected val listSize: Int
        get() = mListAdapter.mData.size

    protected val list: List<M>
        get() = mListAdapter.mData

    protected var currPage = 0
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_line, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe.setOnRefreshListener(this)

        onListHeader(swipe)
        lineRecyclerView.itemAnimator = onItemAnimator()
        lineRecyclerView.layoutManager = onLayoutManager()
        lineRecyclerView.adapter = mListAdapter
        lineRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (RecyclerUtil.isSlideToBottom(recyclerView)) {
                    nextPage()
                }
            }
        })

        swipe.setOnRefreshListener(this)
        customizeView(context, view.findViewById(R.id.rooContentFl))
        refresh()
    }

    fun refresh() {
        if (swipe != null) {
            swipe.isRefreshing = true
            onRefresh()
        }
    }

    /**
     * The return value is not too small
     */
    protected fun pageSize(): Int {
        return 10
    }

    protected fun pageStartAt(): Int {
        return 1
    }

    protected fun customizeView(context: Context?, rooContentFl: ViewGroup) {}

    protected open fun onLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    protected fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        if (lineRecyclerView != null) {
            lineRecyclerView!!.addItemDecoration(decor)
        }
    }

    protected fun onItemAnimator(): RecyclerView.ItemAnimator {
        return DefaultItemAnimator()
    }

    fun onListFooter(mProgressBar: ProgressBar) {

    }

    fun onListHeader(mHeader: SwipeRefreshLayout) {

    }

    /**
     * return ProgressBar'Style file
     *
     * @return
     */
    fun onListFooterStyle(footerStyle: FooterStyle): Int {
        return when {
            footerStyle === FooterStyle.BLUE -> R.drawable.loading_bar_style_blue
            footerStyle === FooterStyle.RED -> R.drawable.loading_bar_style_red
            footerStyle === FooterStyle.YELLOW -> R.drawable.loading_bar_style_yellow
            else -> R.drawable.loading_bar_style_blue
        }
    }

    protected fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun onAdapter(): A

    protected abstract fun onNextPage(page: Int, callback: LoadCallback)

    protected fun getListItem(position: Int): M {
        return mListAdapter.mData[position]
    }

    protected fun noDataTip(): String {
        return getString(R.string.no_more_data_text)
    }

    override fun onRefresh() {
        mListAdapter.isRefresh = true
        nextPage()
    }

    private fun nextPage() {
        if (mListAdapter.isLoading)
            return
        mListAdapter.isLoading = true

        val refresh = swipe.isRefreshing
        currPage = pageStartAt() + if (refresh) 0 else currPage
        if (!refresh) {
            swipe.isEnabled = false
            mListAdapter.showLoading()
            mListAdapter.isRefresh = false
        } else {
            mListAdapter.isRefresh = true
        }

        mHandler.postDelayed({
            onNextPage(currPage, object : LoadCallback() {
                override fun onResult() {
                    if (refresh) {
                        mListAdapter.clearData()
                    }
                    mListAdapter.isLoading = false
                    mListAdapter.isRefresh = false
                    swipe.isEnabled = true
                    swipe.isRefreshing = false
                }

                override fun onSuccessLoad(list: MutableList<M>) {
                    if (!list.isEmpty()) {
                        currPage++
                        mListAdapter.mData = list
                    } else {
                        mListAdapter.showLoading()
                        Toast.makeText(context, noDataTip(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        },1000)
    }

    abstract inner class LoadCallback {
        /**
         * must invoke this method to end load progress
         */
        abstract fun onResult()

        /**
         * Call this method means load data success
         * thi method must after [.onResult]，and between two method mustn't have [ListFragmentLine.nextPage] call
         */
        abstract fun onSuccessLoad(list: MutableList<M>)
    }

}
