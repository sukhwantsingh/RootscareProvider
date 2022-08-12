package com.rootscare.serviceprovider.ui.pricelistss.models


import androidx.annotation.Keep

@Keep
data class ModelPriceListing(
    val code: String?,
    val message: String?,
    val result: ArrayList<Result?>?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val id: String?,
        var isChecked: Boolean? = false,
        val name: String?,
        val description: String?,
        var price: String?
    ){
        fun showDesc() = if(description.isNullOrBlank()) "" else description
    }
}