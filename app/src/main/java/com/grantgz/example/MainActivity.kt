package com.grantgz.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.list_load_more_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity_layout)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}

val addMoreList: ArrayList<String> = arrayListOf<String>().apply {
    add("01")
    add("02")/*
    add("03")
    add("04")
    add("05")
    add("06")
    add("07")
    add("08")
    add("09")
    add("10")
    add("11")
    add("12")
    add("13")*/
}

class LoadMoreFragment : ListFragmentGz<String, LoadMoreFragment.LoadMoreHolder, LoadMoreFragment.LoadMoreAdapter>() {

    private val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onAdapter(): LoadMoreAdapter {
        return LoadMoreAdapter()
    }

    override fun onItemAnimator(): RecyclerView.ItemAnimator {
        return SlideInRightAnimator().apply {
            addDuration = 250
        }
    }

    override fun onNextPage(page: Int, callback: LoadCallback?) {
        mHandler.postDelayed({
            callback?.onResult()
            callback?.onLoad(addMoreList)
        }, 1500)
    }

    inner class LoadMoreAdapter : RecyclerView.Adapter<LoadMoreHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadMoreHolder {
            return LoadMoreHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.list_load_more_item,
                            parent,
                            false
                    )
            ).apply {
                itemView.setOnClickListener {
                    ToastUtils.showShort("sdfdsfsdfds")
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

