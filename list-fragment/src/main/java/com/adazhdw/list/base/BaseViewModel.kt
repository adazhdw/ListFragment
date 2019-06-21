package com.adazhdw.list.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel() {

    fun launch(block: suspend () -> Unit) {
        launchOnUI(block, {
            errorFun(it)
        })
    }

    open fun errorFun(throwable: Throwable) {

    }

    private fun launchOnUI(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (ex: Throwable) {
            error(ex)
        }
    }

}