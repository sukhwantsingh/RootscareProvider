package com.rootscare.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class MyCustomButton : androidx.appcompat.widget.AppCompatButton {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    fun init() {
        val tf = Typeface.createFromAsset(context.assets, "font/century_gothic.ttf")
        setTypeface(tf, Typeface.BOLD)
    }


}