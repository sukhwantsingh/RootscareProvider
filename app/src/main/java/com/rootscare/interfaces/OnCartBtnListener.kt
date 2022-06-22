package com.rootscare.interfaces

interface OnCartBtnListener {
    fun onDeleteItemClick(id: Int,posation: Int)
    fun onItemIncrementClick(product_id: Int,posation: Int,qty: String,type: String)
    fun onItemDecrementClick(product_id: Int,posation: Int,qty: String,type: String)
}