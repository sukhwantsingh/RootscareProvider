package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentResponseHospital(
    @field:JsonProperty("result")
    @field:SerializedName("result")
    val result: ArrayList<ResultItemPayment?>? = null,
    @field:JsonProperty("code")
    @field:SerializedName("code")
    val code: String? = null,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    val message: String? = null,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: Boolean? = null
)
data class ResultItemPayment(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("order_id")
    val order_id: String? = null,

    @field:SerializedName("transaction_id")
    val transaction_id: String? = null,

    @field:SerializedName("payment_type")
    val payment_type: String? = null,

    @field:SerializedName("subtotal")
    val subtotal: String? = null,

    @field:SerializedName("vat")
    val vat: String? = null,

    @field:SerializedName("amount")
    val amount: String? = null,

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("payment_status")
    val payment_status: String? = null,

    @field:SerializedName("order_type")
    val order_type: String? = null,

    @field:SerializedName("hospital")
    val hospital: String? = null


)