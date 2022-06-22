package com.rootscare.data.model.request.fetchScheduleData

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class FetchScheduleRequest(
	@field:JsonProperty("service_type")
	@field:SerializedName("service_type")
	var service_type: String? = null,
	@field:JsonProperty("user_id")
	@field:SerializedName("user_id")
	var user_id: String? = null
)