package com.gobox.core.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.gobox.core.R
import timber.log.Timber
import java.lang.Math.ceil

class GoButton: AppCompatButton {
    private var active: Boolean = true
    private var border: Boolean = false
    private var reverseColor: Boolean = false
    private var radius: Float = 16F
    private var iconSize: Float? = null
    private var activeBackgroundColorResId: Int = R.color.button_active
    private var activeTextColorResId: Int = R.color.button_text_active
    private var inactiveBackgroundColorResId: Int = R.color.button_inactive
    private var inactiveTextColorResId: Int = R.color.button_text_inactive

    private var hasSetPadding = false
    private var originPadding = ArrayList<Int>(listOf(0,0,0,0))
    private var drawablePadding = ArrayList<Int>(listOf(0,0,0,0))
    private var textTotalWidth = 0
    private var textTotalHeight = 0

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
                R.styleable.GoButton_go_border -> {
                    border = myAttrs.getBoolean(attr, true)
                }
                R.styleable.GoButton_go_reverse_color -> {
                    reverseColor = myAttrs.getBoolean(attr, true)
                }
                R.styleable.GoButton_go_radius -> {
                    radius = myAttrs.getDimension(attr, 16F)
                }
                R.styleable.GoButton_go_icon_size -> {
                    iconSize = myAttrs.getDimension(attr, 16F)
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
        if (!hasSetPadding) {
            hasSetPadding = true
            var horizontalPadding: Int = kotlin.math.floor((width - drawablePadding[0] - drawablePadding[2] - textTotalWidth) / 2.0).toInt()
            if (horizontalPadding>8) horizontalPadding -= 8
            val verticalPadding: Int = kotlin.math.floor((height - drawablePadding[1] - drawablePadding[3] - textTotalHeight) / 2.0).toInt()
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

            Timber.tag("GoButton").e("${width}, ${height}, ${originPadding}, ${drawablePadding}, ${textTotalWidth}, ${textTotalHeight}")
        }

        super.onDraw(canvas)
    }

    private fun refreshButton() {
        var borderColor = if (border) {context.getColor(activeTextColorResId)} else {context.getColor(activeBackgroundColorResId)}
        var backgroundColor = context.getColor(activeBackgroundColorResId)
        var textColor = context.getColor(activeTextColorResId)

        if (!active) {
            borderColor = if (border) {context.getColor(inactiveTextColorResId)} else {context.getColor(inactiveBackgroundColorResId)}
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

        // 2. Set text
        text = text.toString().uppercase()
        setTextColor(textColor)

        // 3. Set image color & Get original padding
        originPadding[0] = totalPaddingStart
        if (compoundDrawables[0]!=null) { // left
            compoundDrawables[0].colorFilter = PorterDuffColorFilter(textColor, PorterDuff.Mode.MULTIPLY)
            originPadding[0] = originPadding[0] - compoundDrawables[0].intrinsicWidth
        }
        originPadding[1] = totalPaddingTop
        if (compoundDrawables[1]!=null) { // top
            compoundDrawables[1].colorFilter = PorterDuffColorFilter(textColor, PorterDuff.Mode.MULTIPLY)
            originPadding[1] = originPadding[1] - compoundDrawables[1].intrinsicHeight
        }
        originPadding[2] = totalPaddingEnd
        if (compoundDrawables[2]!=null) { // right
            compoundDrawables[2].colorFilter = PorterDuffColorFilter(textColor, PorterDuff.Mode.MULTIPLY)
            originPadding[2] = originPadding[2] - compoundDrawables[2].intrinsicWidth
        }
        originPadding[3] = totalPaddingBottom
        if (compoundDrawables[3]!=null) { // bottom
            compoundDrawables[3].colorFilter = PorterDuffColorFilter(textColor, PorterDuff.Mode.MULTIPLY)
            originPadding[3] = originPadding[3] - compoundDrawables[3].intrinsicHeight
        }

        // 4. Resize drawables
        val newDrawables = ArrayList<Drawable?>()
        compoundDrawables.forEachIndexed { i, drawable ->
            if (drawable != null) {
                var size = textSize
                if (iconSize!=null) size = iconSize!!
                val draw = ScaleDrawable(drawable, 0, size, size).drawable
                if (i==0 || i==2) {
                    // left or right
                    draw!!.setBounds(0, 0, size.toInt(), size.toInt())
                } else {
                    draw!!.setBounds(0, 0, size.toInt(), size.toInt())
                }
                newDrawables.add(draw)
            } else {
                newDrawables.add(null)
            }
        }
        setCompoundDrawables(newDrawables[0],newDrawables[1],newDrawables[2],newDrawables[3])

        if (compoundDrawables[0]!=null) drawablePadding[0] = totalPaddingStart - originPadding[0]
        if (compoundDrawables[1]!=null) drawablePadding[1] = totalPaddingTop - originPadding[1]
        if (compoundDrawables[2]!=null) drawablePadding[2] = totalPaddingEnd - originPadding[2]
        if (compoundDrawables[3]!=null) drawablePadding[3] = totalPaddingBottom - originPadding[3]

        textTotalWidth = kotlin.math.ceil(paint.measureText(text.toString(), 0, text.length)).toInt()
        textTotalHeight = kotlin.math.ceil(paint.fontMetrics.bottom - paint.fontMetrics.top).toInt()

        stateListAnimator = null
    }

    fun setActive(act: Boolean) {
        active = act
        refreshButton()
    }

    fun getActive(): Boolean {
        return active
    }
}