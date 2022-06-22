package com.rootscare.data.model.response.loginresponse

import com.google.gson.annotations.SerializedName

data class TaskListResponse(
	@field:SerializedName("result")
	val result: ArrayList<ResultItem?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ResultItem(

//	@field:SerializedName("duration")
//	val duration: String? = null,
//
	@field:SerializedName("price")
	var price: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	var isChecked: Boolean? = false
)
