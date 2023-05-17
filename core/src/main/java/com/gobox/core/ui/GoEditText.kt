package com.gobox.core.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.gobox.core.R

class GoEditText: AppCompatEditText {
    private var isError: Boolean = false

    constructor(ctx: Context): super(ctx) {
        refreshComponent()
    }

    constructor(ctx: Context, attrs: AttributeSet): super(ctx, attrs) {
        val myAttrs = ctx.obtainStyledAttributes(attrs, R.styleable.GoEditText)
        for (i in 0 until myAttrs.indexCount) {
            when (val attr = myAttrs.getIndex(i)) {
                R.styleable.GoEditText_go_is_error -> {
                    isError = myAttrs.getBoolean(attr, true)
                }
            }
        }

        refreshComponent()
        myAttrs.recycle()
    }

    private fun refreshComponent() {
        if (isError) {
            setBackgroundResource(R.drawable.edit_error)
            setTextColor(context.getColor(R.color.text_red))
            setHintTextColor(context.getColor(R.color.text_red))
        } else {
            setBackgroundResource(R.drawable.edit_normal)
            setTextColor(context.getColor(R.color.text_main))
            setHintTextColor(context.getColor(R.color.text_hint))
        }
    }

    fun setIsError(act: Boolean) {
        isError = act
        refreshComponent()
    }

    fun getIsError(): Boolean {
        return isError
    }
}