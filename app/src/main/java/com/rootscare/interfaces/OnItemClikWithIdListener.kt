package com.rootscare.interfaces

interface OnItemClikWithIdListener {
    fun onItemClick(id: Int)
    fun onAcceptBtnClick(id: String, text: String)
    fun onRejectBtnClick(id: String, text: String)

}