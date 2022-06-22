package com.rootscare.serviceprovider.ui.pricelistss


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
        var price: String?
    )
}