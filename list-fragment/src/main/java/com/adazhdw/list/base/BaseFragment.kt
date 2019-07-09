package com.adazhdw.list.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

open class BaseFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = BaseViewModel().viewModelScope.coroutineContext

    val TAG = javaClass.simpleName
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
