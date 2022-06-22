package com.rootscare.serviceprovider.ui.transactionss.models


import androidx.annotation.Keep

@Keep
data class ModelWithdrawalDetails(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val abal: String?,
        val bankdetails: Bankdetails?,
        val content: Content?,
        val withdrawal: List<Withdrawal?>?
    ) {
        @Keep
        data class Bankdetails(
            val account_no: String?,
            val acname: String?,
            val address: String?,
            val bank_name: String?
        )

        @Keep
        data class Content(
            val content: String?,
            val heading: String?
        )

        @Keep
        data class Withdrawal(
            val amount: String?,
            val date: String?,
            val text: String?
        )
    }
}