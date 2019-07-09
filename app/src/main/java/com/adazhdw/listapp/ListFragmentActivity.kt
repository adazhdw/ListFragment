package com.adazhdw.listapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adazhdw.baselibrary.base.BaseNormalActivity
import com.adazhdw.baselibrary.http.await
import com.adazhdw.baselibrary.http.retrofitService
import com.adazhdw.list.BaseRvAdapter
import com.adazhdw.list.BaseViewHolder
import com.adazhdw.list.ListFragmentLine
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_list_fragment.*
import kotlinx.android.synthetic.main.list_fragment_load_more_item.view.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class ListFragmentActivity : BaseNormalActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fragment)

        requestBtn.setOnClickListener {
            launch {
                val data = retrofitService(ApiService::class.java).getWxArticleChapters().await()
                data.data?.forEach {
                    Log.d(TAG, it.name ?: "")
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.listFragment, ListFragment2())
            .commit()
    }
}

open class ListFragment2 : ListFragmentLine<WxArticleChapter, BaseViewHolder, ListFragment2.LoadMoreAdapter>() {
    override fun onAdapter(): LoadMoreAdapter {
        return LoadMoreAdapter()
    }

    override fun onNextPage(page: Int, callback: LoadCallback) {
        launch {
            val data = retrofitService(ApiService::class.java).getWxArticleChapters().await()
            callback.onResult()
            callback.onSuccessLoad(data.data ?: listOf())
        }
    }

    inner class LoadMoreAdapter : BaseRvAdapter<WxArticleChapter>(context) {
        override fun onLayoutId(): Int {
            return R.layout.list_fragment_load_more_item
        }

        override fun initData(holder: BaseViewHolder, position: Int) {
            getListItem(position).let {
                holder.itemView.itemTv.text = it.name
            }
        }
    }
}

interface ApiService {

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Observable<BaseResponse<HistoryData>>

    @GET("/wxarticle/list/{articleId}/{page}/json")
    fun getWxArticleHistory2(@Path("articleId") articleId: Int, @Path("page") page: Int = 1): Call<BaseResponse<HistoryData>>

    @GET("https://wanandroid.com/wxarticle/chapters/json")
    fun getWxArticleChapters(): Call<ListResponse<WxArticleChapter>>

}
