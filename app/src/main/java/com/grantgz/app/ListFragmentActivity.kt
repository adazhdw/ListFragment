package com.grantgz.app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.grantgz.listfragment.ListFragmentGz
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
    add("06")/*
    add("07")
    add("08")
    add("09")
    add("10")
    add("11")
    add("12")
    add("13")*/
}

open class LoadMoreFragment : ListFragmentGz<String, LoadMoreFragment.LoadMoreHolder, LoadMoreFragment.LoadMoreAdapter>() {

    override fun customizeView(context: Context?, rooContentFl: ViewGroup?) {
        super.customizeView(context, rooContentFl)
        rooContentFl?.visibility = View.VISIBLE
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
        }, 1500)
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

