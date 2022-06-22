package com.rootscare.data.model.request.commonuseridrequest

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonUserIdRequest(
	@field:JsonProperty("id")
	@field:SerializedName("id")
	var id: String? = null,
	@field:JsonProperty("service_type")
	@field:SerializedName("service_type")
	var service_type: String? = null
)

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class NeedSupportRequest(
	@field:JsonProperty("user_id")
	@field:SerializedName("user_id")
	var user_id: String? = null,

	@field:JsonProperty("service_type")
	@field:SerializedName("service_type")
	var service_type: String? = null,

	@field:JsonProperty("issue_topic")
	@field:SerializedName("issue_topic")
	var issue_topic: String? = null,

	@field:JsonProperty("message")
	@field:SerializedName("message")
	var message: String? = null
)

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonNotificationIdRequest(
	@field:JsonProperty("id")
	@field:SerializedName("id")
	var id: String? = null,
	@field:JsonProperty("read")
	@field:SerializedName("read")
	var read: String? = null
)
