package com.rootscare.data.model.response.doctor.payment

import com.google.gson.annotations.SerializedName
import java.util.*

data class PaymentResponse(

	@field:SerializedName("result")
	val result: LinkedList<ResultItem>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ResultItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("paymentType")
	val paymentType: String? = null,

	@field:SerializedName("price")
	val amount: String? = null,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null
)
