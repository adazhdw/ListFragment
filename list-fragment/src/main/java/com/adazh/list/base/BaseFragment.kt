package com.adazh.list.base

import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(), View.OnClickListener {

    val TAG = javaClass.simpleName
    val IDENTIFY = hashCode().toString()

    override fun onDestroy() {
        super.onDestroy()
    }

    protected fun setOnClickListener(views: Array<View>) {
        for (v in views) {
            v.setOnClickListener(this)
        }
    }

    protected fun setOnClickListener(ids: IntArray) {
        val view = view ?: return
        for (id in ids) {
            view.findViewById<View>(id).setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {

    }

    override fun onStart() {
        super.onStart()
        if (!isHidden)
            onShow()
    }

    override fun onStop() {
        super.onStop()
        onHide()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onHide()
        } else {
            onShow()
        }
    }

    protected fun onShow() {

    }

    protected fun onHide() {

    }
}
