package com.rootscare.data.model.response.doctor.review

import com.google.gson.annotations.SerializedName
import java.util.*

data class ReviewResponse(

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

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("review_by")
	val reviewBy: String? = null
)
