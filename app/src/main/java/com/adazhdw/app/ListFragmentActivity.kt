package com.adazhdw.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adazhdw.baselibrary.http.RetrofitUtil
import com.adazhdw.baselibrary.http.requestC
import com.adazh.list.ListFragmentCustom
import io.reactivex.Observable
import kotlinx.android.synthetic.main.list_fragment_load_more_item.view.*
import retrofit2.http.GET
import retrofit2.http.Path

class ListFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fragment)
    }
}

open class LoadMoreFragment :
    ListFragmentCustom<ChapterHistory, LoadMoreFragment.LoadMoreHolder, LoadMoreFragment.LoadMoreAdapter>() {

    override fun onListHeader(mHeader: SwipeRefreshLayout) {
        mHeader.setColorSchemeResources(R.color.colorPrimary)
    }

    override fun noDataTip(): String {
        return "无数据"
    }



    override fun onAdapter(): LoadMoreAdapter {
        return LoadMoreAdapter()
    }

    override fun onNextPage(page: Int, callback: LoadCallback) {
        RetrofitUtil.apiService(ApiService::class.java)
            .getWxArticleHistory(408, page)
            .requestC(onSuccess = {
                mHandler.postDelayed({
                    callback.onResult()
                    callback.onLoad(it.data?.datas ?: listOf())
                }, 1000)
            })
    }

    inner class LoadMoreAdapter : RecyclerView.Adapter<LoadMoreHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadMoreHolder {
            return LoadMoreHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_fragment_load_more_item,
                    parent,
                    false
                )
            ).apply {
                itemView.setOnClickListener {
                    showToast("第 $layoutPosition 行")
                }
            }
        }

        override fun getItemCount(): Int {
            return listSize
        }

        override fun onBindViewHolder(holder: LoadMoreHolder, position: Int) {
            getListItem(position).let {
                holder.itemView.itemTv.text = it.title
            }
        }

    }

    class LoadMoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

interface ApiService {


    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Observable<HistoryList>

}
