package com.rootscare.serviceprovider.ui.transactionss.models


import androidx.annotation.Keep

@Keep
data class ModelTransactions(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val content: Content?,
        val paymentdetails: List<Paymentdetail?>?,
        val percentage: String?
    ) {
        @Keep
        data class Content(
            val content: String?,
            val heading: String?
        )

        @Keep
        data class Paymentdetail(
            val date: String?,
            val order_id: String?,
            val paymentStatus: String?,
            val paymentType: String?,
            val price: String?
        )
    }
}