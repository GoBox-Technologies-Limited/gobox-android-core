package com.gobox.core.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.gobox.core.R


class GoButton: AppCompatButton {
    private var active: Boolean = true
    private var reverseColor: Boolean = false
    private var radius: Float = 16F
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
                R.styleable.GoButton_go_reverse_color -> {
                    reverseColor = myAttrs.getBoolean(attr, true)
                }
                R.styleable.GoButton_go_radius -> {
                    radius = myAttrs.getDimension(attr, 16F)
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
        text = text.toString().uppercase()
        // set default padding to zero
        setPadding(0, 0, 0, 0)

        // resize drawables to text size
        compoundDrawables.forEach {

        }

        // handle left & right drawables
        if (compoundDrawables[0]!=null || compoundDrawables[1]!=null || compoundDrawables[2]!=null || compoundDrawables[3]!=null) {
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

            var drawableHeight = 0
            if (compoundDrawables[1]!=null) {
                // add left drawable width with default padding 2
                drawableHeight += compoundDrawables[1].intrinsicHeight + 2
            }
            if (compoundDrawables[3]!=null) {
                // add right drawable width with default padding 2
                drawableHeight += compoundDrawables[3].intrinsicHeight + 2
            }

            val textWidth = paint.measureText(text.toString(), 0, text.length)
            val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
            var horizontalPadding: Int = ((width - drawableWidth - textWidth) / 2).toInt()
            if (horizontalPadding<0) horizontalPadding=0
            var verticalPadding: Int = ((height - drawableHeight - textHeight) / 2).toInt()
            if (verticalPadding<0) verticalPadding=0
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        }
        super.onDraw(canvas)
    }

    private fun refreshButton() {
        var borderColor = context.getColor(activeBackgroundColorResId)
        var backgroundColor = context.getColor(activeBackgroundColorResId)
        var textColor = context.getColor(activeTextColorResId)

        if (!active) {
            borderColor = context.getColor(inactiveBackgroundColorResId)
            backgroundColor = context.getColor(inactiveBackgroundColorResId)
            textColor = context.getColor(inactiveTextColorResId)
        }

        if (reverseColor) {
            backgroundColor = textColor
            textColor = borderColor
        }

        // 1. Set background color
        val gd = GradientDrawable()
        gd.setColor(backgroundColor)
        gd.cornerRadius = radius
        gd.setStroke(2, borderColor)
        background = gd

        // 2. Set text color
        setTextColor(textColor)

        // 3. Set image color
        compoundDrawables.forEach {
            if (it != null) {
                it.colorFilter = PorterDuffColorFilter(textColor, PorterDuff.Mode.MULTIPLY)
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