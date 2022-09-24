package com.gobox.core.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.marginTop
import com.gobox.core.R


class GoButton: AppCompatButton {
    private var active: Boolean = true
    private var radius: Int = 8
    private var activeBackgroundColorResId: Int = R.color.button_active
    private var activeTextColorResId: Int = R.color.button_text_active
    private var inactiveBackgroundColorResId: Int = R.color.button_inactive
    private var inactiveTextColorResId: Int = R.color.button_text_inactive

    constructor(ctx: Context): super(ctx) {
        refreshButton()
    }

    constructor(ctx: Context, attrs: AttributeSet): super(ctx, attrs) {
        val myAttrs = ctx.obtainStyledAttributes(attrs, R.styleable.GoButton)
        for (i in 0 until myAttrs.indexCount) {
            when (val attr = myAttrs.getIndex(i)) {
                R.styleable.GoButton_go_active -> {
                    active = myAttrs.getBoolean(attr, true)
                }
                R.styleable.GoButton_go_radius -> {
                    radius = myAttrs.getInt(attr, 8)
                }
                R.styleable.GoButton_go_active_background_color -> {
                    activeBackgroundColorResId = myAttrs.getResourceId(attr, 0)
                }
                R.styleable.GoButton_go_active_text_color -> {
                    activeTextColorResId = myAttrs.getResourceId(attr, 0)
                }
                R.styleable.GoButton_go_inactive_background_color -> {
                    inactiveBackgroundColorResId = myAttrs.getResourceId(attr, 0)
                }
                R.styleable.GoButton_go_inactive_text_color -> {
                    inactiveTextColorResId = myAttrs.getResourceId(attr, 0)
                }
            }
        }
        refreshButton()
        myAttrs.recycle()
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int): super(ctx, attrs, defStyle) {
        refreshButton()
    }

    override fun onDraw(canvas: Canvas?) {
        if (compoundDrawables[0]!=null) {
            // has drawable images
            var drawableWidth = 0
            if (compoundDrawables[0]!=null) {
                // add left drawable width with default padding 2
                drawableWidth += compoundDrawables[0].intrinsicWidth
            }
            if (compoundDrawables[2]!=null) {
                // add right drawable width with default padding 2
                drawableWidth += compoundDrawables[2].intrinsicWidth
            }

            val textWidth = (paint.measureText(text.toString()) + textSize).toInt()
            val autoPadding: Int = (width - drawableWidth - textWidth) / 2
            setPadding(autoPadding, 0, autoPadding, 0)
        }
        super.onDraw(canvas)
    }

    private fun refreshButton() {
        if (active) {
            // 1. Set background color
            val shape = GradientDrawable()
            shape.cornerRadius = radius.toFloat()
            shape.setColor(context.getColor(activeBackgroundColorResId))
            background = shape

            // 2. Set text color
            setTextColor(context.getColor(activeTextColorResId))

            // 3. Set image color
            compoundDrawables.forEach {
                if (it != null) {
                    it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.button_text_active), PorterDuff.Mode.SRC_IN)
                }
            }
        } else {
            // 1. Set background color
            val shape = GradientDrawable()
            shape.cornerRadius = radius.toFloat()
            shape.setColor(context.getColor(inactiveBackgroundColorResId))
            background = shape

            // 2. Set text color
            setTextColor(context.getColor(inactiveTextColorResId))

            // 3. Set image color
            compoundDrawables.forEach {
                if (it != null) {
                    it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.button_text_inactive), PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

    fun setActive(act: Boolean) {
        active = act
        refreshButton()
    }

    fun getActive(): Boolean {
        return active
    }
}