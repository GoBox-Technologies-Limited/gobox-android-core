package com.gobox.core.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.gobox.core.R


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
                }
            }
        }
        refreshButton()
        myAttrs.recycle()
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int): super(ctx, attrs, defStyle)

    override fun onDraw(canvas: Canvas?) {
        if (compoundDrawables[0]!=null) {
            // has drawable images
            var drawableWidth = 0
            if (compoundDrawables[0]!=null) {
                // add left drawable width with default padding 2
                drawableWidth += compoundDrawables[0].intrinsicWidth + 2
            }
            if (compoundDrawables[2]!=null) {
                // add right drawable width with default padding 2
                drawableWidth += compoundDrawables[2].intrinsicWidth + 2
            }

            val textWidth = (paint.measureText(text.toString()) + textSize * 2).toInt()
            val autoPadding: Int = (width - drawableWidth - textWidth) / 2
            setPadding(autoPadding, 0, autoPadding, 0)
        }
        super.onDraw(canvas)
    }

    private fun refreshButton() {
        if (isActive) {
            background = AppCompatResources.getDrawable(context, R.drawable.button_active)
            setTextColor(context.getColor(R.color.button_text_active))
            compoundDrawables.forEach {
                if (it != null) {
                    it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.button_text_active), PorterDuff.Mode.SRC_IN)
                }
            }
        } else {
            background = AppCompatResources.getDrawable(context, R.drawable.button_inactive)
            setTextColor(context.getColor(R.color.button_text_inactive))
            compoundDrawables.forEach {
                if (it != null) {
                    it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.button_text_inactive), PorterDuff.Mode.SRC_IN)
                }
            }
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