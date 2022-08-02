package com.example.storyappdicoding.ui.customview.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyappdicoding.R

class MyEditTextPassword: AppCompatEditText, View.OnTouchListener {
    private lateinit var buttonImage: Drawable

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
        buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if(s.toString().isEmpty()){
                    error = context.getString(R.string.empty_field)
                }
                else if(s.toString().length < 6){
                    error = context.getString(R.string.invalid_password)
                }
            }
        })

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val buttonStart: Float
            val buttonEnd: Float
            var isButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                buttonEnd = (buttonImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < buttonEnd) isButtonClicked = true
            } else {
                buttonStart = (width - paddingEnd - buttonImage.intrinsicWidth).toFloat()
                if (event.x > buttonStart) isButtonClicked = true
            }

            if (isButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        hideButton()
                        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
                            transformationMethod = PasswordTransformationMethod.getInstance()
                            buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_off_24) as Drawable
                            showButton()
                        } else {
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                            buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24) as Drawable
                            showButton()
                        }
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        showButton()
        textSize = 16f
        setBackgroundResource(R.drawable.border)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = buttonImage)
    }

    private fun hideButton() {
        setButtonDrawables()
    }


    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
}