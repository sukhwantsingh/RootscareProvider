package com.rootscare.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

import android.widget.TextView


class MyTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super( context!!,  attrs, defStyle)  {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    fun init() {
        val tf = Typeface.createFromAsset(context.assets, "font/rubik_regular.ttf")
        typeface = tf
    }


}