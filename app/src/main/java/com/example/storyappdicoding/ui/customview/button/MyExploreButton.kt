package com.example.storyappdicoding.ui.customview.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyappdicoding.R

class MyExploreButton: AppCompatButton {
    private lateinit var backgroundButton: Drawable
    private var txtColor: Int = 0
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }



    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        backgroundButton = ContextCompat.getDrawable(context, R.drawable.bg_button_enabled) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = backgroundButton
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = context.getString(R.string.start_explore)
    }
}