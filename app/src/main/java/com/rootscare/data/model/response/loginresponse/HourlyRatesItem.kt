package com.rootscare.data.model.response.loginresponse

import com.google.gson.annotations.SerializedName

data class HourlyRatesItem(

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("price")
	val price: String? = null
)