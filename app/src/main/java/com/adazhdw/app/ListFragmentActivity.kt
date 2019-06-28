package com.adazhdw.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adazhdw.baselibrary.http.RetrofitUtil
import com.adazhdw.baselibrary.http.await
import com.adazhdw.baselibrary.http.requestC
import com.adazhdw.baselibrary.http.retrofitService
import com.adazhdw.list.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.list_fragment_load_more_item.view.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class ListFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fragment)
    }
}

open class ListFragment2 : ListFragmentLine<ChapterHistory, BaseViewHolder, ListFragment2.LoadMoreAdapter>() {
    override fun onAdapter(): LoadMoreAdapter {
        return LoadMoreAdapter()
    }

    override fun onNextPage(page: Int, callback: LoadCallback) {
        launch {
            val data = retrofitService(ApiService::class.java)
                .getWxArticleHistory2(408, page)
                .await()
            callback.onResult()
            callback.onSuccessLoad(data.data?.datas ?: mutableListOf())
        }
    }

    inner class LoadMoreAdapter : BaseRvAdapter<ChapterHistory>(context) {
        override fun onLayoutId(): Int {
            return R.layout.list_fragment_load_more_item
        }

        override fun initData(holder: BaseViewHolder, position: Int) {
            getListItem(position).let {
                holder.itemView.itemTv.text = it.title
            }
        }
    }
}

interface ApiService {

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Observable<HistoryList>

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory2(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Call<HistoryList>

}
