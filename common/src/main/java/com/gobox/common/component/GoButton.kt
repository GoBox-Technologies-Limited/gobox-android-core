package com.gobox.common.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.gobox.common.R

class GoButton: AppCompatButton {
    private var isActive: Boolean = true

    constructor(ctx: Context): super(ctx) {
    }

    constructor(ctx: Context, attrs: AttributeSet): super(ctx, attrs) {
        val myAttrs = ctx.obtainStyledAttributes(attrs, R.styleable.GoButton)
        for (i in 0 until myAttrs.indexCount) {
            when (val attr = myAttrs.getIndex(i)) {
                R.styleable.GoButton_isActive -> {
                    isActive = myAttrs.getBoolean(attr, true)
                    refreshButton()
                }
            }
        }
        myAttrs.recycle()
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int): super(ctx, attrs, defStyle)

    private fun refreshButton() {
        if (isActive) {
            background = AppCompatResources.getDrawable(context, R.drawable.button_active)
            setTextColor(context.getColor(R.color.button_text_active))
        } else {
            background = AppCompatResources.getDrawable(context, R.drawable.button_inactive)
            setTextColor(context.getColor(R.color.button_text_inactive))
        }
    }

    fun setActive(active: Boolean) {
        isActive = active
        refreshButton()
    }

    fun getActive(): Boolean {
        return isActive
    }
}