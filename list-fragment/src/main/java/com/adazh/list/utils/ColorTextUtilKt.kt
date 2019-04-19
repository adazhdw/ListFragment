package com.adazh.list.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

object ColorTextUtilKt {

    fun makeTextEx_Ex(msg: String? = "", color: String? = "#000000", start: Int? = 0, end: Int? = 0): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(color)),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun makeTextEx_In(msg: String? = "", color: String? = "#000000", start: Int? = 0, end: Int? = 0): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(color)),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    fun makeTextIn_Ex(msg: String? = "", color: String? = "#000000", start: Int? = 0, end: Int? = 0): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(color)),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun makeTextIn_In(msg: String? = "", color: String? = "#000000", start: Int? = 0, end: Int? = 0): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(color)),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    fun makeTextEx_Ex(
            msg: String? = "",
            color: Int? = Color.parseColor("#000000"),
            start: Int? = 0,
            end: Int? = 0
    ): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(color ?: 0),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun makeTextEx_In(
            msg: String? = "",
            color: Int? = Color.parseColor("#000000"),
            start: Int? = 0,
            end: Int? = 0
    ): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(color ?: 0),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    fun makeTextIn_Ex(
            msg: String? = "",
            color: Int? = Color.parseColor("#000000"),
            start: Int? = 0,
            end: Int? = 0
    ): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(color ?: 0),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun makeTextIn_In(
            msg: String? = "",
            color: Int? = Color.parseColor("#000000"),
            start: Int? = 0,
            end: Int? = 0
    ): SpannableString {
        val spannableString = SpannableString(msg)
        spannableString.setSpan(
                ForegroundColorSpan(color ?: 0),
                start ?: 0,
                end ?: msg?.length ?: 0,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableString
    }
}