package com.rootscare.interfaces

import android.widget.TextView

interface OnItemClickWithReportIdListener {
    fun onItemClick(id: Int){}

    fun uploadCerti(dispTv:TextView){}

}