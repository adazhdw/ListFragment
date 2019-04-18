package com.adazhdw.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adazhdw.listfragment.ListFragmentCustom
import kotlinx.android.synthetic.main.list_fragment_load_more_item.view.*

class ListFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fragment)
    }
}

val addMoreList: ArrayList<String> = arrayListOf<String>().apply {
    add("01")
    add("02")
    add("03")
    add("04")
    add("05")
    add("06")
    add("07")
    add("08")
    add("09")
    add("10")
    add("11")/*
    add("12")
    add("13")*/
}

open class LoadMoreFragment : ListFragmentCustom<String, LoadMoreFragment.LoadMoreHolder, LoadMoreFragment.LoadMoreAdapter>() {

    override fun onLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }

    override fun onListHeader(mHeader: SwipeRefreshLayout) {
        mHeader.setColorSchemeResources(R.color.colorPrimary)
    }

    override fun noDataTip(): String {
        return "无数据"
    }

    override fun onAdapter(): LoadMoreAdapter {
        return LoadMoreAdapter()
    }

    protected fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onNextPage(page: Int, callback: LoadCallback?) {
        mHandler.postDelayed({
            callback?.onResult()
            callback?.onLoad(addMoreList)
        }, 1000)
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
            holder.itemView.itemTv.text = "第 $position 行"
        }

    }

    class LoadMoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

